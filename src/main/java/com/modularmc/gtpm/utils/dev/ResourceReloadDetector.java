package com.modularmc.gtpm.utils.dev;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.config.ConfigHolder;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@TransVariants(keyPrefix = "gtpm.debug.resource_rebuild",
               value = {
                       @Variant(id = "done",
                                source = "Gradle resource rebuild done in %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "Gradle资源重建在%s内完成")),
                       @Variant(id = "start",
                                source = "Invoking gradle resource rebuild (./gradlew :processResources)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "正在调用Gradle资源重建（./gradlew :processResources）")),
               })
@ApiStatus.Internal
public class ResourceReloadDetector {

    private static final Path gradleDir = findGradleDir();

    @ApiStatus.Internal
    public static CompletableFuture<Void> regenerateResourcesOnReload(Supplier<CompletableFuture<Void>> reloadFuture) {
        if (!ConfigHolder.INSTANCE.dev.autoRebuildResources || !GTPM.isDev() || gradleDir == null) {
            return reloadFuture.get();
        }
        ProcessBuilder builder = switch (Util.getPlatform()) {
            case WINDOWS -> new ProcessBuilder("cmd.exe", "/c", "gradlew.bat", ":processResources");
            default -> new ProcessBuilder("./gradlew", ":processResources");
        };
        builder.directory(gradleDir.toFile());
        builder.inheritIO();
        Process process;
        try {
            process = builder.start();
        } catch (IOException exception) {
            GTPM.LOGGER.error("Cound not run ./gradlew :processResources", exception);
            GTPM.LOGGER.error("Message the GTPM developers about this!");
            return reloadFuture.get();
        }
        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("gtpm.debug.resource_rebuild.start"));
        Instant start = Instant.now();
        // wait for the resource reload to finish, then send chat message, then let MC actually reload resources
        return process.toHandle().onExit()
                .thenRun(() -> Minecraft.getInstance().player
                        .sendSystemMessage(Component.translatable("gtpm.debug.resource_rebuild.done",
                                Duration.between(start, Instant.now()))))
                .thenCompose($ -> reloadFuture.get());
    }

    private static @Nullable Path findGradleDir() {
        Path path = Path.of(".").toAbsolutePath();
        do {
            if (Files.isRegularFile(path.resolve("settings.gradle")) ||
                    Files.isRegularFile(path.resolve("settings.gradle.kts"))) {
                return path;
            }
            path = path.getParent();
        } while (path.getParent() != null);

        return null;
    }
}

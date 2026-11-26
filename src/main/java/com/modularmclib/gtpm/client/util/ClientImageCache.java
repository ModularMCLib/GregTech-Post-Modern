package com.modularmclib.gtpm.client.util;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.misc.ImageCache;
import com.modularmclib.gtpm.api.misc.ImageRequestLifecycle;
import com.modularmclib.gtpm.api.misc.ImageRequestResult;
import com.modularmclib.gtpm.common.network.packets.CPacketImageRequest;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.platform.NativeImage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@OnlyIn(Dist.CLIENT)
public class ClientImageCache {

    private static final ImageRequestLifecycle REQUESTS = new ImageRequestLifecycle();
    // TODO make some kind of loading icon for this
    private static final AbstractTexture LOADING_TEXTURE_MARKER = new SimpleTexture(
            GTPM.id("textures/block/void.png"));
    private static final LoadingCache<String, AbstractTexture> CACHE = CacheBuilder.newBuilder()
            .refreshAfterWrite(ImageCache.REFRESH_SECS, TimeUnit.SECONDS)
            .expireAfterAccess(ImageCache.EXPIRE_SECS, TimeUnit.SECONDS)
            .build(CacheLoader.from(url -> {
                ImageRequestLifecycle.StartResult request = REQUESTS.restart(url);
                if (request.outcome() == ImageRequestLifecycle.StartOutcome.STARTED) {
                    GTPM.LOGGER.debug("Requesting image {} with request id {}", url, request.requestId());
                    try {
                        PacketDistributor.sendToServer(new CPacketImageRequest(url, request.requestId()));
                    } catch (RuntimeException exception) {
                        REQUESTS.receiveFailure(url, request.requestId(), ImageRequestResult.Status.INTERNAL_ERROR);
                        GTPM.LOGGER.error("Could not send Central Monitor image request {} for {}",
                                request.requestId(), url, exception);
                        throw exception;
                    }
                }
                return LOADING_TEXTURE_MARKER;
            }));

    private static @NotNull ResourceLocation getUrlTextureId(String url) {
        return GTPM.id("textures/central_monitor/image_" + url.hashCode());
    }

    public static @Nullable ResourceLocation getOrLoadTexture(@Nullable String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        try {
            AbstractTexture texture = CACHE.get(url);
            if (texture == LOADING_TEXTURE_MARKER) {
                return null;
            }
            return getUrlTextureId(url);
        } catch (ExecutionException | RuntimeException exception) {
            CACHE.invalidate(url);
            GTPM.LOGGER.error("Could not load image {}", url, exception);
            return null;
        }
    }

    @ApiStatus.Internal
    public static void receiveImagePart(String url, long requestId, byte[] imagePart, int index,
                                        final int totalParts) {
        ImageRequestLifecycle.ReceiveResult result = REQUESTS.receivePart(
                url, requestId, imagePart, index, totalParts);
        if (result.outcome() == ImageRequestLifecycle.ReceiveOutcome.COMPLETE) {
            try {
                saveTexture(url, result.imageBytes());
            } catch (IOException | RuntimeException exception) {
                CACHE.invalidate(url);
                GTPM.LOGGER.error("Could not decode Central Monitor image {} from request {}",
                        url, requestId, exception);
            }
        } else if (result.outcome() == ImageRequestLifecycle.ReceiveOutcome.REJECTED) {
            CACHE.invalidate(url);
            GTPM.LOGGER.warn("Rejected invalid image response part {} of {} for request {} ({})",
                    index, totalParts, requestId, url);
        } else if (result.outcome() == ImageRequestLifecycle.ReceiveOutcome.IGNORED) {
            GTPM.LOGGER.debug("Ignoring stale image response part for request {} ({})", requestId, url);
        }
    }

    @ApiStatus.Internal
    public static void receiveImageFailure(String url, long requestId, ImageRequestResult.Status status) {
        ImageRequestLifecycle.ReceiveResult result = REQUESTS.receiveFailure(url, requestId, status);
        if (result.outcome() == ImageRequestLifecycle.ReceiveOutcome.FAILED) {
            CACHE.invalidate(url);
            GTPM.LOGGER.warn("Central Monitor image request {} for {} failed: {}", requestId, url, status);
        } else {
            GTPM.LOGGER.debug("Ignoring stale image failure for request {} ({})", requestId, url);
        }
    }

    private static void saveTexture(String url, byte[] imageBytes) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(imageBytes.length);
        buffer.put(imageBytes).flip();
        DynamicTexture texture = new DynamicTexture(NativeImage.read(buffer));

        Minecraft.getInstance().getTextureManager().register(getUrlTextureId(url), texture);

        CACHE.put(url, texture);
    }
}

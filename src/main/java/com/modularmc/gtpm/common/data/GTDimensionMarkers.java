package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.data.DimensionMarker;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.common.recipe.condition.DimensionCondition;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
import com.modularmc.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("unused")
@TransVariants(keyPrefix = "block.gtpm",
               value = {
                       @Variant(id = "overworld_marker",
                                source = "Overworld",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "主世界")),
                       @Variant(id = "the_end_marker",
                                source = "The End",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "末地")),
                       @Variant(id = "the_nether_marker",
                                source = "The Nether",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "下界")),
               })
public class GTDimensionMarkers {

    static {
        REGISTRATE.resetCreativeModeTab();
    }

    public static final BlockEntry<Block> OVERWORLD_MARKER = createMarker("overworld");
    public static final BlockEntry<Block> NETHER_MARKER = createMarker("the_nether");
    public static final BlockEntry<Block> END_MARKER = createMarker("the_end");

    public static final DimensionMarker OVERWORLD = createAndRegister(Level.OVERWORLD, 0,
            () -> OVERWORLD_MARKER, null);
    public static final DimensionMarker NETHER = createAndRegister(Level.NETHER, 0,
            () -> NETHER_MARKER, null);
    public static final DimensionMarker END = createAndRegister(Level.END, 0,
            () -> END_MARKER, null);

    public static DimensionMarker createAndRegister(ResourceKey<Level> dimension, int tier, Supplier<ItemLike> supplier,
                                                    @Nullable Component overrideName) {
        if (overrideName == null) {
            // if a special name hasn't been set, use the dimension's 'translated' name as a default
            overrideName = DimensionCondition.getDimensionName(dimension);
        }

        DimensionMarker marker = new DimensionMarker(tier, supplier, overrideName);
        GTRegistries.register(GTRegistries.DIMENSION_MARKERS, dimension.location(), marker);
        return marker;
    }

    private static BlockEntry<Block> createMarker(String name) {
        return REGISTRATE.block("%s_marker".formatted(name), Block::new)
                .lang(FormattingUtil.toEnglishName(name))
                .blockstate(() -> (ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cube(ctx.getName(),
                        prov.modLoc("block/dim_markers/%s/down".formatted(name)),
                        prov.modLoc("block/dim_markers/%s/up".formatted(name)),
                        prov.modLoc("block/dim_markers/%s/north".formatted(name)),
                        prov.modLoc("block/dim_markers/%s/south".formatted(name)),
                        prov.modLoc("block/dim_markers/%s/east".formatted(name)),
                        prov.modLoc("block/dim_markers/%s/west".formatted(name)))
                        .texture("particle", "#north")
                        .guiLight(BlockModel.GuiLight.FRONT)))
                .simpleItem()
                .register();
    }

    public static void init() {}
}

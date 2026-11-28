package com.modularmc.gtpm.common.block;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;

import java.util.function.Consumer;

public enum StoneBlockType {

    STONE("stone", "%s",
            text -> text.source("%s").translation(MinecraftLocale.ZH_CN, "%s")),
    COBBLE("cobble", "%s_cobblestone", 2.0f, 6.0f,
            text -> text.source("%s Cobblestone").translation(MinecraftLocale.ZH_CN, "%s圆石")),
    COBBLE_MOSSY("cobble_mossy", "mossy_%s_cobblestone", 2.0f, 6.0f,
            text -> text.source("Mossy %s Cobblestone").translation(MinecraftLocale.ZH_CN, "覆苔%s圆石")),
    POLISHED("polished", "polished_%s",
            text -> text.source("Polished %s").translation(MinecraftLocale.ZH_CN, "磨制%s")),
    BRICKS("bricks", "%s_bricks",
            text -> text.source("%s Bricks").translation(MinecraftLocale.ZH_CN, "%s砖")),
    BRICKS_CRACKED("bricks_cracked", "cracked_%s_bricks",
            text -> text.source("Cracked %s Bricks").translation(MinecraftLocale.ZH_CN, "裂纹%s砖")),
    BRICKS_MOSSY("bricks_mossy", "mossy_%s_bricks",
            text -> text.source("Mossy %s Bricks").translation(MinecraftLocale.ZH_CN, "覆苔%s砖")),
    CHISELED("chiseled", "chiseled_%s",
            text -> text.source("Chiseled %s").translation(MinecraftLocale.ZH_CN, "雕纹%s")),
    TILED("tiled", "%s_tile",
            text -> text.source("%s Tile").translation(MinecraftLocale.ZH_CN, "%s瓦")),
    TILED_SMALL("tiled_small", "%s_small_tile",
            text -> text.source("%s Small Tile").translation(MinecraftLocale.ZH_CN, "%s小片瓦")),
    WINDMILL_A("windmill_a", "%s_windmill_a",
            text -> text.source("%s Windmill A").translation(MinecraftLocale.ZH_CN, "%s风车形瓦 A")),
    WINDMILL_B("windmill_b", "%s_windmill_b",
            text -> text.source("%s Windmill B").translation(MinecraftLocale.ZH_CN, "%s风车形瓦 B")),
    BRICKS_SMALL("bricks_small", "small_%s_bricks",
            text -> text.source("Small %s Bricks").translation(MinecraftLocale.ZH_CN, "%s小块砖")),
    BRICKS_SQUARE("bricks_square", "square_%s_bricks",
            text -> text.source("Square %s Bricks").translation(MinecraftLocale.ZH_CN, "%s方形砖"));

    public final String id;
    public final String blockId;
    public final float hardness;
    public final float resistance;

    public final LocalizationDeclaration namePattern;

    StoneBlockType(String id, String blockId, Consumer<LocalizationDeclaration.Builder> localization) {
        this(id, blockId, 1.5f, 6.0f, localization); // vanilla stone stats
    }

    StoneBlockType(String id, String blockId, float hardness, float resistance,
                   Consumer<LocalizationDeclaration.Builder> localization) {
        this.id = id;
        this.blockId = blockId;
        this.hardness = hardness;
        this.resistance = resistance;
        var text = new LocalizationDeclaration.Builder();
        localization.accept(text);
        this.namePattern = text.build();
    }
}

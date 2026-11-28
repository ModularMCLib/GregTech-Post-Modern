package com.modularmc.gtpm.common.cover.data;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.core.Direction;

import lombok.Getter;

@Trans(key = "cover.machine_controller.mode.null",
       source = "Control Nothing",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：无"))
public enum ControllerMode {

    @Trans(key = "cover.machine_controller.mode.machine",
           source = "Control Machine",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：机器"))
    MACHINE("cover.machine_controller.mode.machine", null),
    @Trans(key = "cover.machine_controller.mode.cover_up",
           source = "Control Cover (Top)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：覆盖板（顶面）"))
    COVER_UP("cover.machine_controller.mode.cover_up", Direction.UP),
    @Trans(key = "cover.machine_controller.mode.cover_down",
           source = "Control Cover (Bottom)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：覆盖板（底面）"))
    COVER_DOWN("cover.machine_controller.mode.cover_down", Direction.DOWN),
    @Trans(key = "cover.machine_controller.mode.cover_north",
           source = "Control Cover (North)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：覆盖板（北面）"))
    COVER_NORTH("cover.machine_controller.mode.cover_north", Direction.NORTH),
    @Trans(key = "cover.machine_controller.mode.cover_east",
           source = "Control Cover (East)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：覆盖板（东面）"))
    COVER_EAST("cover.machine_controller.mode.cover_east", Direction.EAST),
    @Trans(key = "cover.machine_controller.mode.cover_south",
           source = "Control Cover (South)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：覆盖板（南面）"))
    COVER_SOUTH("cover.machine_controller.mode.cover_south", Direction.SOUTH),
    @Trans(key = "cover.machine_controller.mode.cover_west",
           source = "Control Cover (West)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制目标：覆盖板（西面）"))
    COVER_WEST("cover.machine_controller.mode.cover_west", Direction.WEST);

    @Getter
    public final String localeName;
    public final Direction side;

    ControllerMode(String localeName, Direction side) {
        this.localeName = localeName;
        this.side = side;
    }

    public static final String nullLocaleName = "cover.machine_controller.mode.null";
}

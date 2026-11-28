package com.modularmc.gtpm.utils.input;

import com.modularmc.gtpm.GTPM;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;

import com.mojang.blaze3d.platform.InputConstants;

@TransVariants(keyPrefix = "gtpm.key",
               value = {
                       @Variant(id = "armor_charging",
                                source = "Armor Charging to Inventory Toggle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用/禁用盔甲向物品栏充能")),
                       @Variant(id = "armor_hover",
                                source = "Armor Hover Toggle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换盔甲悬浮模式")),
                       @Variant(id = "armor_mode_switch",
                                source = "Armor Mode Switch",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换盔甲模式")),
                       @Variant(id = "enable_boots",
                                source = "Enable Boosted Jump",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用跳跃提升")),
                       @Variant(id = "enable_jetpack",
                                source = "Enable Jetpack",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用喷气背包")),
                       @Variant(id = "enable_step_assist",
                                source = "Enable StepAssist",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用步行辅助")),
                       @Variant(id = "tool_aoe_change",
                                source = "Tool AoE Mode Switch",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换工具范围模式")),
               })
public final class SyncedKeyMappings {

    // MC keymappings
    public static final SyncedKeyMapping VANILLA_JUMP = SyncedKeyMapping
            .createFromMC(() -> () -> Minecraft.getInstance().options.keyJump);
    public static final SyncedKeyMapping VANILLA_SNEAK = SyncedKeyMapping
            .createFromMC(() -> () -> Minecraft.getInstance().options.keyShift);
    public static final SyncedKeyMapping VANILLA_FORWARD = SyncedKeyMapping
            .createFromMC(() -> () -> Minecraft.getInstance().options.keyUp);
    public static final SyncedKeyMapping VANILLA_BACKWARD = SyncedKeyMapping
            .createFromMC(() -> () -> Minecraft.getInstance().options.keyDown);
    public static final SyncedKeyMapping VANILLA_LEFT = SyncedKeyMapping
            .createFromMC(() -> () -> Minecraft.getInstance().options.keyLeft);
    public static final SyncedKeyMapping VANILLA_RIGHT = SyncedKeyMapping
            .createFromMC(() -> () -> Minecraft.getInstance().options.keyRight);

    // GT keymappings
    public static final SyncedKeyMapping ARMOR_MODE_SWITCH = SyncedKeyMapping.createConfigurable(
            "gtpm.key.armor_mode_switch", KeyConflictContext.IN_GAME, InputConstants.KEY_M);
    public static final SyncedKeyMapping ARMOR_HOVER = SyncedKeyMapping.createConfigurable(
            "gtpm.key.armor_hover", KeyConflictContext.IN_GAME, InputConstants.KEY_H);
    public static final SyncedKeyMapping JETPACK_ENABLE = SyncedKeyMapping.createConfigurable(
            "gtpm.key.enable_jetpack", KeyConflictContext.IN_GAME, InputConstants.KEY_G);
    public static final SyncedKeyMapping BOOTS_ENABLE = SyncedKeyMapping.createConfigurable(
            "gtpm.key.enable_boots", KeyConflictContext.IN_GAME, InputConstants.KEY_PERIOD);
    public static final SyncedKeyMapping ARMOR_CHARGING = SyncedKeyMapping.createConfigurable(
            "gtpm.key.armor_charging", KeyConflictContext.IN_GAME, InputConstants.KEY_N);
    public static final SyncedKeyMapping TOOL_AOE_CHANGE = SyncedKeyMapping.createConfigurable(
            "gtpm.key.tool_aoe_change", KeyConflictContext.IN_GAME, InputConstants.KEY_V);
    public static final SyncedKeyMapping STEP_ASSIST_ENABLE = SyncedKeyMapping.createConfigurable(
            "gtpm.key.enable_step_assist", KeyConflictContext.IN_GAME,
            InputConstants.KEY_APOSTROPHE);

    public static void init() {
        if (GTPM.isClientSide()) {
            NeoForge.EVENT_BUS.register(SyncedKeyMapping.class);
        }
        ModLoader.postEvent(new SyncedKeyMappingEvent());
    }

    private SyncedKeyMappings() {}
}

package com.modularmc.gtpm.client;

import com.modularmc.gtpm.api.item.ComponentItem;
import com.modularmc.gtpm.api.item.armor.ArmorComponentItem;
import com.modularmc.gtpm.api.item.component.IItemComponent;
import com.modularmc.gtpm.api.item.component.IItemHUDProvider;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@Trans(key = "mataarmor.hud.supply_mode",
       source = "Supply Mode: %s",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "供能模式：%s"))
@TransVariants(keyPrefix = "metaarmor",
               value = {
                       @Variant(id = "hud.engine_enabled",
                                source = "Engine Enabled: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "引擎已启用：%s")),
                       @Variant(id = "hud.gravi_engine",
                                source = "GraviEngine: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "重力引擎：%s")),
                       @Variant(id = "hud.hover_mode",
                                source = "Hover Mode: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "悬浮模式：%s")),
                       @Variant(id = "hud.status.disabled",
                                source = "§cOFF",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c关")),
                       @Variant(id = "hud.status.enabled",
                                source = "§aON",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a开")),
               })
public class HudGuiOverlay implements LayeredDraw.Layer {

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker tracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isWindowActive() && mc.level != null &&
                !mc.gui.getDebugOverlay().showDebugScreen() &&
                !mc.options.hideGui) {
            renderHUDMetaArmor(mc.player.getItemBySlot(EquipmentSlot.HEAD), guiGraphics);
            renderHUDMetaArmor(mc.player.getItemBySlot(EquipmentSlot.CHEST), guiGraphics);
            renderHUDMetaArmor(mc.player.getItemBySlot(EquipmentSlot.LEGS), guiGraphics);
            renderHUDMetaArmor(mc.player.getItemBySlot(EquipmentSlot.FEET), guiGraphics);
            renderHUDMetaItem(mc.player.getItemInHand(InteractionHand.MAIN_HAND), guiGraphics);
            renderHUDMetaItem(mc.player.getItemInHand(InteractionHand.OFF_HAND), guiGraphics);
        }
    }

    private static void renderHUDMetaArmor(@NotNull ItemStack stack, GuiGraphics guiGraphics) {
        if (stack.getItem() instanceof ArmorComponentItem valueItem) {
            if (valueItem.getArmorLogic() instanceof IItemHUDProvider provider) {
                IItemHUDProvider.tryDrawHud(provider, stack, guiGraphics);
            }
        }
    }

    private static void renderHUDMetaItem(@NotNull ItemStack stack, GuiGraphics guiGraphics) {
        if (stack.getItem() instanceof ComponentItem valueItem) {
            for (IItemComponent behaviour : valueItem.getComponents()) {
                if (behaviour instanceof IItemHUDProvider provider) {
                    IItemHUDProvider.tryDrawHud(provider, stack, guiGraphics);
                }
            }
        }
    }
}

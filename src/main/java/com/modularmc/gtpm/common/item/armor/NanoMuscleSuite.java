package com.modularmc.gtpm.common.item.armor;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IElectricItem;
import com.modularmc.gtpm.api.item.armor.ArmorLogicSuite;
import com.modularmc.gtpm.api.item.armor.ArmorUtils;
import com.modularmc.gtpm.api.item.datacomponents.GTArmor;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.data.GTItems;
import com.modularmc.gtpm.utils.input.SyncedKeyMappings;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@TransVariants(keyPrefix = "metaarmor",
               value = {
                       @Variant(id = "nms.nightvision.enabled",
                                source = "NanoMuscle™ Suite: NightVision Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：夜视已启用")),
                       @Variant(id = "nms.nightvision.error",
                                source = "NanoMuscle™ Suite: §cNot enough power!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：§c能量不足！")),
                       @Variant(id = "nms.share.disable",
                                source = "NanoMuscle™ Suite: Charging Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：供能模式已禁用")),
                       @Variant(id = "nms.share.enable",
                                source = "NanoMuscle™ Suite: Charging Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：供能模式已启用")),
                       @Variant(id = "nms.step_assist.disabled",
                                source = "NanoMuscle™ Suite: StepAssist Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：步行辅助已禁用")),
                       @Variant(id = "nms.step_assist.enabled",
                                source = "NanoMuscle™ Suite: StepAssist Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：步行辅助已启用")),
                       @Variant(id = "tooltip.falldamage",
                                source = "Nullifies Fall Damage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消除摔落伤害")),
                       @Variant(id = "nms.nightvision.disabled",
                                source = "NanoMuscle™ Suite: NightVision Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米肌体™套装：夜视已禁用")),
                       @Variant(id = "tooltip.stepassist",
                                source = "Provides Step-Assist",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "提供步行辅助")),
               })
public class NanoMuscleSuite extends ArmorLogicSuite implements IStepAssist {

    @OnlyIn(Dist.CLIENT)
    protected ArmorUtils.ModularHUD HUD;

    public NanoMuscleSuite(ArmorItem.Type slot, int energyPerUse, long maxCapacity, int tier) {
        super(energyPerUse, maxCapacity, tier, slot);
        if (GTPM.isClientSide() && this.shouldDrawHUD()) {
            // noinspection NewExpressionSideOnly
            HUD = new ArmorUtils.ModularHUD();
        }
    }

    @Override
    public void onArmorTick(Level world, Player player, ItemStack stack) {
        IElectricItem item = GTCapabilityHelper.getElectricItem(stack);
        if (item == null) {
            return;
        }
        GTArmor.Mutable data = stack.getOrDefault(GTDataComponents.ARMOR_DATA, GTArmor.EMPTY).toMutable();
        byte toggleTimer = data.toggleTimer();
        int nightVisionTimer = data.nightVisionTimer();

        if (type == ArmorItem.Type.HELMET) {
            boolean nightVision = data.nightVision();
            if (toggleTimer == 0 && SyncedKeyMappings.ARMOR_MODE_SWITCH.isKeyDown(player)) {
                nightVision = !nightVision;
                toggleTimer = 5;
                if (item.getCharge() < ArmorUtils.MIN_NIGHTVISION_CHARGE) {
                    nightVision = false;
                    if (world.isClientSide())
                        player.displayClientMessage(Component.translatable("metaarmor.nms.nightvision.error"), true);
                } else {
                    if (world.isClientSide()) player.displayClientMessage(Component
                            .translatable("metaarmor.nms.nightvision." + (nightVision ? "enabled" : "disabled")), true);
                }
            }

            if (nightVision) {
                player.removeEffect(MobEffects.BLINDNESS);
                float tickRate = world.tickRateManager().tickrate();
                if (nightVisionTimer <= ArmorUtils.NIGHT_VISION_RESET * tickRate) {
                    nightVisionTimer = Mth.floor(ArmorUtils.NIGHTVISION_DURATION * tickRate);
                    player.addEffect(
                            new MobEffectInstance(MobEffects.NIGHT_VISION, nightVisionTimer, 0, true,
                                    false));
                    item.discharge(4, this.tier, true, false, false);
                }
            } else {
                player.removeEffect(MobEffects.NIGHT_VISION);
            }
            data.nightVision(nightVision);

        }

        if (nightVisionTimer > 0) nightVisionTimer--;
        if (toggleTimer > 0) toggleTimer--;

        data.nightVisionTimer(nightVisionTimer);
        data.toggleTimer(toggleTimer);
        stack.set(GTDataComponents.ARMOR_DATA, data.toImmutable());
    }

    public static void disableNightVision(@NotNull Level world, Player player, boolean sendMsg) {
        if (!world.isClientSide) {
            player.removeEffect(MobEffects.NIGHT_VISION);
            if (sendMsg)
                player.displayClientMessage(Component.translatable("metaarmor.nms.nightvision.disabled"), true);
        }
    }

    public boolean handleUnblockableDamage(LivingEntity entity, @NotNull ItemStack armor, DamageSource source,
                                           double damage, ArmorItem.Type equipmentSlot) {
        return source.is(DamageTypes.FALL);
    }

    /*
     * @Override
     * public ArmorProperties getProperties(EntityLivingBase player, @NotNull ItemStack armor, DamageSource source,
     * double damage, ArmorItem.Type equipmentSlot) {
     * IElectricItem container = armor.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
     * int damageLimit = Integer.MAX_VALUE;
     * if (source == DamageSource.FALL && this.getEquipmentSlot(armor) == ArmorItem.Type.FEET) {
     * if (energyPerUse > 0 && container != null) {
     * damageLimit = (int) Math.min(damageLimit, 25.0 * container.getCharge() / (energyPerUse * 10.0D));
     * }
     * return new ArmorProperties(10, (damage < 8.0) ? 1.0 : 0.875, damageLimit);
     * }
     * return super.getProperties(player, armor, source, damage, equipmentSlot);
     * }
     */

    @Override
    public int damageArmor(@Nullable LivingEntity entity, ItemStack itemStack,
                           int damage, EquipmentSlot equipmentSlot) {
        IElectricItem item = GTCapabilityHelper.getElectricItem(itemStack);
        if (item != null) {
            item.discharge(energyPerUse / 10L * damage, item.getTier(), true, false, false);
        }
        return super.damageArmor(entity, itemStack, damage, equipmentSlot);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot,
                                            ArmorMaterial.Layer layer) {
        ItemStack currentChest = Minecraft.getInstance().player.getInventory()
                .getArmor(ArmorItem.Type.CHESTPLATE.getSlot().getIndex());
        ItemStack advancedChest = GTItems.NANO_CHESTPLATE_ADVANCED.asStack();
        String armorTexture = "nano_muscule_suite";
        if (advancedChest.is(currentChest.getItem())) armorTexture = "advanced_nano_muscle_suite";
        return slot != EquipmentSlot.LEGS ?
                GTPM.id(String.format("textures/armor/%s_1.png", armorTexture)) :
                GTPM.id(String.format("textures/armor/%s_2.png", armorTexture));
    }

    @Override
    public double getDamageAbsorption() {
        return 1.0D;
    }

    @Override
    public float getHeatResistance() {
        return 0.75f;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawHUD(ItemStack item, GuiGraphics guiGraphics) {
        addCapacityHUD(item, this.HUD);
        this.HUD.draw(guiGraphics);
        this.HUD.reset();
    }

    @Override
    public void addInfo(ItemStack itemStack, List<Component> lines) {
        super.addInfo(itemStack, lines);
        if (type == ArmorItem.Type.HELMET) {
            GTArmor data = itemStack.getOrDefault(GTDataComponents.ARMOR_DATA, GTArmor.EMPTY);
            boolean nv = data.nightVision();
            if (nv) {
                lines.add(Component.translatable("metaarmor.message.nightvision.enabled"));
            } else {
                lines.add(Component.translatable("metaarmor.message.nightvision.disabled"));
            }
        } else if (type == ArmorItem.Type.BOOTS) {
            lines.add(Component.translatable("metaarmor.tooltip.stepassist"));
            lines.add(Component.translatable("metaarmor.tooltip.falldamage"));
        }
    }
}

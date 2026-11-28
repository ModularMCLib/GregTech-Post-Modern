package com.modularmc.gtpm.common.item.behavior;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IMedicalConditionTracker;
import com.modularmc.gtpm.api.data.medicalcondition.MedicalCondition;
import com.modularmc.gtpm.api.item.component.IAddInformation;
import com.modularmc.gtpm.api.item.component.IInteractionItem;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines an antidote for a hazard (e.g. poisoning)
 *
 * @param types         the type of the hazard to remove
 * @param removePercent the time to remove from the chosen hazard, as a percentage of the current time [0, 100].
 *                      -1 for all.
 */
@TransVariants(keyPrefix = "gtpm.medical_condition.antidote",
               value = {
                       @Variant(id = "description",
                                source = "§aAntidote §7Hold Shift to show details",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a解毒剂 §7按住Shift以显示详情")),
                       @Variant(id = "description_shift",
                                source = "§aCures these conditions:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a治愈以下疾病：")),
                       @Variant(id = "description.effect_removed",
                                source = "Removes %s%% of current conditions' effects",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "疾病效果持续时间降低%s%%")),
                       @Variant(id = "description.effect_removed.all",
                                source = "Removes all of current conditions' effects",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消除所有疾病效果")),
               })
public record AntidoteBehavior(Set<MedicalCondition> types, int removePercent)
        implements IInteractionItem, IAddInformation {

    public AntidoteBehavior(int timeToRemove, MedicalCondition... types) {
        this(new HashSet<>(), timeToRemove);
        this.types.addAll(Arrays.asList(types));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack itemstack = IInteractionItem.super.finishUsingItem(stack, level, livingEntity);
        IMedicalConditionTracker tracker = null;
        if (livingEntity instanceof Player player) {
            tracker = GTCapabilityHelper.getMedicalConditionTracker(player);
        }
        if (tracker == null) {
            return itemstack;
        }
        for (var entry : tracker.getMedicalConditions().object2FloatEntrySet()) {
            MedicalCondition condition = entry.getKey();
            if (condition == null) {
                continue;
            }
            if (!this.types.contains(condition)) {
                continue;
            }
            if (removePercent == -1) {
                tracker.removeMedicalCondition(condition);
            } else {
                float time = entry.getFloatValue();
                float timeToRemove = time * (removePercent / 100.0f);
                if (timeToRemove > 0.05f * time) {
                    tracker.removeMedicalCondition(condition);
                    continue;
                }
                tracker.heal(condition, (int) timeToRemove);
            }
        }
        return itemstack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext context,
                                List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        if (!GTPMConfigHolder.INSTANCE.gameplay.hazardsEnabled) return;

        if (GTUtil.isShiftDown()) {
            tooltipComponents.add(Component.translatable("gtpm.medical_condition.antidote.description_shift"));
            for (var type : types) {
                tooltipComponents.add(Component.translatable("gtpm.medical_condition." + type.name));
            }
            if (removePercent == -1) {
                tooltipComponents
                        .add(Component.translatable("gtpm.medical_condition.antidote.description.effect_removed.all"));
            } else {
                tooltipComponents
                        .add(Component.translatable("gtpm.medical_condition.antidote.description.effect_removed",
                                removePercent));
            }
            return;
        }
        tooltipComponents.add(Component.translatable("gtpm.medical_condition.antidote.description"));
    }
}

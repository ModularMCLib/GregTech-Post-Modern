package com.modularmc.gtpm.common.item.behavior;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IElectricItem;
import com.modularmc.gtpm.api.item.component.IAddInformation;
import com.modularmc.gtpm.api.item.component.IInteractionItem;
import com.modularmc.gtpm.api.item.component.IItemLifeCycle;
import com.modularmc.gtpm.common.data.GTDataComponents;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

@Trans(key = "behavior.toggle_energy_consumer.tooltip",
       source = "Use to toggle mode",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击切换模式"))
public class ToggleEnergyConsumerBehavior implements IInteractionItem, IItemLifeCycle, IAddInformation {

    private final int energyUsagePerTick;

    public ToggleEnergyConsumerBehavior(int energyUsagePerTick) {
        this.energyUsagePerTick = energyUsagePerTick;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack item, Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            IElectricItem electricItem = GTCapabilityHelper.getElectricItem(item);
            boolean isItemActive = isItemActive(item);
            if (isItemActive) {
                setItemActive(item, false);
            } else if (electricItem != null && drainActivationEnergy(electricItem, true)) {
                setItemActive(item, true);
            }
        }
        return InteractionResultHolder.pass(item);
    }

    private boolean drainActivationEnergy(IElectricItem electricItem, boolean simulate) {
        return electricItem.discharge(energyUsagePerTick, electricItem.getTier(), true, false, simulate) >=
                energyUsagePerTick;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        IElectricItem electricItem = GTCapabilityHelper.getElectricItem(stack);
        if (isItemActive(stack) && electricItem != null) {
            boolean shouldRemainActive = drainActivationEnergy(electricItem, false);
            if (!shouldRemainActive) {
                setItemActive(stack, false);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("behavior.toggle_energy_consumer.tooltip"));
    }

    public static boolean isItemActive(ItemStack itemStack) {
        return itemStack.getOrDefault(GTDataComponents.ACTIVE, false);
    }

    public static void setItemActive(ItemStack itemStack, boolean isActive) {
        itemStack.set(GTDataComponents.ACTIVE, isActive);
    }
}

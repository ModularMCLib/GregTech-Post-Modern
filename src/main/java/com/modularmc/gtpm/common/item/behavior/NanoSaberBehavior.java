package com.modularmc.gtpm.common.item.behavior;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.item.component.IEnchantableItem;
import com.modularmc.gtpm.api.item.component.IItemAttributes;
import com.modularmc.gtpm.config.GTPMConfigHolder;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;

public class NanoSaberBehavior extends ToggleEnergyConsumerBehavior implements IItemAttributes, IEnchantableItem {

    public static final ResourceLocation OVERRIDE_KEY_LOCATION = GTPM.id("nano_saber_active");

    private final double baseAttackDamage;
    private final double additionalAttackDamage;

    public NanoSaberBehavior() {
        super(GTPMConfigHolder.INSTANCE.tools.nanoSaber.energyConsumption);
        this.baseAttackDamage = GTPMConfigHolder.INSTANCE.tools.nanoSaber.nanoSaberBaseDamage;
        this.additionalAttackDamage = GTPMConfigHolder.INSTANCE.tools.nanoSaber.nanoSaberDamageBoost;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        double attackDamage = baseAttackDamage + (isItemActive(stack) ? additionalAttackDamage : 0.0D);
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND)
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND)
                .build();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 33;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.value().getEffects(EnchantmentEffectComponents.REPAIR_WITH_XP).isEmpty() &&
                enchantment.value().getEffects(EnchantmentEffectComponents.ITEM_DAMAGE).isEmpty() &&
                Items.IRON_SWORD.getDefaultInstance().supportsEnchantment(enchantment);
    }
}

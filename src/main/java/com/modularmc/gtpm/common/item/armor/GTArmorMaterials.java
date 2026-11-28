package com.modularmc.gtpm.common.item.armor;

import com.modularmc.gtpm.GTPM;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.EnumMap;
import java.util.List;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTArmorMaterials {

    public static final List<ArmorMaterial.Layer> BLANK_LAYERS = List.of(new ArmorMaterial.Layer(GTPM.id("armor")));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GOGGLES = REGISTRATE.generic("goggles", Registries.ARMOR_MATERIAL,
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
            }), 0, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.EMPTY, BLANK_LAYERS, 0.0F, 0.0F))
            .register();
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> JETPACK = REGISTRATE.generic("jetpack", Registries.ARMOR_MATERIAL,
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
            }), 0, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.EMPTY, BLANK_LAYERS, 0.0F, 0.0F))
            .register();
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ARMOR = REGISTRATE.generic("armor", Registries.ARMOR_MATERIAL,
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.HELMET, 0);
            }), 0, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.EMPTY, BLANK_LAYERS, 5.0F, 0.0F))
            .register();
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BAD_PPE_EQUIPMENT = REGISTRATE
            .generic("bad_ppe_equipment", Registries.ARMOR_MATERIAL,
                    () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 1);
                        map.put(ArmorItem.Type.LEGGINGS, 2);
                        map.put(ArmorItem.Type.CHESTPLATE, 3);
                        map.put(ArmorItem.Type.HELMET, 1);
                    }), 10, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.EMPTY, BLANK_LAYERS, 0.0F, 0.0F))
            .register();
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GOOD_PPE_EQUIPMENT = REGISTRATE
            .generic("good_ppe_equipment", Registries.ARMOR_MATERIAL,
                    () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                    }), 10, SoundEvents.ARMOR_EQUIP_GENERIC, () -> Ingredient.EMPTY, BLANK_LAYERS, 0.0F, 0.0F))
            .register();

    public static void init() {}
}

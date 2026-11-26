package com.modularmclib.gtpm.integration.jei.subtype;

import com.modularmclib.gtpm.api.data.chemical.material.Material;
import com.modularmclib.gtpm.common.data.GTDataComponents;
import com.modularmclib.gtpm.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import org.jetbrains.annotations.Nullable;

public class MaterialSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

    public static final MaterialSubtypeInterpreter INSTANCE = new MaterialSubtypeInterpreter();

    private MaterialSubtypeInterpreter() {}

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.getOrDefault(GTDataComponents.ITEM_MATERIAL, GTMaterials.NULL);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        Material material = ingredient.getOrDefault(GTDataComponents.ITEM_MATERIAL, GTMaterials.NULL);
        if (material.isNull()) {
            return "";
        }
        return material.getUnlocalizedName();
    }
}

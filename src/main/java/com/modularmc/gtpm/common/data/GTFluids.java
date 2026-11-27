package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.fluids.store.FluidStorageKeys;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.common.fluid.potion.PotionFluid;
import com.modularmc.gtpm.data.recipe.CustomTags;

import com.modularmc.registrate.util.entry.FluidEntry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTFluids {

    private static final ResourceLocation POTION_TEXTURE = GTPM.id("block/fluids/fluid.potion");

    @SuppressWarnings("UnstableApiUsage")
    public static final FluidEntry<PotionFluid> POTION = REGISTRATE
            .fluid("potion", POTION_TEXTURE, POTION_TEXTURE,
                    properties -> new PotionFluid.PotionFluidType(properties, POTION_TEXTURE, POTION_TEXTURE),
                    PotionFluid::new)
            .clientExtension(() -> () -> new IClientFluidTypeExtensions() {

                @Override
                public ResourceLocation getStillTexture() {
                    return POTION_TEXTURE;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return POTION_TEXTURE;
                }

                @Override
                public int getTintColor(FluidStack stack) {
                    return stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                            .getColor() | 0xff000000;
                }
            })
            .lang("Potion")
            .source(PotionFluid::new).noBlock().noBucket()
            .tag(CustomTags.POTION_FLUIDS)
            .register();

    public static void init() {
        // Register fluids for non-materials
        handleNonMaterialFluids(GTMaterials.Water, Fluids.WATER);
        handleNonMaterialFluids(GTMaterials.Lava, Fluids.LAVA);
        handleNonMaterialFluids(GTMaterials.Milk, NeoForgeMod.MILK);
        NeoForgeMod.enableMilkFluid();

        // register fluids for materials
        REGISTRATE.creativeModeTab(GTCreativeModeTabs.MATERIAL_FLUID);
    }

    public static void generateMaterialFluids() {
        for (var material : GTRegistries.MATERIALS) {
            var fluidProperty = material.getProperty(PropertyKey.FLUID);

            if (fluidProperty != null) {
                GTRegistrate registrate = GTRegistrate.createIgnoringListenerErrors(material.getModid());
                fluidProperty.registerFluids(material, registrate);
            }
        }
    }

    public static void handleNonMaterialFluids(@NotNull Material material, @NotNull Fluid fluid) {
        handleNonMaterialFluids(material, () -> fluid);
    }

    public static void handleNonMaterialFluids(@NotNull Material material, @NotNull Supplier<Fluid> fluid) {
        var property = material.getProperty(PropertyKey.FLUID);
        property.getStorage().store(FluidStorageKeys.LIQUID, fluid, null);
    }
}

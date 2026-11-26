package com.modularmclib.gtpm.common.data;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.data.chemical.material.Material;
import com.modularmclib.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmclib.gtpm.api.fluids.store.FluidStorageKeys;
import com.modularmclib.gtpm.api.registry.GTRegistries;
import com.modularmclib.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmclib.gtpm.common.fluid.potion.PotionFluid;
import com.modularmclib.gtpm.data.recipe.CustomTags;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;

import com.tterrag.registrate.util.entry.FluidEntry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static com.modularmclib.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTFluids {

    @SuppressWarnings("UnstableApiUsage")
    public static final FluidEntry<PotionFluid> POTION = REGISTRATE
            .fluid("potion", GTPM.id("block/fluids/fluid.potion"), GTPM.id("block/fluids/fluid.potion"),
                    PotionFluid.PotionFluidType::new, PotionFluid::new)
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

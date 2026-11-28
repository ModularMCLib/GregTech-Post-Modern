package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.fluids.store.FluidStorageKeys;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.common.fluid.potion.PotionFluid;
import com.modularmc.gtpm.data.recipe.CustomTags;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
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

@TransVariants(keyPrefix = "block.gtpm",
               value = {
                       @Variant(id = "creosote",
                                source = "Creosote",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "杂酚油")),
                       @Variant(id = "heavy_oil",
                                source = "Heavy Oil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "重油")),
                       @Variant(id = "light_oil",
                                source = "Light Oil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "轻油")),
                       @Variant(id = "natural_gas",
                                source = "Natural Gas",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "天然气")),
                       @Variant(id = "oil",
                                source = "Oil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "石油")),
                       @Variant(id = "raw_oil",
                                source = "Raw Oil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "原油")),
               })
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

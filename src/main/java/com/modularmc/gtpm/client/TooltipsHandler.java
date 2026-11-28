package com.modularmc.gtpm.client;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.HazardProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.fluids.FluidConstants;
import com.modularmc.gtpm.api.fluids.FluidState;
import com.modularmc.gtpm.api.fluids.GTFluid;
import com.modularmc.gtpm.common.data.GTFluids;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.common.fluid.potion.PotionFluidHelper;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;
import java.util.function.Consumer;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "tooltip.hold_shift",
                                source = "§7Hold SHIFT for more info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7按住§6SHIFT§7获得更多信息")),
                       @Variant(id = "tooltip.hold_ctrl",
                                source = "§7Hold CTRL for more info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7按住CTRL获得更多信息")),
                       @Variant(id = "debug.f3_h.enabled",
                                source = "GregTech has modified the debug info! For Developers: enable the misc:debug config option in the GregTech config file to see more",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "格雷科技修改了调试信息！对于开发人员：在GregTech配置文件中启用misc:debug以查看更多信息")),
                       @Variant(id = "universal.kiloliters",
                                source = "%s B",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s B")),
                       @Variant(id = "universal.parentheses",
                                source = "(%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "(%s)")),
                       @Variant(id = "universal.spaced_parentheses",
                                source = "( %s )",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "( %s )")),
                       @Variant(id = "universal.padded_spaced_parentheses",
                                source = " ( %s ) ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " ( %s ) ")),
                       @Variant(id = "fluid.state_gas",
                                source = "§aState: Gaseous",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a状态：气态")),
                       @Variant(id = "fluid.state_liquid",
                                source = "§aState: Liquid",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a状态：液态")),
                       @Variant(id = "fluid.state_plasma",
                                source = "§aState: Plasma",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a状态：等离子态")),
                       @Variant(id = "tooltip.status.trinary.false",
                                source = "False",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "假")),
                       @Variant(id = "tooltip.status.trinary.true",
                                source = "True",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "真")),
                       @Variant(id = "tooltip.status.trinary.unknown",
                                source = "Unknown",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "未知")),
                       @Variant(id = "fluid.temperature",
                                source = "§cTemperature: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c温度：%s")),
                       @Variant(id = "fluid.temperature.cryogenic",
                                source = "§bCryogenic! Handle with care!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b低温流体！轻拿轻放！")),
               })
@OnlyIn(Dist.CLIENT)
public class TooltipsHandler {

    private static final String ITEM_PREFIX = "item." + GTPM.MOD_ID;
    private static final String BLOCK_PREFIX = "block." + GTPM.MOD_ID;

    public static void appendTooltips(ItemStack stack, TooltipFlag flag, List<Component> tooltips,
                                      Item.TooltipContext context) {
        // Formula
        var materialEntry = ChemicalHelper.getMaterialEntry(stack.getItem());
        if (!materialEntry.isEmpty()) {
            var formula = materialEntry.material().getChemicalFormula();
            if (formula != null && !formula.isEmpty()) {
                tooltips.add(1, Component.literal(formula).withStyle(ChatFormatting.YELLOW));
            }
        }
        if (stack.getItem() instanceof BucketItem bucket) {
            var fluid = bucket.content;
            if (!(fluid instanceof EmptyFluid)) {
                appendFluidTooltips(new FluidStack(fluid, FluidType.BUCKET_VOLUME), tooltips::add, flag, context);
            }
        } else if (stack.getItem() instanceof MilkBucketItem) {
            appendFluidTooltips(GTMaterials.Milk.getFluid(FluidType.BUCKET_VOLUME), tooltips::add, flag, context);
        }

        // Block/Item custom tooltips
        String translationKey = stack.getDescriptionId();
        if (translationKey.startsWith(ITEM_PREFIX) || translationKey.startsWith(BLOCK_PREFIX)) {
            String tooltipKey = translationKey + ".tooltip";
            if (Language.getInstance().has(tooltipKey)) {
                tooltips.add(1, Component.translatable(tooltipKey));
            } else {
                List<MutableComponent> multiLang = LangHandler.getMultiLang(tooltipKey);
                if (multiLang != null && !multiLang.isEmpty()) {
                    tooltips.addAll(1, multiLang);
                }
            }
        }

        Material material = HazardProperty.getValidHazardMaterial(stack);
        if (material.isNull()) {
            return;
        }
        GTUtil.appendHazardTooltips(material, tooltips);
    }

    public static void appendFluidTooltips(FluidStack fluidStack, Consumer<Component> tooltips, TooltipFlag flag,
                                           Item.TooltipContext context) {
        Fluid fluid = fluidStack.getFluid();
        int amount = fluidStack.getAmount();
        FluidType fluidType = fluid.getFluidType();

        if (fluidType == GTFluids.POTION.getType()) {
            if (fluidStack.is(FluidTags.WATER)) {
                return;
            }
            PotionFluidHelper.addPotionTooltip(fluidStack, tooltips, context);
            return;
        }

        var material = ChemicalHelper.getMaterial(fluid);
        if (!material.isNull()) {
            var formula = material.getChemicalFormula();
            if (formula != null && !formula.isEmpty()) {
                tooltips.accept(Component.literal(formula).withStyle(ChatFormatting.YELLOW));
            }

            if (material.hasProperty(PropertyKey.INGOT)) {
                if (GTUtil.isShiftDown() && amount >= GTValues.L) {
                    long ingots = amount / GTValues.L;
                    long remainder = amount % GTValues.L;
                    String fluidAmount = String.format(" %,d mB = %,d * %d mB", amount, ingots, GTValues.L);
                    if (remainder != 0) {
                        fluidAmount += String.format(" + %d mB", remainder);
                    }
                    tooltips.accept(Component.translatable("gtpm.gui.fluid_amount").withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(fluidAmount)));
                }
            }
        }

        if (fluid instanceof GTFluid attributedFluid) {
            FluidState state = attributedFluid.getState();
            switch (state) {
                case LIQUID -> tooltips.accept(Component.translatable("gtpm.fluid.state_liquid"));
                case GAS -> tooltips.accept(Component.translatable("gtpm.fluid.state_gas"));
                case PLASMA -> tooltips.accept(Component.translatable("gtpm.fluid.state_plasma"));
            }
            attributedFluid.getAttributes().forEach(a -> a.appendFluidTooltips(tooltips));
        } else {
            String key = "gtpm.fluid.state_" + (fluidType.isLighterThanAir() ? "gas" : "liquid");
            tooltips.accept(Component.translatable(key));
        }

        tooltips.accept(Component.translatable("gtpm.fluid.temperature",
                FormattingUtil.formatTemperature(fluidType.getTemperature())));
        if (fluidType.getTemperature() < FluidConstants.CRYOGENIC_FLUID_THRESHOLD) {
            tooltips.accept(Component.translatable("gtpm.fluid.temperature.cryogenic"));
        }
    }
}

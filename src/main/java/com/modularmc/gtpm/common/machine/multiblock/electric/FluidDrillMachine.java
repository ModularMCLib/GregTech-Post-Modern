package com.modularmc.gtpm.common.machine.multiblock.electric;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.IEnergyContainer;
import com.modularmc.gtpm.api.capability.recipe.EURecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.gui.fancy.TooltipsPanel;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMaintenanceMachine;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMultiPart;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockDisplayText;
import com.modularmc.gtpm.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.modularmc.gtpm.api.misc.EnergyContainerList;
import com.modularmc.gtpm.common.data.GTBlocks;
import com.modularmc.gtpm.common.data.GTMaterialBlocks;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.common.machine.trait.FluidDrillLogic;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@TransVariants(keyPrefix = "gtpm.multiblock.fluid_rig",
               value = {
                       @Variant(id = "drilled_fluid",
                                source = "Fluid: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体：%s")),
                       @Variant(id = "fluid_amount",
                                source = "Pumping Rate: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "抽取速率：%s")),
                       @Variant(id = "no_fluid_in_area",
                                source = "None in Area.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "区域内没有。")),
                       @Variant(id = "vein_depletion",
                                source = "Vein Size: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉大小：%s")),
                       @Variant(id = "vein_depleted",
                                source = "Vein Depleted.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉已枯竭")),
               })
public class FluidDrillMachine extends WorkableElectricMultiblockMachine {

    @Getter
    private final int tier;

    public FluidDrillMachine(BlockEntityCreationInfo info, int tier) {
        super(info, new FluidDrillLogic());
        this.tier = tier;
    }

    @NotNull
    @Override
    public FluidDrillLogic getRecipeLogic() {
        return (FluidDrillLogic) super.getRecipeLogic();
    }

    public int getEnergyTier() {
        var energyContainer = this.getCapabilitiesFlat(IO.IN, EURecipeCapability.CAP);
        var energyCont = new EnergyContainerList(energyContainer.stream().filter(IEnergyContainer.class::isInstance)
                .map(IEnergyContainer.class::cast).toList());

        return Math.min(this.tier + 1,
                Math.max(this.tier, GTUtil.getFloorTierByVoltage(energyCont.getEffectiveVoltage())));
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        boolean formed = isFormed();
        MultiblockDisplayText.builder(textList, formed);
        if (!formed) return;

        int energyTier = getEnergyTier();
        textList.add(Component.translatable("gtpm.multiblock.max_energy_per_tick",
                GTValues.V[energyTier], GTValues.VNF[energyTier]));
        var logic = getRecipeLogic();
        Fluid veinFluid = logic.getVeinFluid();
        Component fluidInfo = veinFluid == null ?
                Component.translatable("gtpm.multiblock.fluid_rig.no_fluid_in_area").withStyle(ChatFormatting.RED) :
                veinFluid.getFluidType().getDescription().copy().withStyle(ChatFormatting.GREEN);
        textList.add(Component.translatable("gtpm.multiblock.fluid_rig.drilled_fluid", fluidInfo)
                .withStyle(ChatFormatting.GRAY));
        if (veinFluid != null) {
            float tickRate = getLevel().tickRateManager().tickrate();
            float fluidPerSecond = Mth.floor(logic.getFluidToProduce() * tickRate / FluidDrillLogic.MAX_PROGRESS);
            Component amountInfo = Component.literal(FormattingUtil.formatNumbers(fluidPerSecond) + " mB/s")
                    .withStyle(ChatFormatting.BLUE);
            textList.add(Component.translatable("gtpm.multiblock.fluid_rig.fluid_amount", amountInfo)
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public static int getDepletionChance(int tier) {
        if (tier == GTValues.MV)
            return 1;
        if (tier == GTValues.HV)
            return 2;
        if (tier == GTValues.EV)
            return 8;
        return 1;
    }

    public static int getRigMultiplier(int tier) {
        if (tier == GTValues.MV)
            return 1;
        if (tier == GTValues.HV)
            return 16;
        if (tier == GTValues.EV)
            return 64;
        return 1;
    }

    public static Block getCasingState(int tier) {
        if (tier == GTValues.MV)
            return GTBlocks.CASING_STEEL_SOLID.get();
        if (tier == GTValues.HV)
            return GTBlocks.CASING_TITANIUM_STABLE.get();
        if (tier == GTValues.EV)
            return GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get();
        return GTBlocks.CASING_STEEL_SOLID.get();
    }

    @SuppressWarnings("DataFlowIssue")
    public static Block getFrameState(int tier) {
        if (tier == GTValues.MV)
            return GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Steel).get();
        if (tier == GTValues.HV)
            return GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Titanium).get();
        if (tier == GTValues.EV)
            return GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.TungstenSteel).get();
        return GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Steel).get();
    }

    public static ResourceLocation getBaseTexture(int tier) {
        if (tier == GTValues.MV)
            return GTPM.id("block/casings/solid/machine_casing_solid_steel");
        if (tier == GTValues.HV)
            return GTPM.id("block/casings/solid/machine_casing_stable_titanium");
        if (tier == GTValues.EV)
            return GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel");
        return GTPM.id("block/casings/solid/machine_casing_solid_steel");
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        for (IMultiPart part : getParts()) {
            if (part instanceof IMaintenanceMachine maintenanceMachine) {
                maintenanceMachine.attachMaintenanceTooltips(tooltipsPanel);
            }
        }
    }
}

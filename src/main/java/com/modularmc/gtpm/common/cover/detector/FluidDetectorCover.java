package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.utils.RedstoneUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

@TransVariants(keyPrefix = "gtpm.cover.fluid_detector",
               value = {
                       @Variant(id = "message_fluid_storage_inverted",
                                source = "Monitoring Inverted Fluid Storage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以反相模式监控流体存储")),
                       @Variant(id = "message_fluid_storage_normal",
                                source = "Monitoring Normal Fluid Storage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以普通模式监控流体存储")),
               })
public class FluidDetectorCover extends DetectorCover {

    public FluidDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && getFluidHandler() != null;
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0)
            return;

        IFluidHandler fluidHandler = getFluidHandler();
        if (fluidHandler == null)
            return;

        int storedFluid = 0;
        int fluidCapacity = 0;

        for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
            FluidStack content = fluidHandler.getFluidInTank(tank);
            if (!content.isEmpty())
                storedFluid += content.getAmount();

            fluidCapacity += fluidHandler.getTankCapacity(tank);
        }

        if (fluidCapacity == 0)
            return;

        setRedstoneSignalOutput(RedstoneUtil.computeRedstoneValue(storedFluid, fluidCapacity, isInverted()));
    }

    protected IFluidHandler getFluidHandler() {
        return FluidUtil.getFluidHandler(coverHolder.getLevel(), coverHolder.getBlockPos(), attachedSide).orElse(null);
    }
}

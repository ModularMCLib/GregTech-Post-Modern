package com.modularmc.gtpm.common.machine.trait;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.capability.IWorkable;
import com.modularmc.gtpm.api.capability.recipe.EURecipeCapability;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMaintenanceMachine;
import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.misc.EnergyContainerList;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.common.capability.EnvironmentalHazardSavedData;
import com.modularmc.gtpm.common.machine.multiblock.electric.CleanroomMachine;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@TransVariants(keyPrefix = "gtpm.recipe_logic",
               value = {
                       @Variant(id = "insufficient_in",
                                source = "Insufficient Inputs",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要输入")),
               })
public class CleanroomLogic extends RecipeLogic implements IWorkable {

    public static final int BASE_CLEAN_AMOUNT = 2;
    @Setter
    @Nullable
    private IMaintenanceMachine maintenanceMachine;
    @Setter
    @Nullable
    private EnergyContainerList energyContainer;
    /**
     * whether the cleanroom was active and needs an update
     */
    @Getter
    @Setter
    @SaveField
    private boolean isActiveAndNeedsUpdate;

    public CleanroomLogic() {
        super();
    }

    @Override
    public CleanroomMachine getMachine() {
        return (CleanroomMachine) super.getMachine();
    }

    @Override
    protected List<Class<?>> validMachineClasses() {
        return List.of(CleanroomMachine.class);
    }

    /**
     * Performs the actual cleaning
     * Call this method every tick in update
     */
    public void serverTick() {
        // always run this logic
        if (duration > 0) {
            EnvironmentalHazardSavedData environmentalHazards = EnvironmentalHazardSavedData
                    .getOrCreate((ServerLevel) this.getMachine().getLevel());
            var zone = environmentalHazards.getZoneByContainedPos(getMachine().getBlockPos());
            // all maintenance problems not being fixed or there are environmental hazards in the area
            // means the machine does not run
            if (maintenanceMachine == null || maintenanceMachine.getNumMaintenanceProblems() < 6 || zone != null) {
                // drain the energy
                if (!consumeEnergy()) {
                    if (progress > 0 && getMachine().regressWhenWaiting()) {
                        this.progress = 1;
                    }

                    // the cleanroom does not have enough energy, so it looses cleanliness
                    if (getMachine().getOffsetTimer() % duration == 0) {
                        adjustCleanAmount(true);
                    }

                    setWaiting(Component.translatable("gtpm.recipe_logic.insufficient_in").append(": ")
                            .append(EURecipeCapability.CAP.getName()));
                    return;
                }
                setStatus(Status.WORKING);
                // increase progress
                if (progress++ < getMaxProgress()) {
                    if (!getMachine().onWorking()) {
                        this.interruptRecipe();
                    }
                    return;
                }
                progress = 0;
                Component failReason = getMachine().beforeWorking(null);
                if (failReason != null) {
                    setWaiting(failReason);
                    return;
                }
                adjustCleanAmount(false);
            } else {
                // has all maintenance problems
                if (progress > 0) {
                    progress--;
                }
                if (getMachine().getOffsetTimer() % duration == 0) {
                    adjustCleanAmount(true);
                }
                setStatus(Status.IDLE);
                getMachine().afterWorking();
            }
        }
    }

    protected void adjustCleanAmount(boolean declined) {
        // range from 5 - ~44 % per cycle instead of the 5 - 70% it was previously
        int amountToClean = BASE_CLEAN_AMOUNT + (3 * (getTierDifference() + 1));
        if (declined) amountToClean *= -1;

        // each maintenance problem lowers gain by 1
        if (maintenanceMachine != null) {
            amountToClean -= maintenanceMachine.getNumMaintenanceProblems();
        }
        getMachine().adjustCleanAmount(amountToClean);
    }

    protected boolean consumeEnergy() {
        var cleanroomTrait = getMachine().getTrait(CleanroomProviderTrait.TYPE);
        if (cleanroomTrait == null) return false;
        // clamp to max for VA indexing
        var tier = Mth.clamp(getMachine().getTier(), GTValues.ULV, GTValues.MAX);
        // use 3/16th an amp when fully clean otherwise 15/16th an amp during cleaning
        long energyToDrain = cleanroomTrait.isActive() ? Math.max(8, (3 * GTValues.V[tier] / 16)) :
                GTValues.VA[tier];
        if (energyContainer != null) {
            long resultEnergy = energyContainer.getEnergyStored() - energyToDrain;
            if (resultEnergy >= 0L && resultEnergy <= energyContainer.getEnergyCapacity()) {
                energyContainer.removeEnergy(energyToDrain);
                return true;
            }
        }
        return false;
    }

    protected int getTierDifference() {
        int minEnergyTier = GTValues.LV;
        // clamp for ULV
        return Math.max(0, getMachine().getTier() - minEnergyTier);
    }

    public void setDuration(int max) {
        this.duration = max;
    }
}

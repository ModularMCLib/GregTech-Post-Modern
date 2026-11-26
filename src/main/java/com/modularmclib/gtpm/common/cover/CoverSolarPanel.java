package com.modularmclib.gtpm.common.cover;

import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.capability.GTCapabilityHelper;
import com.modularmclib.gtpm.api.capability.ICoverable;
import com.modularmclib.gtpm.api.capability.IEnergyContainer;
import com.modularmclib.gtpm.api.cover.CoverBehavior;
import com.modularmclib.gtpm.api.cover.CoverDefinition;
import com.modularmclib.gtpm.api.machine.TickableSubscription;
import com.modularmclib.gtpm.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public class CoverSolarPanel extends CoverBehavior {

    private final long EUt;
    protected TickableSubscription subscription;

    public CoverSolarPanel(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        this.EUt = 1;
    }

    public CoverSolarPanel(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier) {
        super(definition, coverHolder, attachedSide);
        this.EUt = GTValues.V[tier];
    }

    @Override
    public void onLoad() {
        super.onLoad();
        subscription = coverHolder.subscribeServerTick(subscription, this::update);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && attachedSide == Direction.UP && getEnergyContainer() != null;
    }

    protected void update() {
        Level level = coverHolder.getLevel();
        BlockPos blockPos = coverHolder.getBlockPos();
        if (GTUtil.canSeeSunClearly(level, blockPos)) {
            IEnergyContainer energyContainer = getEnergyContainer();
            if (energyContainer != null) {
                energyContainer.acceptEnergyFromNetwork(null, EUt, 1);
            }
        }
    }

    @Nullable
    protected IEnergyContainer getEnergyContainer() {
        return GTCapabilityHelper.getEnergyContainer(coverHolder.getLevel(), coverHolder.getBlockPos(), attachedSide);
    }
}

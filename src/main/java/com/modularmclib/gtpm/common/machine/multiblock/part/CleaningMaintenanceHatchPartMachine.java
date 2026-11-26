package com.modularmclib.gtpm.common.machine.multiblock.part;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.machine.multiblock.CleanroomType;
import com.modularmclib.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmclib.gtpm.common.machine.trait.CleanroomProviderTrait;
import com.modularmclib.gtpm.common.machine.trait.CleanroomReceiverTrait;

import lombok.Getter;

import java.util.Set;

import static com.modularmclib.gtpm.api.GTValues.UHV;
import static com.modularmclib.gtpm.api.GTValues.UV;

public class CleaningMaintenanceHatchPartMachine extends AutoMaintenanceHatchPartMachine {

    private final CleanroomProviderTrait cleanroomProvider;

    @Getter
    private final CleanroomType cleanroomType;

    public CleaningMaintenanceHatchPartMachine(BlockEntityCreationInfo info, CleanroomType cleanroomType) {
        super(info);
        this.cleanroomType = cleanroomType;
        this.cleanroomProvider = attachTrait(new CleanroomProviderTrait(Set.of(cleanroomType)));
        cleanroomProvider.setActive(true);
    }

    @Override
    public void addedToController(MultiblockControllerMachine controller, String structureName) {
        super.addedToController(controller, structureName);
        controller.self().getTraitOptional(CleanroomReceiverTrait.TYPE)
                .ifPresent(t -> t.setCleanroomProvider(cleanroomProvider));
    }

    @Override
    public void removedFromController(MultiblockControllerMachine controller, String structureName) {
        super.removedFromController(controller, structureName);
        controller.self().getTraitOptional(CleanroomReceiverTrait.TYPE)
                .ifPresent(CleanroomReceiverTrait::removeCleanroom);
    }

    @Override
    public int getTier() {
        return cleanroomType == CleanroomType.CLEANROOM ? UV : UHV;
    }
}

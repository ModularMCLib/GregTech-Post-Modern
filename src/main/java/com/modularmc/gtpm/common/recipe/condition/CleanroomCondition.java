package com.modularmc.gtpm.common.recipe.condition;

import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.multiblock.CleanroomType;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.data.GTRecipeConditions;
import com.modularmc.gtpm.common.machine.trait.CleanroomReceiverTrait;
import com.modularmc.gtpm.config.GTPMConfigHolder;

import net.minecraft.network.chat.Component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class CleanroomCondition extends RecipeCondition<CleanroomCondition> {

    // spotless:off
    public static final MapCodec<CleanroomCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> RecipeCondition.isReverse(instance).and(
            CleanroomType.CODEC.fieldOf("cleanroom").forGetter(val -> val.cleanroom)
    ).apply(instance, CleanroomCondition::new));
    // spotless:on

    @Getter
    private CleanroomType cleanroom = CleanroomType.CLEANROOM;

    public CleanroomCondition(boolean isReverse, CleanroomType cleanroom) {
        super(isReverse);
        this.cleanroom = cleanroom;
    }

    @Override
    public RecipeConditionType<CleanroomCondition> getType() {
        return GTRecipeConditions.CLEANROOM;
    }

    @Override
    public Component getTooltips() {
        return cleanroom == null ? null :
                Component.translatable("gtpm.recipe.cleanroom", Component.translatable(cleanroom.translationKey()));
    }

    @Override
    public boolean perTick() {
        return true;
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();

        if (!GTPMConfigHolder.INSTANCE.machines.enableCleanroom) return true;
        if (GTPMConfigHolder.INSTANCE.machines.cleanMultiblocks && machine instanceof MultiblockControllerMachine)
            return true;

        CleanroomReceiverTrait receiverTrait = machine.getTrait(CleanroomReceiverTrait.TYPE);

        if (receiverTrait != null && this.cleanroom != null) return receiverTrait.hasActiveCleanroom(cleanroom);
        return true;
    }

    @Override
    public CleanroomCondition createTemplate() {
        return new CleanroomCondition();
    }
}

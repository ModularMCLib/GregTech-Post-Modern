package com.modularmclib.gtpm.common.recipe.condition;

import com.modularmclib.gtpm.api.machine.MetaMachine;
import com.modularmclib.gtpm.api.machine.multiblock.CleanroomType;
import com.modularmclib.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmclib.gtpm.api.machine.trait.RecipeLogic;
import com.modularmclib.gtpm.api.recipe.GTRecipe;
import com.modularmclib.gtpm.api.recipe.RecipeCondition;
import com.modularmclib.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmclib.gtpm.common.data.GTRecipeConditions;
import com.modularmclib.gtpm.common.machine.trait.CleanroomReceiverTrait;
import com.modularmclib.gtpm.config.ConfigHolder;

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

        if (!ConfigHolder.INSTANCE.machines.enableCleanroom) return true;
        if (ConfigHolder.INSTANCE.machines.cleanMultiblocks && machine instanceof MultiblockControllerMachine)
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

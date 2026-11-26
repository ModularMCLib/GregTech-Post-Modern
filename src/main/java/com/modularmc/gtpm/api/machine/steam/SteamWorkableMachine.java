package com.modularmc.gtpm.api.machine.steam;

import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.capability.recipe.IRecipeHandler;
import com.modularmc.gtpm.api.capability.recipe.RecipeCapability;
import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.api.item.tool.GridHighlightTexture;
import com.modularmc.gtpm.api.machine.feature.IMufflableMachine;
import com.modularmc.gtpm.api.machine.feature.IRecipeLogicMachine;
import com.modularmc.gtpm.api.machine.trait.*;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.recipe.handler.RecipeHandlerList;
import com.modularmc.gtpm.api.sync_system.annotations.RerenderOnChanged;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.common.data.item.GTItemAbilities;
import com.modularmc.gtpm.common.machine.trait.CleanroomReceiverTrait;
import com.modularmc.gtpm.utils.ExtendedUseOnContext;
import com.modularmc.gtpm.utils.ISubscription;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class SteamWorkableMachine extends SteamMachine
                                           implements IRecipeLogicMachine, IMufflableMachine {

    @Getter
    protected final CleanroomReceiverTrait cleanroomReceiver;
    @Getter
    @SaveField
    @SyncToClient
    public final RecipeLogic recipeLogic;
    @Getter
    public final GTRecipeType[] recipeTypes;
    @Getter
    @Setter
    public int activeRecipeType;
    @SaveField
    @SyncToClient
    @RerenderOnChanged
    protected Direction outputFacing;
    @SaveField
    @SyncToClient
    @Getter
    @Setter
    protected boolean isMuffled;
    protected boolean previouslyMuffled = true;
    @Getter
    protected final Map<IO, List<RecipeHandlerList>> capabilitiesProxy;
    @Getter
    protected final Map<IO, Map<RecipeCapability<?>, List<IRecipeHandler<?>>>> capabilitiesFlat;
    protected final List<ISubscription> traitSubscriptions;

    public SteamWorkableMachine(BlockEntityCreationInfo info, boolean isHighPressure,
                                RecipeLogic recipeLogic,
                                NotifiableFluidTank steamTank) {
        super(info, isHighPressure, steamTank);
        this.recipeTypes = getDefinition().getRecipeTypes();
        this.activeRecipeType = 0;
        this.cleanroomReceiver = attachTrait(new CleanroomReceiverTrait());
        this.recipeLogic = attachTrait(recipeLogic);
        this.capabilitiesProxy = new EnumMap<>(IO.class);
        this.capabilitiesFlat = new EnumMap<>(IO.class);
        this.traitSubscriptions = new ArrayList<>();
        this.outputFacing = hasFrontFacing() ? getFrontFacing().getOpposite() : Direction.UP;
    }

    public SteamWorkableMachine(BlockEntityCreationInfo info, boolean isHighPressure,
                                RecipeLogic recipeLogic) {
        this(info, isHighPressure, recipeLogic,
                new NotifiableFluidTank(1, 16 * FluidType.BUCKET_VOLUME, IO.IN));
    }

    public SteamWorkableMachine(BlockEntityCreationInfo info, boolean isHighPressure) {
        this(info, isHighPressure, new RecipeLogic());
    }

    //////////////////////////////////////
    // ***** Initialization *****//
    //////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        // attach self traits
        Map<IO, List<IRecipeHandler<?>>> ioTraits = new Object2ObjectOpenHashMap<>();

        for (MachineTrait trait : getAllTraits()) {
            if (trait instanceof IRecipeHandlerTrait<?> handlerTrait) {
                ioTraits.computeIfAbsent(handlerTrait.getHandlerIO(), i -> new ArrayList<>()).add(handlerTrait);
            }
        }

        for (var entry : ioTraits.entrySet()) {
            var handlerList = RecipeHandlerList.of(entry.getKey(), entry.getValue());
            this.addHandlerList(handlerList);
            traitSubscriptions.add(handlerList.subscribe(recipeLogic::onRecipeHandlerChanged));
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        traitSubscriptions.forEach(ISubscription::unsubscribe);
        traitSubscriptions.clear();
        capabilitiesProxy.clear();
        capabilitiesFlat.clear();
    }

    public boolean hasOutputFacing() {
        return true;
    }

    /**
     * @param outputFacing the facing to set
     */
    public void setOutputFacing(Direction outputFacing) {
        if (hasOutputFacing() && (!hasFrontFacing() || this.outputFacing != getFrontFacing())) {
            this.outputFacing = outputFacing;
        }
    }

    public @Nullable Direction getOutputFacing() {
        if (hasOutputFacing()) {
            return outputFacing;
        }
        return null;
    }

    @Override
    public boolean isFacingValid(Direction facing) {
        if (facing == getOutputFacing()) {
            return false;
        }
        return super.isFacingValid(facing);
    }

    @Override
    protected InteractionResult onWrenchClick(ExtendedUseOnContext context) {
        var gridSide = context.getGridSide();
        var player = context.getPlayer();
        if (!context.getItemInHand().canPerformAction(GTItemAbilities.WRENCH_CONFIGURE)) {
            return super.onWrenchClick(context);
        }
        if (!player.isShiftKeyDown()) {
            if (hasFrontFacing() && gridSide == getFrontFacing()) return InteractionResult.PASS;
            setOutputFacing(gridSide);
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return super.onWrenchClick(context);
    }

    @Override
    public boolean keepSubscribing() {
        return false;
    }

    @Override
    public GTRecipeType getRecipeType() {
        return recipeTypes[activeRecipeType];
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (previouslyMuffled != isMuffled) {
            previouslyMuffled = isMuffled;

            recipeLogic.updateSound();
        }
    }

    //////////////////////////////////////
    // ******* Rendering ********//
    //////////////////////////////////////
    @Override
    public GridHighlightTexture sideTips(Player player, BlockPos pos, BlockState state, Set<GTToolType> toolTypes,
                                         ItemStack held, Direction side) {
        if (toolTypes.contains(GTToolType.WRENCH)) {
            if (!player.isShiftKeyDown()) {
                if (!hasFrontFacing() || side != getFrontFacing()) {
                    return GridHighlightTexture.TOOL_IO_FACING_ROTATION;
                }
            }
        }
        return super.sideTips(player, pos, state, toolTypes, held, side);
    }
}

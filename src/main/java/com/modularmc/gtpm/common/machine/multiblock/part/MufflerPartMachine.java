package com.modularmc.gtpm.common.machine.multiblock.part;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IHazardParticleContainer;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTItemSlotElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.factory.MachineUIHolder;
import com.modularmc.gtpm.api.machine.TickableSubscription;
import com.modularmc.gtpm.api.machine.feature.IWorkLogicMachine;
import com.modularmc.gtpm.api.machine.feature.multiblock.IWorkableMultiController;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.part.TieredPartMachine;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.transfer.item.CustomItemStackHandler;
import com.modularmc.gtpm.common.data.GTMedicalConditions;
import com.modularmc.gtpm.common.data.GTParticleTypes;
import com.modularmc.gtpm.common.machine.trait.hazard.EnvironmentalHazardEmitterTrait;
import com.modularmc.gtpm.utils.GTUtil;

import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.data.Vertical;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import lombok.Getter;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MufflerPartMachine extends TieredPartMachine {

    @Getter
    private final int recoveryChance;
    @Getter
    @SaveField
    private final CustomItemStackHandler inventory;

    private TickableSubscription snowSubscription;
    @Getter
    private final EnvironmentalHazardEmitterTrait hazardEmitter;

    public MufflerPartMachine(BlockEntityCreationInfo info, int tier) {
        super(info, tier);
        this.recoveryChance = Math.max(1, tier * 10);
        this.inventory = new CustomItemStackHandler((int) Math.pow(tier + 1, 2));
        this.hazardEmitter = attachTrait(
                new EnvironmentalHazardEmitterTrait(GTMedicalConditions.CARBON_MONOXIDE_POISONING,
                        2.5f / Math.max(tier, 1)));
    }

    //////////////////////////////////////
    // ******** Muffler *********//
    //////////////////////////////////////

    public void recoverItemsTable(ItemStack... recoveryItems) {
        int numRolls = Math.min(recoveryItems.length, inventory.getSlots());
        IntStream.range(0, numRolls).forEach(slot -> {
            if (calculateChance()) {
                ItemHandlerHelper.insertItemStacked(inventory, recoveryItems[slot].copy(), false);
            }
        });
    }

    private boolean calculateChance() {
        return recoveryChance >= 100 || recoveryChance >= GTValues.RNG.nextInt(100);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientTick() {
        super.clientTick();
        for (MultiblockControllerMachine controller : getControllers()) {
            if (controller instanceof IWorkLogicMachine workLogicMachine && workLogicMachine.getWorkLogic()
                    .isWorking()) {
                emitPollutionParticles();
                break;
            }
        }
    }

    @Override
    public @Nullable GTRecipe modifyRecipe(GTRecipe recipe) {
        return isFrontFaceFree() ? recipe : super.modifyRecipe(recipe);
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        hazardEmitter.emitHazard();
        var supplier = controller.self().getDefinition().getRecoveryItems();
        if (supplier != null) {
            recoverItemsTable(supplier.get());
        }
        return super.afterWorking(controller);
    }

    @Override
    public void addedToController(MultiblockControllerMachine controller, String structureName) {
        super.addedToController(controller, structureName);
        if (snowSubscription == null) {
            this.snowSubscription = subscribeServerTick(null, this::tryBreakSnow);
        }
    }

    @MustBeInvokedByOverriders
    @Override
    public void removedFromController(MultiblockControllerMachine controller, String structureName) {
        super.removedFromController(controller, structureName);
        if (controllers.isEmpty()) {
            unsubscribe(snowSubscription);
            snowSubscription = null;
        }
    }

    private void tryBreakSnow() {
        if (getOffsetTimer() % 10 == 0) {
            for (MultiblockControllerMachine controller : getControllers()) {
                if (controller instanceof IWorkLogicMachine workLogicMachine && workLogicMachine.getWorkLogic()
                        .isWorking()) {
                    BlockPos mufflerPos = getBlockPos().relative(getFrontFacing());
                    GTUtil.tryBreakSnow(getLevel(), mufflerPos, getLevel().getBlockState(mufflerPos), true);
                }
            }
        }
    }

    public boolean isFrontFaceFree() {
        var frontPos = self().getBlockPos().relative(self().getFrontFacing());
        return self().getLevel().getBlockState(frontPos).isAir() ||
                GTCapabilityHelper.getHazardContainer(self().getLevel(),
                        frontPos, self().getFrontFacing().getOpposite()) != null;
    }

    public void emitPollutionParticles() {
        var pos = self().getBlockPos();
        var facing = self().getFrontFacing();

        IHazardParticleContainer container = GTCapabilityHelper.getHazardContainer(self().getLevel(),
                pos.relative(facing), facing.getOpposite());
        if (container != null) {
            // do not emit particles if front face has a duct on it.
            return;
        }

        var center = pos.getCenter();
        var offset = .75f;
        var xPos = (float) (center.x + facing.getStepX() * offset + (GTValues.RNG.nextFloat() - .5f) * .35f);
        var yPos = (float) (center.y + facing.getStepY() * offset + (GTValues.RNG.nextFloat() - .5f) * .35f);
        var zPos = (float) (center.z + facing.getStepZ() * offset + (GTValues.RNG.nextFloat() - .5f) * .35f);

        var ySpd = facing.getStepY() + (GTValues.RNG.nextFloat() - .15f) * .5f;
        var xSpd = facing.getStepX() + (GTValues.RNG.nextFloat() - .5f) * .5f;
        var zSpd = facing.getStepZ() + (GTValues.RNG.nextFloat() - .5f) * .5f;

        self().getLevel().addParticle(GTParticleTypes.MUFFLER_PARTICLE.get(),
                xPos, yPos, zPos, xSpd, ySpd, zSpd);
    }

    //////////////////////////////////////
    // ********** GUI ***********//
    //////////////////////////////////////

    @Override
    public UI createUI(Player player, MachineUIHolder holder) {
        int rowSize = (int) Math.sqrt(inventory.getSlots());
        int xOffset = rowSize == 10 ? 9 : 0;
        int rootWidth = 176 + xOffset * 2;
        int rootHeight = 18 + 18 * rowSize + 94;

        UIElement root = new UIElement();
        UITemplate.setBounds(root, 0, 0, rootWidth, rootHeight);
        root.style(style -> style.backgroundTexture(GuiTextures.BACKGROUND));
        root.addChild(createTitleLabel(rootWidth));
        root.addChild(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.SLOT, 7 + xOffset,
                18 + 18 * rowSize + 12, true));

        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int index = y * rowSize + x;
                root.addChild(createMufflerSlot(index,
                        (88 - rowSize * 9 + x * 18) + xOffset, 18 + y * 18));
            }
        }
        return UI.of(root);
    }

    /** Creates the holder-scoped default preview used by a surrounding multiblock controller. */

    private GTLabelElement createTitleLabel(int rootWidth) {
        GTLabelElement label = new GTLabelElement(10, 5, rootWidth - 20, 10,
                getBlockState().getBlock().getDescriptionId(), true);
        label.textStyle(style -> style
                .textColor(0x404040)
                .textShadow(false)
                .textAlignHorizontal(Horizontal.LEFT)
                .textAlignVertical(Vertical.CENTER));
        return label;
    }

    private GTItemSlotElement createMufflerSlot(int index, int x, int y) {
        GTItemSlotElement slot = new GTItemSlotElement(inventory, index)
                .setBackgroundTexture(GuiTextures.SLOT)
                .setCanTakeItems(true)
                .setCanPutItems(false);
        UITemplate.setBounds(slot, x, y, 18, 18);
        return slot;
    }
}

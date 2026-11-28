package com.modularmc.gtpm.common.machine.multiblock.primitive;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTFluidSlotElement;
import com.modularmc.gtpm.api.gui.element.GTItemSlotElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.element.GTProgressBarElement;
import com.modularmc.gtpm.api.gui.factory.MachineUIHolder;
import com.modularmc.gtpm.api.machine.feature.IUIMachine;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.utils.ExtendedUseOnContext;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.data.Vertical;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.FluidUtil;

import java.util.List;

@TransVariants(keyPrefix = "gtpm.fluid",
               value = {
                       @Variant(id = "amount",
                                source = "§9Amount: %d/%d mB",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9总量：%d/%d mB")),
               })
public class CokeOvenMachine extends PrimitiveWorkableMachine implements IUIMachine {

    public CokeOvenMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    @Override
    public UI createUI(Player player, MachineUIHolder holder) {
        UIElement root = new UIElement();
        UITemplate.setBounds(root, 0, 0, 176, 166);
        root.style(style -> style.backgroundTexture(GuiTextures.PRIMITIVE_BACKGROUND));
        root.addChild(createTitleLabel());
        root.addChild(createInputSlot());
        root.addChild(createProgressBar());
        root.addChild(createOutputSlot());
        root.addChild(createFluidTank());
        root.addChild(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.PRIMITIVE_SLOT, 7, 84,
                true));
        return UI.of(root);
    }

    private GTLabelElement createTitleLabel() {
        GTLabelElement label = new GTLabelElement(5, 5, 166, 10,
                getBlockState().getBlock().getDescriptionId(), true);
        label.textStyle(style -> style
                .textColor(0x404040)
                .textShadow(false)
                .textAlignHorizontal(Horizontal.LEFT)
                .textAlignVertical(Vertical.CENTER));
        return label;
    }

    private GTItemSlotElement createInputSlot() {
        GTItemSlotElement slot = new GTItemSlotElement(importItems.storage, 0)
                .setBackgroundTexture(GuiTextures.group(GuiTextures.PRIMITIVE_SLOT,
                        GuiTextures.PRIMITIVE_FURNACE_OVERLAY))
                .setCanTakeItems(true)
                .setCanPutItems(true);
        return UITemplate.setBounds(slot, 52, 30, 18, 18);
    }

    private GTProgressBarElement createProgressBar() {
        GTProgressBarElement progress = new GTProgressBarElement(recipeLogic::getProgressPercent)
                .setProgressTexture(
                        GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR.getSubTexture(0, 0, 1, 0.5),
                        GuiTextures.PRIMITIVE_BLAST_FURNACE_PROGRESS_BAR.getSubTexture(0, 0.5, 1, 0.5));
        return UITemplate.setBounds(progress, 76, 32, 20, 15);
    }

    private GTItemSlotElement createOutputSlot() {
        GTItemSlotElement slot = new GTItemSlotElement(exportItems.storage, 0)
                .setBackgroundTexture(GuiTextures.group(GuiTextures.PRIMITIVE_SLOT,
                        GuiTextures.PRIMITIVE_FURNACE_OVERLAY))
                .setCanTakeItems(true)
                .setCanPutItems(false);
        return UITemplate.setBounds(slot, 103, 30, 18, 18);
    }

    private GTFluidSlotElement createFluidTank() {
        GTFluidSlotElement tank = new GTFluidSlotElement()
                .setFluidTank(exportFluids, 0)
                .setBackgroundTexture(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK)
                .setShowAmount(false)
                .setAllowClickFilled(false)
                .setAllowClickDrained(false)
                .setOnAddedTooltips(this::addFluidAmountTooltip)
                .setContentOverlay(GuiTextures.PRIMITIVE_LARGE_FLUID_TANK_OVERLAY);
        return UITemplate.setBounds(tank, 134, 13, 20, 58);
    }

    private void addFluidAmountTooltip(GTFluidSlotElement tank, List<Component> tooltips) {
        int fluidAmount = tank.getFluid().getAmount();
        int capacity = Math.max(tank.getCapacity(), fluidAmount);
        tooltips.add(Component.translatable("gtpm.fluid.amount",
                FormattingUtil.formatNumbers(fluidAmount),
                FormattingUtil.formatNumbers(capacity)));
    }

    @Override
    public void animateTick(RandomSource random) {
        if (this.isActive()) {
            final BlockPos pos = getBlockPos();
            float x = pos.getX() + 0.5F;
            float z = pos.getZ() + 0.5F;

            final var facing = getFrontFacing();
            final float horizontalOffset = GTValues.RNG.nextFloat() * 0.6F - 0.3F;
            final float y = pos.getY() + GTValues.RNG.nextFloat() * 0.375F + 0.3F;

            if (facing.getAxis() == Direction.Axis.X) {
                if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) x += 0.52F;
                else x -= 0.52F;
                z += horizontalOffset;
            } else if (facing.getAxis() == Direction.Axis.Z) {
                if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) z += 0.52F;
                else z -= 0.52F;
                x += horizontalOffset;
            }
            if (ConfigHolder.INSTANCE.machines.machineSounds && GTValues.RNG.nextDouble() < 0.1) {
                getLevel().playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F,
                        false);
            }
            getLevel().addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0, 0, 0);
            getLevel().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
        }
    }

    @Override
    public InteractionResult onUseWithItem(ExtendedUseOnContext context) {
        if (!isRemote()) {
            if (super.onUseWithItem(context) == InteractionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }
            if (FluidUtil.interactWithFluidHandler(context.getPlayer(), context.getHand(), exportFluids)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        return super.onUseWithItem(context);
    }
}

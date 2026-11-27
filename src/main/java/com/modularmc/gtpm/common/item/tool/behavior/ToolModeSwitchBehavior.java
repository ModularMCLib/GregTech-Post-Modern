package com.modularmc.gtpm.common.item.tool.behavior;

import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.api.item.tool.ToolHelper;
import com.modularmc.gtpm.api.item.tool.behavior.IToolBehavior;
import com.modularmc.gtpm.api.item.tool.behavior.ToolBehaviorType;
import com.modularmc.gtpm.api.sync_system.FieldCodecs;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.data.GTToolBehaviors;
import com.modularmc.gtpm.common.data.item.GTItemAbilities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbility;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolModeSwitchBehavior implements IToolBehavior<ToolModeSwitchBehavior> {

    public static final ToolModeSwitchBehavior INSTANCE = new ToolModeSwitchBehavior();

    public static final Codec<ToolModeSwitchBehavior> CODEC = Codec.unit(INSTANCE);
    public static final StreamCodec<ByteBuf, ToolModeSwitchBehavior> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    static {
        FieldCodecs.register(ToolModeSwitchBehavior.class, CODEC);
        FieldCodecs.registerStream(ToolModeSwitchBehavior.class, STREAM_CODEC);
    }

    protected ToolModeSwitchBehavior() {}

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility action) {
        var mode = stack.getOrDefault(GTDataComponents.TOOL_MODE, ModeType.BOTH);
        boolean canWrenchConfigureAll = action == GTItemAbilities.WRENCH_CONFIGURE_ALL;
        return action == GTItemAbilities.WRENCH_CONFIGURE || switch (mode) {
            case ITEM -> canWrenchConfigureAll || action == GTItemAbilities.WRENCH_CONFIGURE_ITEMS;
            case FLUID -> canWrenchConfigureAll || action == GTItemAbilities.WRENCH_CONFIGURE_FLUIDS;
            case BOTH -> GTItemAbilities.WRENCH_CONFIGURE_ACTIONS.contains(action);
        };
    }

    @Override
    public @NotNull InteractionResult onItemUse(UseOnContext context) {
        Level world = context.getLevel();
        BlockHitResult blockHitResult = context.getHitResult();
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        BlockPos pos = blockHitResult.getBlockPos();

        if (player != null)
            world.getBlockState(pos).useItemOn(itemStack, world, player, context.getHand(), blockHitResult);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> onItemRightClick(@NotNull Level world, @NotNull Player player,
                                                                        @NotNull InteractionHand hand) {
        var itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            var toolTypes = ToolHelper.getToolTypes(itemStack);
            if (toolTypes.contains(GTToolType.WRENCH)) {
                var newMode = itemStack.getOrDefault(GTDataComponents.TOOL_MODE, ModeType.BOTH).nextMode();
                itemStack.set(GTDataComponents.TOOL_MODE, newMode);

                player.displayClientMessage(
                        Component.translatable("metaitem.machine_configuration.mode", newMode.getName()),
                        true);
            }
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public ToolBehaviorType<ToolModeSwitchBehavior> getType() {
        return GTToolBehaviors.MODE_SWITCH;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, Item.TooltipContext context, @NotNull List<Component> tooltip,
                               @NotNull TooltipFlag flag) {
        ModeType behavior = stack.getOrDefault(GTDataComponents.TOOL_MODE, ModeType.BOTH);
        tooltip.add(Component.translatable("metaitem.machine_configuration.mode", behavior.getName()));
    }

    public enum ModeType implements StringRepresentable {

        ITEM("item", Component.translatable("gtpm.mode.item")),
        FLUID("fluid", Component.translatable("gtpm.mode.fluid")),
        BOTH("both", Component.translatable("gtpm.mode.both"));

        public static final Codec<ModeType> CODEC = StringRepresentable.fromEnum(ModeType::values);
        public static final StreamCodec<ByteBuf, ModeType> STREAM_CODEC = ByteBufCodecs.BYTE
                .map(aByte -> ModeType.values()[aByte], val -> (byte) val.ordinal());

        static {
            FieldCodecs.register(ModeType.class, CODEC);
            FieldCodecs.registerStream(ModeType.class, STREAM_CODEC);
        }

        @Getter
        private final @NotNull String serializedName;
        @Getter
        private final Component name;

        ModeType(String id, Component name) {
            this.serializedName = id;
            this.name = name;
        }

        public ModeType nextMode() {
            return switch (this) {
                case ITEM -> FLUID;
                case FLUID -> BOTH;
                case BOTH -> ITEM;
            };
        }
    }
}

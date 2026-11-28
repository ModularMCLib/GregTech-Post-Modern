package com.modularmc.gtpm.common.item.behavior;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTButtonElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.element.GTScrollerViewElement;
import com.modularmc.gtpm.api.gui.element.GTTextFieldElement;
import com.modularmc.gtpm.api.gui.factory.HeldItemUIHelper;
import com.modularmc.gtpm.api.gui.factory.HeldItemUIHolder;
import com.modularmc.gtpm.api.gui.texture.ColorRectTexture;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.api.gui.widget.BlockChoiceList;
import com.modularmc.gtpm.api.item.component.IItemUIFactory;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialRequest;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialSources;
import com.modularmc.gtpm.api.multiblock.autobuild.batch.BatchRequest;
import com.modularmc.gtpm.api.multiblock.autobuild.batch.BatchStatus;
import com.modularmc.gtpm.api.multiblock.autobuild.batch.StructureOperation;
import com.modularmc.gtpm.api.multiblock.autobuild.batch.StructureRequest;
import com.modularmc.gtpm.api.multiblock.autobuild.plan.BatchPlanner;
import com.modularmc.gtpm.api.multiblock.pattern.layout.PatternAxis;
import com.modularmc.gtpm.api.multiblock.pattern.match.PatternSelections;
import com.modularmc.gtpm.api.multiblock.preview.PreviewVisibility;
import com.modularmc.gtpm.api.multiblock.preview.StructurePreview;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sync_system.annotations.ClientFieldChangeListener;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.ServerFieldChangeListener;
import com.modularmc.gtpm.api.sync_system.annotations.SyncBoth;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.sync_system.managed.ItemSyncHolder;
import com.modularmc.gtpm.api.sync_system.managed.SyncItem;
import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;
import com.modularmc.gtpm.api.sync_system.rpc.RPCSender;
import com.modularmc.gtpm.client.gui.ClientMultiblockPreviewElement;
import com.modularmc.gtpm.client.renderer.MultiblockInWorldPreviewRenderer;
import com.modularmc.gtpm.common.machine.owner.MachineOwner;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.data.pattern.StructurePatternRegistry;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerScreen;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.ScrollDisplay;
import com.lowdragmc.lowdraglib2.gui.ui.data.ScrollerMode;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.math.Size;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Locale;

@TransVariants(keyPrefix = "gtpm.multiblock.autobuild",
               value = {
                       @Variant(id = "commit_material_failed",
                                source = "Could not commit material for %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无法提交位置 %s 的材料。")),
                       @Variant(id = "count_overflow",
                                source = "Automatic-build material counts exceeded the supported range.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自动搭建材料数量超出支持范围。")),
                       @Variant(id = "failed",
                                source = "Auto-build failed.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自动搭建失败。")),
                       @Variant(id = "invalid_repeat",
                                source = "Invalid repeat count: %d.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无效重复次数：%d。")),
                       @Variant(id = "invalid_target_level",
                                source = "The player and multiblock controller are not in the same server level.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "玩家与多方块主机不在同一个服务端维度。")),
                       @Variant(id = "invalid_tier",
                                source = "Invalid tier %2$d for category %1$s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "类别 %1$s 的等级 %2$d 无效。")),
                       @Variant(id = "missing_material",
                                source = "Missing material for %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "位置 %s 缺少材料。")),
                       @Variant(id = "no_candidate",
                                source = "No placeable candidate for %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "位置 %s 没有可放置候选。")),
                       @Variant(id = "pattern_unavailable",
                                source = "Multiblock structure pattern is not available: %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "多方块结构样式不可用：%s。")),
                       @Variant(id = "player_inventory_unavailable",
                                source = "The player's inventory is unavailable.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "玩家物品栏不可用。")),
                       @Variant(id = "refund_failed",
                                source = "Committed automatic-build material could not be returned to its source.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "已提交的自动搭建材料无法返还原来源。")),
                       @Variant(id = "status.failed",
                                source = "Failed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "失败")),
                       @Variant(id = "status.partial_success",
                                source = "Partially successful",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "部分成功")),
                       @Variant(id = "status.success",
                                source = "Successful",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "成功")),
                       @Variant(id = "success",
                                source = "Auto-build completed: placed %d, removed %d, stages %d.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "自动搭建完成：放置 %d，拆除 %d，阶段 %d。")),
                       @Variant(id = "terminal.access_denied",
                                source = "You do not have permission to use this controller.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "你没有权限操作这台主机。")),
                       @Variant(id = "terminal.controller_missing",
                                source = "The bound controller is missing or has been replaced.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已绑定的主机不存在或已被替换。")),
                       @Variant(id = "terminal.open_failed",
                                source = "Could not open the terminal interface.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无法打开终端界面。")),
                       @Variant(id = "terminal.out_of_range",
                                source = "The bound controller is out of reach.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已绑定的主机超出操作范围。")),
                       @Variant(id = "terminal.unbound",
                                source = "Bind the terminal to a multiblock controller first.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "请先将终端绑定至多方块主机。")),
                       @Variant(id = "terminal.unloaded",
                                source = "The bound controller's chunk is not loaded.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已绑定主机所在的区块尚未加载。")),
                       @Variant(id = "terminal.wrong_dimension",
                                source = "The bound controller is in another dimension.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已绑定的主机位于其他维度。")),
                       @Variant(id = "unknown_tier_category",
                                source = "Unknown tier block category: %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "未知等级方块类别：%s。")),
                       @Variant(id = "unloaded",
                                source = "Target position is not loaded: %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "目标位置未加载：%s。")),
                       @Variant(id = "unsupported_liquid_candidate",
                                source = "Liquid candidate is not supported: %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "暂不支持流体候选：%s。")),
                       @Variant(id = "pattern_reloaded_incompatible",
                                source = "The multiblock pattern changed incompatibly. Reopen the terminal.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "多方块结构样式发生了不兼容变更，请重新打开终端。")),
                       @Variant(id = "terminal.flip",
                                source = "Flip: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "翻转：%s")),
                       @Variant(id = "terminal.mode.build",
                                source = "Build",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搭建")),
                       @Variant(id = "terminal.mode.demolish",
                                source = "Demolish",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "拆除")),
                       @Variant(id = "terminal.no_hatch",
                                source = "No hatch: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无舱室：%s")),
                       @Variant(id = "terminal.no_result",
                                source = "No batch result yet",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "暂无批次结果")),
                       @Variant(id = "terminal.off",
                                source = "Off",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "关")),
                       @Variant(id = "terminal.on",
                                source = "On",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "开")),
                       @Variant(id = "terminal.repeat",
                                source = "Repeat section %s: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "重复段%s：%s")),
                       @Variant(id = "terminal.replace",
                                source = "Replace: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "替换：%s")),
                       @Variant(id = "terminal.structure_locked",
                                source = "[Locked] %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[锁定] %s")),
                       @Variant(id = "terminal.structure_selected",
                                source = "[Selected] %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[已选] %s")),
                       @Variant(id = "terminal.structure_unselected",
                                source = "[Unselected] %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[未选] %s")),
                       @Variant(id = "terminal.tier",
                                source = "%s: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s：%s")),
                       @Variant(id = "execution_failed",
                                source = "Automatic building failed; the bound terminal session was closed.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "自动搭建失败，已关闭绑定的终端会话。")),
                       @Variant(id = "execution_result",
                                source = "Auto-build %s: %s placed, %s removed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自动搭建%s：放置%s，拆除%s")),
                       @Variant(id = "terminal.bound",
                                source = "Bound to %s at %s.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已绑定至 %s，坐标：%s。")),
                       @Variant(id = "terminal.execute",
                                source = "Execute",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "执行")),
                       @Variant(id = "terminal.result",
                                source = "%s · P:%s R:%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s · 放置:%s 拆除:%s")),
               })
@NullMarked
public class TerminalBehavior extends ItemSyncHolder implements IItemUIFactory, SyncItem {

    @Getter
    @Setter
    @SaveField
    private BlockPos position = BlockPos.ZERO;
    @Getter
    @Setter
    @SaveField
    private ResourceLocation dimension = Level.OVERWORLD.location();
    @Getter
    @Setter
    @SaveField
    private @Nullable ResourceLocation definitionId;
    @Getter
    @SaveField
    @SyncBoth
    private boolean replaceMode;
    @Getter
    @SaveField
    @SyncBoth
    private boolean noHatchMode = true;
    @SaveField
    @SyncBoth
    private boolean flipMode;
    @SaveField
    @SyncBoth
    private StructureOperation mode = StructureOperation.BUILD;
    @SaveField
    @SyncBoth
    private final Object2ObjectMap<String, ResourceLocation> tierChoices = new Object2ObjectLinkedOpenHashMap<>();
    @SaveField
    @SyncBoth
    private final Object2IntMap<PatternAxis> dimensions = new Object2IntOpenHashMap<>();
    @SaveField
    @SyncBoth
    private final Object2ObjectMap<String, String> choices = new Object2ObjectLinkedOpenHashMap<>();
    @SaveField
    @SyncBoth
    private final ObjectSet<String> selectedStructures = new ObjectLinkedOpenHashSet<>(
            ObjectList.of(MultiblockControllerMachine.DEFAULT_STRUCTURE));
    @Getter
    @SyncToClient
    private final Object2ObjectMap<String, StructureRequest> structures = new Object2ObjectLinkedOpenHashMap<>();
    @Getter
    @SyncToClient
    private final ObjectSet<String> included = new ObjectLinkedOpenHashSet<>();
    @Getter
    @SyncToClient
    private @Nullable StructurePreview preview;
    @Getter
    @SyncToClient
    private @Nullable BatchStatus status;
    @Getter
    @SyncToClient
    private int placed;
    @Getter
    @SyncToClient
    private int removed;
    private boolean refreshUI;
    private @Nullable MultiblockControllerMachine openedController;
    private @Nullable ServerPlayer openedPlayer;
    private ObjectList<MaterialRequest.Result> boundSources = ObjectList.of();
    private long patternGeneration;

    /** The registered component creates independent annotated state for each opened stack. */
    public TerminalBehavior() {
        this(ItemStack.EMPTY, RegistryAccess.EMPTY);
    }

    public TerminalBehavior(ItemStack stack, RegistryAccess registries) {
        super(stack, registries);
    }

    @ClientFieldChangeListener(fieldName = { "replaceMode", "noHatchMode", "structures", "included",
            "preview",
            "status", "placed", "removed" })
    public void onTerminalChanged() {
        refreshUI = true;
    }

    @ServerFieldChangeListener(fieldName = { "flipMode", "mode", "tierChoices", "dimensions", "choices",
            "selectedStructures" })
    public void onSelectionChanged() {
        initialize(openedController.getDefinition());
        refresh();
    }

    @RPCMethod
    public void execute(RPCSender sender, long fingerprint) {
        var player = sender.asPlayer();
        if (fingerprint != Long.parseUnsignedLong(preview.fingerprint(), 16)) return;
        try {
            var result = openedController.autoBuildBatch(player, createRequest(boundSources), fingerprint);
            status = result.status();
            placed = result.placed();
            removed = result.removed();
            player.displayClientMessage(Component.translatable("gtpm.multiblock.autobuild.execution_result",
                    Component
                            .translatable("gtpm.multiblock.autobuild.status." + status.name().toLowerCase(Locale.ROOT)),
                    placed, removed), true);
            refresh();
        } catch (RuntimeException exception) {
            GTPM.LOGGER.error("Terminal execution failed for {}", definitionId, exception);
            player.displayClientMessage(Component.translatable("gtpm.multiblock.autobuild.execution_failed"), true);
            player.closeContainer();
        }
    }

    private void initialize(MultiblockMachineDefinition definition) {
        structures.clear();
        included.clear();
        var planner = new BatchPlanner();
        for (String name : definition.getStructureOrder()) {
            var request = planner.defaultStructureOptions(definition, name);
            var pattern = definition.getPattern(name);
            for (var choice : pattern.getChoiceDescriptors()) {
                String branch = choices.get(choice.id());
                if (choice.alternatives().contains(branch)) request = request.withChoice(choice.path(), branch);
            }
            var restored = PatternSelections.of(request.choices());
            for (var entry : choices.object2ObjectEntrySet())
                restored = restored.with(entry.getKey(), entry.getValue());
            request = planner.normalizeChoices(definition, new StructureRequest(name, request.repetitions(),
                    request.tierChoices(), request.flipMode(), request.mode(), restored));
            int[] repetitions = request.repetitions().toIntArray();
            var axes = pattern.getSourceDefinition().getAxes();
            for (var repeat : pattern.getRepeatDescriptors()) {
                if (repeat.minimum() == repeat.maximum()) continue;
                var axis = PatternAxis.of(switch (repeat.axis()) {
                    case DEPTH -> axes.getDepth();
                    case HEIGHT -> axes.getHeight();
                    case WIDTH -> axes.getWidth();
                });
                if (dimensions.containsKey(axis)) {
                    repetitions = pattern.resizeLayout(repetitions, request.choices(), repeat.index(),
                            dimensions.getInt(axis));
                }
            }
            var tiers = new Object2ObjectLinkedOpenHashMap<>(request.tierChoices());
            tiers.putAll(tierChoices);
            request = new StructureRequest(name, new IntArrayList(repetitions), tiers,
                    definition.isAllowFlip() && flipMode, mode, request.choices());
            structures.put(name, normalize(definition, request));
            if (selectedStructures.contains(name)) included.add(name);
        }
        choices.keySet().removeIf(key -> (key.startsWith("$") || key.startsWith("@measure:")) &&
                structures.values().stream().noneMatch(request -> request.choices().containsKey(key)));
        for (var request : structures.values()) {
            for (var entry : request.choices().object2ObjectEntrySet()) {
                if (!entry.getValue().equals(choices.get(entry.getKey())))
                    choices.put(entry.getKey(), entry.getValue());
            }
        }
        if (included.isEmpty()) included.add(MultiblockControllerMachine.DEFAULT_STRUCTURE);
        for (String name : new ObjectArrayList<>(included)) includeDependencies(definition, name);
        patternGeneration = StructurePatternRegistry.generation();
    }

    private StructureRequest normalize(MultiblockMachineDefinition definition, StructureRequest request) {
        request = new BatchPlanner().normalizeChoices(definition, request);
        var domains = new BatchPlanner().activeTierChoiceOptions(definition, request);
        var tiers = new Object2ObjectLinkedOpenHashMap<String, ResourceLocation>();
        for (var entry : domains.object2ObjectEntrySet()) {
            ResourceLocation current = request.tierChoices().get(entry.getKey());
            tiers.put(entry.getKey(), entry.getValue().contains(current) ? current : entry.getValue().getFirst());
        }
        return new StructureRequest(request.structureName(), request.repetitions(), tiers,
                request.flipMode(), request.mode(), request.choices());
    }

    private void includeDependencies(MultiblockMachineDefinition definition, String name) {
        if (structures.get(name).mode() != StructureOperation.BUILD) return;
        for (String dependency : definition.getRequiredStructures(name)) {
            included.add(dependency);
            structures.put(dependency, structures.get(dependency).withMode(StructureOperation.BUILD));
        }
    }

    private BatchRequest createRequest(ObjectList<MaterialRequest.Result> sources) {
        var definition = openedController.getDefinition();
        return new BatchRequest(definition.getStructureOrder().stream().filter(included::contains)
                .map(structures::get).collect(ObjectArrayList.toList()), replaceMode, noHatchMode, sources);
    }

    @ServerFieldChangeListener(fieldName = { "replaceMode", "noHatchMode" })
    public void refresh() {
        var definition = openedController.getDefinition();
        if (patternGeneration != StructurePatternRegistry.generation()) initialize(definition);
        boundSources = MaterialSources.request(openedPlayer, new MaterialRequest(ObjectList.of(), getStack(), null));
        preview = StructurePreview.bound(definition.getId(),
                new BatchPlanner().resolveBatch(openedController, openedPlayer, createRequest(boundSources)));
    }

    @Override
    public TerminalBehavior createSyncHolder(ItemStack stack, RegistryAccess registries) {
        return new TerminalBehavior(stack, registries);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        if (MetaMachine.getMachine(level, blockPos) instanceof MultiblockControllerMachine controller) {
            if (player instanceof ServerPlayer serverPlayer &&
                    !bind(controller, serverPlayer, context.getHand(), itemStack)) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack item, Level level, Player player,
                                                  InteractionHand usedHand) {
        if (player instanceof ServerPlayer serverPlayer &&
                !openBound(serverPlayer, usedHand, item)) {
            return InteractionResultHolder.fail(item);
        }
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

    @Override
    public boolean canCreateUI(HeldItemUIHolder holder, Player player) {
        return definitionId != null &&
                GTRegistries.MACHINES.get(definitionId) instanceof MultiblockMachineDefinition &&
                isUIStillValid(holder, player);
    }

    @Override
    public boolean isUIStillValid(HeldItemUIHolder holder, Player player) {
        if (definitionId == null || !ItemStack.isSameItem(holder.getHeld(), holder.getOpenedStack()) ||
                !dimension.equals(player.level().dimension().location())) {
            return false;
        }
        if (player.level().isClientSide) return true;
        if (!player.level().isLoaded(position)) return false;
        MultiblockControllerMachine controller = boundController(player);
        return controller != null && (openedController == null || openedController == controller) &&
                holder.getHeld() == getStack() &&
                canAccess(controller, player, holder.getHand(), holder.getHeld());
    }

    @Override
    public UI createUI(HeldItemUIHolder holder, Player player) {
        var definition = (MultiblockMachineDefinition) GTRegistries.MACHINES.get(definitionId);
        initialize(definition);
        if (player instanceof ServerPlayer serverPlayer) {
            openedController = boundController(player);
            openedPlayer = serverPlayer;
            refresh();
            var root = new UIElement() {

                private int refreshTicks;

                @Override
                public void serverTick() {
                    super.serverTick();
                    if (++refreshTicks < 20) return;
                    refreshTicks = 0;
                    try {
                        refresh();
                    } catch (RuntimeException exception) {
                        GTPM.LOGGER.error("Closed terminal after refresh failed for {}", definition.getId(),
                                exception);
                        player.closeContainer();
                    }
                }

                @Override
                protected void onRemoved() {
                    openedController = null;
                    openedPlayer = null;
                    super.onRemoved();
                }
            };
            UITemplate.setBounds(root, 0, 0, 300, 200);
            return UI.of(root);
        }

        class Page extends UIElement {

            private final UIElement main = new UIElement();
            private final ClientMultiblockPreviewElement preview;
            private final BlockChoiceList blocks;
            private final GTScrollerViewElement options = new GTScrollerViewElement();
            private final ObjectList<UIElement> controls = new ObjectArrayList<>();
            private final ObjectList<GTTextFieldElement> dimensionInputs = new ObjectArrayList<>();
            private final Object2IntMap<PatternAxis> pendingDimensions = new Object2IntOpenHashMap<>();
            private final GTButtonElement execute;
            private final GTButtonElement fullscreen;
            private final GTButtonElement edit;
            private final GTLabelElement title;
            private final GTLabelElement result = label(192, 167, 100, Component.empty());
            private @Nullable GTButtonElement visibility;
            private @Nullable StructurePreview displayedPreview;
            private boolean expanded;
            private boolean editing;
            private boolean worldVisible;
            private boolean awaitingPreview;
            private int dimensionTicks;
            private int selectedStructure;
            private int width = 300;
            private int height = 200;

            Page() {
                var empty = new StructurePreview(definition.getId(), "awaiting-server", false,
                        ObjectList.of(), ObjectList.of(), ObjectList.of());

                UITemplate.setBounds(this, 0, 0, width, height);
                setOverflowVisible(true);
                main.getStyle().backgroundTexture(GuiTextures.BACKGROUND);
                main.setOverflowVisible(false);
                addChild(main);
                title = label(6, 6, 244, definition.getBlock().getName());
                title.getStyle().tooltips(Component.literal(dimension + " @ " + position.toShortString()));
                preview = new ClientMultiblockPreviewElement(180, 176, new PreviewVisibility(empty), true);
                blocks = new BlockChoiceList((group, id) -> change(() -> tierChoices.put(group, id)));
                blocks.setDisplay(false);
                options.scrollerStyle(style -> style.mode(ScrollerMode.VERTICAL)
                        .verticalScrollDisplay(ScrollDisplay.NEVER).horizontalScrollDisplay(ScrollDisplay.NEVER)
                        .scrollerViewStyle(0));
                options.viewPort(port -> {
                    port.getLayout().paddingAll(4);
                    port.getStyle().backgroundTexture(GuiTextures.BACKGROUND_INVERSE);
                });
                execute = button(242, 180, 54, Component.translatable("gtpm.multiblock.autobuild.terminal.execute"));
                execute.setOnClick(event -> {
                    rpcToServer("execute", Long.parseUnsignedLong(TerminalBehavior.this.preview.fingerprint(), 16));
                    event.stopPropagation();
                });
                edit = button(260, 4, 16, Component.literal("✎"));
                edit.getStyle().tooltips(text("edit"));
                edit.setOnClick(event -> {
                    editing = !editing;
                    edit.setText(Component.literal(editing ? "✘" : "✎"));
                    blocks.setDisplay(editing);
                    if (!editing) blocks.close();
                    event.stopPropagation();
                });
                fullscreen = button(278, 4, 16, Component.literal("↗"));
                fullscreen.getStyle().tooltips(text("fullscreen"));
                fullscreen.setOnClick(event -> {
                    expanded = !expanded;
                    fullscreen.setText(Component.literal(expanded ? "↙" : "↗"));
                    fullscreen.getStyle().tooltips(text(expanded ? "compact" : "fullscreen"));
                    var ui = getModularUI();
                    ui.init(ui.getScreenWidth(), ui.getScreenHeight());
                    ((ModularUIContainerScreen) ui.getScreen()).init();
                    event.stopPropagation();
                });
                main.addChildren(title, preview, options, result, execute, edit, fullscreen);
                title.getStyle().zIndex(1);
                options.getStyle().zIndex(1);
                result.getStyle().zIndex(1);
                execute.getStyle().zIndex(1);
                edit.getStyle().zIndex(1);
                fullscreen.getStyle().zIndex(1);
                addChild(blocks);
                rebuildOptions();
                updateDisplay();
            }

            private Size resize(Size screen) {
                width = expanded ? screen.getWidth() : Math.min(300, screen.getWidth());
                height = expanded ? screen.getHeight() : Math.min(200, screen.getHeight());
                UITemplate.setBounds(main, 0, 0, width, height);
                main.getStyle().backgroundTexture(expanded ? IGuiTexture.EMPTY : GuiTextures.BACKGROUND);
                title.getLayout().left(expanded ? 8 : 6).top(expanded ? 10 : 6)
                        .width(expanded ? Math.min(220, width - 130) : width - 48);
                title.textStyle(style -> style.textColor(expanded ? 0xffffffff : 0xff404040).textShadow(expanded));
                edit.getLayout().left(width - (expanded ? 44 : 40)).top(expanded ? 8 : 4);
                fullscreen.getLayout().left(width - (expanded ? 26 : 22)).top(expanded ? 8 : 4);
                rebuildOptions();
                return Size.of(width, height);
            }

            private String structureName() {
                return definition.getStructureOrder().get(selectedStructure);
            }

            private void rebuildOptions() {
                options.clearAllScrollViewChildren();
                controls.clear();
                dimensionInputs.clear();
                visibility = null;
                int contentWidth = 44;
                var requests = structures;
                var selected = included;
                String name = structureName();
                var structure = requests.get(name);
                var replace = button(0, 0, contentWidth, text("replace"));
                replace.textStyle(style -> style.textColor(replaceMode ? 0xff167129 : 0xff404040));
                replace.setOnClick(event -> {
                    replaceMode = !replaceMode;
                    rebuildOptions();
                    event.stopPropagation();
                });
                var casings = button(0, 20, contentWidth, text("casings"));
                casings.textStyle(style -> style.textColor(noHatchMode ? 0xff167129 : 0xff404040));
                casings.setOnClick(event -> {
                    noHatchMode = !noHatchMode;
                    rebuildOptions();
                    event.stopPropagation();
                });
                options.addScrollViewChildren(replace, casings);
                controls.add(replace);
                controls.add(casings);
                int y = 40;
                var order = definition.getStructureOrder();
                if (order.size() > 1) {
                    addStep(y, contentWidth, displayName(name), () -> {
                        selectedStructure = (selectedStructure + order.size() - 1) % order.size();
                        blocks.close();
                        rebuildOptions();
                    }, () -> {
                        selectedStructure = (selectedStructure + 1) % order.size();
                        blocks.close();
                        rebuildOptions();
                    }, true, true);
                    y += 30;
                }
                boolean required = selected.stream()
                        .anyMatch(entry -> requests.get(entry).mode() == StructureOperation.BUILD &&
                                definition.getRequiredStructures(entry).contains(name));
                boolean locked = required || selected.contains(name) && selected.size() == 1;
                if (!locked) {
                    var included = button(0, y, contentWidth,
                            text(selected.contains(name) ? "selected" : "unselected"));
                    included.setOnClick(event -> {
                        change(() -> {
                            if (!selectedStructures.remove(name)) selectedStructures.add(name);
                        });
                        event.stopPropagation();
                    });
                    options.addScrollViewChild(included);
                    controls.add(included);
                    y += 20;
                }
                if (definition.isRenderWorldPreview()) {
                    worldVisible = MultiblockInWorldPreviewRenderer.isPreviewVisible(position, name);
                    visibility = button(0, y, contentWidth,
                            text(worldVisible ? "visible" : "hidden"));
                    visibility.getStyle().tooltips(text("world_preview"));
                    visibility.setOnClick(event -> {
                        MultiblockInWorldPreviewRenderer.togglePreview(position, TerminalBehavior.this.preview,
                                structureName(),
                                GTPMConfigHolder.INSTANCE.client.inWorldPreviewDuration * 20);
                        event.stopPropagation();
                    });
                    options.addScrollViewChild(visibility);
                    y += 20;
                }
                var mode = button(0, y, contentWidth,
                        text("mode." + structure.mode().name().toLowerCase(Locale.ROOT)));
                mode.setActive(!required);
                mode.setOnClick(event -> {
                    change(() -> TerminalBehavior.this.mode = TerminalBehavior.this.mode == StructureOperation.BUILD ?
                            StructureOperation.DEMOLISH : StructureOperation.BUILD);
                    event.stopPropagation();
                });
                options.addScrollViewChild(mode);
                if (!required) controls.add(mode);
                y += 20;
                if (definition.isAllowFlip()) {
                    var flip = button(0, y, contentWidth,
                            text("flip", text(structure.flipMode() ? "on" : "off")));
                    flip.setOnClick(event -> {
                        change(() -> flipMode = !flipMode);
                        event.stopPropagation();
                    });
                    options.addScrollViewChild(flip);
                    controls.add(flip);
                    y += 20;
                }
                var pattern = definition.getPattern(name);
                var axes = pattern.getSourceDefinition().getAxes();
                int[] repetitions = structure.repetitions().toIntArray();
                int[] sizes = pattern.getDimensions(repetitions, structure.choices());
                for (var repetition : pattern.getRepeatDescriptors()) {
                    if (repetition.minimum() == repetition.maximum()) continue;
                    int index = repetition.index();
                    var axis = PatternAxis.of(switch (repetition.axis()) {
                        case DEPTH -> axes.getDepth();
                        case HEIGHT -> axes.getHeight();
                        case WIDTH -> axes.getWidth();
                    });
                    int[] bounds = repetitions.clone();
                    bounds[index] = repetition.minimum();
                    int minimum = pattern.getDimensions(bounds, structure.choices())[repetition.axis().ordinal()];
                    bounds[index] = repetition.maximum();
                    int maximum = pattern.getDimensions(bounds, structure.choices())[repetition.axis().ordinal()];
                    if (minimum == maximum) continue;
                    var title = text("axis." + axis.name().toLowerCase(Locale.ROOT));
                    options.addScrollViewChild(label(0, y, contentWidth, title));
                    var input = new GTTextFieldElement(0, y + 10, contentWidth, 12) {

                        @Override
                        protected void onRawTextUpdate() {
                            super.onRawTextUpdate();
                            dimensionTicks = 5;
                            updateInteractivity();
                        }

                        @Override
                        protected void onMouseWheel(UIEvent event) {
                            if (isEditable() && !isError()) {
                                setRawText(Integer.toString(Math.clamp((long) Integer.parseInt(getText()) +
                                        (event.deltaY > 0 ? 1 : -1), minimum, maximum)));
                                event.stopPropagation();
                            }
                        }
                    };
                    input.setNumbersOnlyInt(minimum, maximum);
                    input.setText(
                            Integer.toString(pendingDimensions.getOrDefault(axis, sizes[repetition.axis().ordinal()])),
                            false);
                    input.setTextResponder(value -> pendingDimensions.put(axis, Integer.parseInt(value)));
                    input.textFieldStyle(style -> style.fontSize(8).textColor(0xff404040).textShadow(false)
                            .focusOverlay(IGuiTexture.EMPTY));
                    input.getStyle().backgroundTexture(GuiTextures.BACKGROUND_INVERSE).tooltips(Component.empty());
                    options.addScrollViewChild(input);
                    dimensionInputs.add(input);
                    controls.add(input);
                    y += 30;
                }
                for (var choice : pattern.getChoiceDescriptors()) {
                    var values = choice.alternatives();
                    int index = values.indexOf(structure.choices().getOrDefault(choice.path(), values.getFirst()));
                    addStep(y, contentWidth, text("variant_index", index + 1, values.size()),
                            () -> change(() -> TerminalBehavior.this.choices.put(choice.path(), values.get(index - 1))),
                            () -> change(() -> TerminalBehavior.this.choices.put(choice.path(), values.get(index + 1))),
                            index > 0, index + 1 < values.size());
                    y += 30;
                }
                var measurementValues = pattern.getMeasurementValues(structure.choices());
                for (var measurement : pattern.getMeasurements()) {
                    String key = "@measure:" + measurement.getParameter();
                    int value = measurementValues.getInt(measurement.getParameter());
                    addStep(y, contentWidth, Component.literal(measurement.getParameter() + ": " + value),
                            () -> change(() -> TerminalBehavior.this.choices.put(key, Integer.toString(value - 1))),
                            () -> change(() -> TerminalBehavior.this.choices.put(key, Integer.toString(value + 1))),
                            value > measurement.getMinimum(), value < measurement.getMaximum());
                    y += 30;
                }
                var layout = pattern.expandLayout(repetitions, structure.choices());
                for (var instance : layout.getModuleChoices()) {
                    var candidates = instance.getDescriptor().getCandidates();
                    int index = candidates.indexOf(instance.getSelection());
                    addStep(y, contentWidth,
                            Component.literal(instance.getDescriptor().getId() + ": " + instance.getSelection()),
                            () -> change(() -> TerminalBehavior.this.choices.put(instance.getPath(),
                                    candidates.get(index - 1))),
                            () -> change(() -> TerminalBehavior.this.choices.put(instance.getPath(),
                                    candidates.get(index + 1))),
                            index > 0, index + 1 < candidates.size());
                    y += 30;
                }
                options.viewContainer.getLayout().width(contentWidth).height(y);
                int optionHeight = expanded ? Math.min(y + 8, height - 96) : height - 56;
                options.viewPort(port -> port.getStyle().backgroundTexture(expanded ?
                        new ColorRectTexture(0xA0202020) : GuiTextures.BACKGROUND_INVERSE));
                UITemplate.setBounds(options, width - (expanded ? 62 : 58), expanded ? 34 : 20, 54, optionHeight);
                preview.setFullscreen(expanded);
                preview.getLayout().left(expanded ? 0 : 4).top(expanded ? 0 : 20);
                preview.resize(expanded ? width : width - 66, expanded ? height : height - 24);
                execute.getLayout().left(width - (expanded ? 62 : 58))
                        .top(expanded ? optionHeight + 42 : height - 20);
                result.getLayout().left(width - (expanded ? 62 : 58))
                        .top(expanded ? optionHeight + 62 : height - 33).width(54);
                result.textStyle(style -> style.textColor(expanded ? 0xffffffff : 0xff404040).textShadow(expanded));
                blocks.setFullscreen(expanded);
                blocks.resize(expanded ? width - BlockChoiceList.WIDTH - 66 : width + 4,
                        expanded ? 34 : 20, height - (expanded ? 38 : 24));
                var choices = new BatchPlanner().activeTierChoiceOptions(definition, structure);
                blocks.setChoices(choices, structure.tierChoices());
                boolean available = !choices.isEmpty();
                edit.setActive(available);
                if (!available) {
                    editing = false;
                    edit.setText(Component.literal("✎"));
                    blocks.setDisplay(false);
                    blocks.close();
                }
                updateInteractivity();
            }

            private void addStep(int y, int width, Component name, Runnable previous, Runnable next,
                                 boolean hasPrevious, boolean hasNext) {
                options.addScrollViewChild(label(0, y, width, name));
                var back = button(0, y + 10, 16, Component.literal("◀"));
                back.setOnClick(event -> {
                    previous.run();
                    event.stopPropagation();
                });
                back.setActive(hasPrevious);
                var forward = button(width - 16, y + 10, 16, Component.literal("▶"));
                forward.setOnClick(event -> {
                    next.run();
                    event.stopPropagation();
                });
                forward.setActive(hasNext);
                options.addScrollViewChildren(back, forward);
                if (hasPrevious) controls.add(back);
                if (hasNext) controls.add(forward);
            }

            private void change(Runnable update) {
                update.run();
                awaitingPreview = true;
                initialize(definition);
                rebuildOptions();
            }

            private void updateDisplay() {
                if (dimensionTicks == 0 && TerminalBehavior.this.preview != null &&
                        TerminalBehavior.this.preview != displayedPreview) {
                    awaitingPreview = false;
                    displayedPreview = TerminalBehavior.this.preview;
                    preview.setPreview(displayedPreview);
                    MultiblockInWorldPreviewRenderer.updatePreview(position, displayedPreview,
                            GTPMConfigHolder.INSTANCE.client.inWorldPreviewDuration * 20);
                }
                result.setValue(status == null ? Component.empty() :
                        Component.translatable("gtpm.multiblock.autobuild.status." +
                                status.name().toLowerCase(Locale.ROOT)));
                result.getStyle().tooltips(status == null ? Component.empty() :
                        Component.translatable("gtpm.multiblock.autobuild.terminal.result",
                                Component.translatable("gtpm.multiblock.autobuild.status." +
                                        status.name().toLowerCase(Locale.ROOT)),
                                placed, removed));
                updateInteractivity();
            }

            private void updateInteractivity() {
                boolean ready = TerminalBehavior.this.preview != null;
                controls.forEach(button -> button.setActive(ready));
                blocks.setInteractive(ready);
                boolean settled = ready && dimensionTicks == 0 && !awaitingPreview;
                execute.setActive(settled);
                if (visibility != null) visibility.setActive(settled);
            }

            @Override
            public void screenTick() {
                super.screenTick();
                if (dimensionTicks > 0 && dimensionInputs.stream().noneMatch(GTTextFieldElement::isError) &&
                        --dimensionTicks == 0) {
                    if (!pendingDimensions.isEmpty()) {
                        dimensions.putAll(pendingDimensions);
                        pendingDimensions.clear();
                        awaitingPreview = true;
                        displayedPreview = TerminalBehavior.this.preview;
                        initialize(definition);
                        refreshUI = true;
                    }
                    updateInteractivity();
                }
                if (refreshUI) {
                    if (dimensionTicks == 0 && dimensionInputs.stream().noneMatch(GTTextFieldElement::isFocused)) {
                        refreshUI = false;
                        rebuildOptions();
                    }
                    updateDisplay();
                }
                if (visibility != null) {
                    boolean visible = MultiblockInWorldPreviewRenderer.isPreviewVisible(position,
                            structureName());
                    if (worldVisible != visible) {
                        worldVisible = visible;
                        visibility.setText(text(visible ? "visible" : "hidden"));
                    }
                }
            }
        }

        var root = new Page();
        return UI.of(root, root::resize);
    }

    private static GTButtonElement button(int x, int y, int width, Component text) {
        var button = new GTButtonElement(x, y, width, 16);
        button.setButtonTexture(GuiTextures.VANILLA_BUTTON).setText(text);
        button.getLayout().paddingAll(0);
        button.text.getLayout().widthPercent(100).heightPercent(100).marginAll(0).minWidth(0);
        button.textStyle(style -> style.adaptiveWidth(false).adaptiveHeight(false).fontSize(8)
                .textColor(0xff404040).textShadow(false).textWrap(TextWrap.HIDE));
        button.getStyle().tooltips(text);
        return button;
    }

    private static GTLabelElement label(int x, int y, int width, Component text) {
        var label = new GTLabelElement(x, y, width, 10, text);
        label.textStyle(style -> style.adaptiveWidth(false).adaptiveHeight(false).fontSize(9)
                .textColor(0xff404040).textShadow(false).textWrap(TextWrap.HIDE));
        label.getStyle().tooltips(text);
        return label;
    }

    private static Component text(String key, Object... arguments) {
        return Component.translatable("gtpm.multiblock.configuration." + key, arguments);
    }

    private static Component displayName(String name) {
        return name.equals(MultiblockControllerMachine.DEFAULT_STRUCTURE) ? text("main") : Component.translatable(name);
    }

    private static boolean bind(MultiblockControllerMachine controller, ServerPlayer player, InteractionHand hand,
                                ItemStack terminal) {
        if (!canAccess(controller, player, hand, terminal)) return reject(player, "access_denied");
        TerminalBehavior data = (TerminalBehavior) ItemSyncHolder.create(terminal, player.registryAccess());
        data.position = controller.getBlockPos().immutable();
        data.dimension = player.level().dimension().location();
        data.definitionId = controller.getDefinition().getId();
        data.saveToStack();
        player.displayClientMessage(Component.translatable("gtpm.multiblock.autobuild.terminal.bound",
                controller.getDefinition().getBlock().getName(), controller.getBlockPos().toShortString()), true);
        return true;
    }

    private static boolean openBound(ServerPlayer player, InteractionHand hand, ItemStack terminal) {
        TerminalBehavior data = (TerminalBehavior) ItemSyncHolder.create(terminal, player.registryAccess());
        if (data.definitionId == null) return reject(player, "unbound");
        if (!data.dimension.equals(player.level().dimension().location())) return reject(player, "wrong_dimension");
        if (!player.level().isLoaded(data.position)) return reject(player, "unloaded");
        MultiblockControllerMachine controller = data.boundController(player);
        if (controller == null) return reject(player, "controller_missing");
        if (player.distanceToSqr(Vec3.atCenterOf(data.position)) > 64) return reject(player, "out_of_range");
        if (!canAccess(controller, player, hand, terminal)) return reject(player, "access_denied");
        try {
            return HeldItemUIHelper.open(player, hand) || reject(player, "open_failed");
        } catch (RuntimeException exception) {
            GTPM.LOGGER.error("Failed to open terminal bound to {} at {} for {}", data.definitionId,
                    data.position, player.getGameProfile().getName(), exception);
            return reject(player, "open_failed");
        }
    }

    private @Nullable MultiblockControllerMachine boundController(Player player) {
        return MetaMachine.getMachine(player.level(),
                position) instanceof MultiblockControllerMachine controller &&
                definitionId.equals(controller.getDefinition().getId()) ? controller : null;
    }

    private static boolean canAccess(MultiblockControllerMachine controller, Player player,
                                     InteractionHand hand, ItemStack terminal) {
        return !player.isSpectator() && player.getItemInHand(hand) == terminal &&
                controller.getLevel() == player.level() &&
                player.distanceToSqr(Vec3.atCenterOf(controller.getBlockPos())) <= 64 &&
                player.level().mayInteract(player, controller.getBlockPos()) &&
                MachineOwner.canOpenOwnerMachine(player, controller) &&
                MachineOwner.canBreakOwnerMachine(player, controller);
    }

    private static boolean reject(ServerPlayer player, String reason) {
        player.displayClientMessage(Component.translatable("gtpm.multiblock.autobuild.terminal." + reason), true);
        return false;
    }
}

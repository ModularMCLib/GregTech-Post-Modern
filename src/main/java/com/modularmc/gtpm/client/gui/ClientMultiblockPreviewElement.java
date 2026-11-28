package com.modularmc.gtpm.client.gui;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.block.property.GTBlockStateProperties;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.element.GTButtonElement;
import com.modularmc.gtpm.api.gui.element.GTItemSlotElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.multiblock.MultiblockBlockInfo;
import com.modularmc.gtpm.api.multiblock.MultiblockPreviewLevel;
import com.modularmc.gtpm.api.multiblock.preview.PreviewVisibility;
import com.modularmc.gtpm.api.multiblock.preview.StructurePreview;
import com.modularmc.gtpm.api.multiblock.preview.StructurePreview.Cell;
import com.modularmc.gtpm.api.multiblock.util.RelativeDirection;
import com.modularmc.gtpm.integration.emi.gui.EmiIngredient;
import com.modularmc.gtpm.integration.jei.gui.JeiIngredient;
import com.modularmc.gtpm.integration.xei.GTXEIHelper;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.client.scene.ISceneBlockRenderHook;
import com.lowdragmc.lowdraglib2.client.scene.WorldSceneRenderer;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import com.lowdragmc.lowdraglib2.utils.data.BlockPosFace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.sidebar.SidebarGroupGuiButton;
import dev.vfyjxf.taffy.style.TaffyPosition;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Setter;
import org.joml.Vector3f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Client-owned scene, focus/layer controls and paged materials for one preview window.
 */
@TransVariants(keyPrefix = "gtpm.multiblock",
               value = {
                       @Variant(id = "preview.button.flip.false",
                                source = "F-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "翻-")),
                       @Variant(id = "preview.button.flip.true",
                                source = "F+",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "翻+")),
                       @Variant(id = "preview.button.me.false",
                                source = "A-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "AE-")),
                       @Variant(id = "preview.button.me.true",
                                source = "A+",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "AE+")),
                       @Variant(id = "preview.button.mode.build",
                                source = "B",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搭")),
                       @Variant(id = "preview.button.mode.demolish",
                                source = "D",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "拆")),
                       @Variant(id = "preview.button.mode.locked",
                                source = "B!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搭!")),
                       @Variant(id = "preview.button.no_hatch.false",
                                source = "H-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无舱-")),
                       @Variant(id = "preview.button.no_hatch.true",
                                source = "H+",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无舱+")),
                       @Variant(id = "preview.button.repeat",
                                source = "R:%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "重:%s")),
                       @Variant(id = "preview.button.repeat_none",
                                source = "R:-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "重:-")),
                       @Variant(id = "preview.button.repeat_unit",
                                source = "U:%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "段:%s")),
                       @Variant(id = "preview.button.replace.false",
                                source = "X-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "替-")),
                       @Variant(id = "preview.button.replace.true",
                                source = "X+",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "替+")),
                       @Variant(id = "preview.button.selected.false",
                                source = "-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "-")),
                       @Variant(id = "preview.button.selected.locked",
                                source = "Lock",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锁")),
                       @Variant(id = "preview.button.selected.true",
                                source = "+",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "+")),
                       @Variant(id = "preview.button.tier_group",
                                source = "T:%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "组:%s")),
                       @Variant(id = "preview.button.tier_group_none",
                                source = "T:-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "组:-")),
                       @Variant(id = "preview.button.tier_value",
                                source = "V:%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "级:%s")),
                       @Variant(id = "preview.button.tier_value_none",
                                source = "V:-",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "级:-")),
                       @Variant(id = "preview.button.unavailable",
                                source = "!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "!")),
                       @Variant(id = "preview.cell.action.clear_for_air",
                                source = "clear for air",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "清空为空气")),
                       @Variant(id = "preview.cell.action.controller",
                                source = "controller",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制器")),
                       @Variant(id = "preview.cell.action.demolish_candidate",
                                source = "demolish",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "拆除")),
                       @Variant(id = "preview.cell.action.ignore_any",
                                source = "ignore",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "忽略")),
                       @Variant(id = "preview.cell.action.keep",
                                source = "keep",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "保留")),
                       @Variant(id = "preview.cell.action.place",
                                source = "place",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "放置")),
                       @Variant(id = "preview.cell.action.update_direction",
                                source = "update direction",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更新方向")),
                       @Variant(id = "preview.direction.down",
                                source = "Down",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "下")),
                       @Variant(id = "preview.direction.east",
                                source = "East",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "东")),
                       @Variant(id = "preview.direction.north",
                                source = "North",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "北")),
                       @Variant(id = "preview.direction.south",
                                source = "South",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "南")),
                       @Variant(id = "preview.direction.up",
                                source = "Up",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "上")),
                       @Variant(id = "preview.direction.west",
                                source = "West",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "西")),
                       @Variant(id = "preview.rotate",
                                source = "Click and drag to rotate",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "左键拖拽以旋转")),
                       @Variant(id = "preview.select",
                                source = "Right-click to check candidates",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击方块以查看其他可选方块")),
                       @Variant(id = "preview.tooltip.flip",
                                source = "Flip this structure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "翻转此结构")),
                       @Variant(id = "preview.tooltip.me",
                                source = "Real execution uses ME first, then player inventory; XEI does not query the network",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "实际执行优先使用 AE，再使用玩家物品栏；XEI 不查询网络")),
                       @Variant(id = "preview.tooltip.mode",
                                source = "Switch between build and demolition",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换搭建或拆除模式")),
                       @Variant(id = "preview.tooltip.mode_locked",
                                source = "Required by a selected build structure and locked to build mode",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "此结构是已选搭建结构的必需依赖，已锁定为搭建模式")),
                       @Variant(id = "preview.tooltip.no_hatch",
                                source = "Exclude all hatches and fill with original non-hatch candidates; skip hatch-only positions",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "排除所有舱室，使用原有非舱室候选补位；仅含舱室的位置跳过")),
                       @Variant(id = "preview.tooltip.repeat",
                                source = "Change this unit's repetition count",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "调整此结构段的重复次数")),
                       @Variant(id = "preview.tooltip.repeat_unit",
                                source = "Select a repeatable pattern unit",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "选择可重复结构段")),
                       @Variant(id = "preview.tooltip.replace",
                                source = "Allow replacing occupied targets",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "允许替换已占用目标")),
                       @Variant(id = "preview.tooltip.selected",
                                source = "Include or exclude this structure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "加入或移除此结构")),
                       @Variant(id = "preview.tooltip.selected_locked",
                                source = "Required by a selected build structure and cannot be removed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "此结构是已选搭建结构的必需依赖，无法移除")),
                       @Variant(id = "preview.tooltip.structure",
                                source = "Select the structure to configure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "选择要配置的结构")),
                       @Variant(id = "preview.tooltip.tier_group",
                                source = "Select a tiered block group",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "选择等级方块组")),
                       @Variant(id = "preview.tooltip.tier_group_selected",
                                source = "Tier group: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "等级组：%s")),
                       @Variant(id = "preview.tooltip.tier_value",
                                source = "Select an exact legal tier",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "选择严格合法等级")),
                       @Variant(id = "preview.tooltip.tier_value_selected",
                                source = "Exact tier candidate: %s (%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "精确等级候选：%s（%s）")),
                       @Variant(id = "preview.zoom",
                                source = "Use mousewheel or right-click + drag to zoom",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用鼠标滚轮或右键拖拽以缩放")),
                       @Variant(id = "configuration.main",
                                source = "Main structure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "主体结构")),
                       @Variant(id = "preview.button.hatches",
                                source = "H",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "舱")),
                       @Variant(id = "preview.button.layer",
                                source = "L:%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "层:%s")),
                       @Variant(id = "preview.button.layer_all",
                                source = "L:*",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "层:*")),
                       @Variant(id = "preview.button.structure",
                                source = "S",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "构")),
                       @Variant(id = "preview.button.structure_all",
                                source = "S:*",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "构:*")),
                       @Variant(id = "preview.cell.conflict",
                                source = "Conflicting structure targets",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "结构目标冲突")),
                       @Variant(id = "preview.cell.contribution",
                                source = "%s -> %s [%s], cell %s/%s/%s/%s/%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s → %s［%s］，单元 %s/%s/%s/%s/%s")),
                       @Variant(id = "preview.cell.demolition",
                                source = "Demolition target",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "拆除目标")),
                       @Variant(id = "preview.cell.direction",
                                source = "Required direction: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "要求方向：%s")),
                       @Variant(id = "preview.cell.hatch_replaceable",
                                source = "Accepts interchangeable hatches",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "此处可以替换为舱室")),
                       @Variant(id = "preview.cell.position",
                                source = "Relative position: %s, %s, %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "相对坐标：%s, %s, %s")),
                       @Variant(id = "preview.diagnostic.missing_material",
                                source = "Missing material",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "材料不足")),
                       @Variant(id = "preview.material.missing",
                                source = "Missing: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "缺少：%s")),
                       @Variant(id = "preview.material.required",
                                source = "Required: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需求：%s")),
                       @Variant(id = "preview.material.structure",
                                source = "  %s: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  %s：%s")),
                       @Variant(id = "preview.tooltip.focus",
                                source = "Focus one selected structure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "聚焦一个已选结构")),
                       @Variant(id = "preview.tooltip.hatches",
                                source = "Highlight positions that accept hatches",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高亮可以替换为舱室的位置")),
                       @Variant(id = "preview.tooltip.layer",
                                source = "Cycle visible layers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换可见层")),
                       @Variant(id = "preview.tooltip.next_materials",
                                source = "Next materials",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "下一页材料")),
                       @Variant(id = "preview.tooltip.previous_materials",
                                source = "Previous materials",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "上一页材料")),
               })
@OnlyIn(Dist.CLIENT)
@NullMarked
public final class ClientMultiblockPreviewElement extends UIElement {

    private static final int MATERIAL_BAR_HEIGHT = 24;

    private PreviewVisibility state;
    private MultiblockPreviewLevel previewLevel;
    @Nullable
    private Level sourceLevel;
    private final PreviewScene scene;
    private final UIElement materialViewport;
    private final UIElement predicateViewport = new UIElement();
    private final Object2ObjectMap<GTItemSlotElement, List<ItemStack>> predicateItems = new Object2ObjectLinkedOpenHashMap<>();
    private @Nullable Consumer<ItemStack> updateJEIMaterial;
    private @Nullable Consumer<ItemStack> updateJEIPredicate;
    private final GTButtonElement previousMaterials;
    private final GTButtonElement nextMaterials;
    private final ObjectList<GTItemSlotElement> materialSlots = new ObjectArrayList<>();
    private ObjectList<List<ItemStack>> materialCandidates = ObjectList.of();
    private final GTButtonElement focusButton;
    private final GTButtonElement layerButton;
    private final GTButtonElement hatchButton;
    private final GTLabelElement diagnosticLabel;
    private final boolean showAvailability;
    @Setter
    private @Nullable Consumer<Cell> onSelected;
    private int materialViewportWidth;
    private float sceneAspect;
    private final Long2ObjectMap<StructurePreview.Cell> sceneCells = new Long2ObjectOpenHashMap<>();
    private final LongList loadedPositions = new LongArrayList();
    private final LongList hatchPositions = new LongArrayList();
    private LongList renderedPositions = LongList.of();
    private final PreviewCellRenderHook renderHook = new PreviewCellRenderHook();
    private int materialPage;
    private long renderedViewRevision;
    private @Nullable BlockState cameraControllerState;
    private boolean removed;
    private boolean fullscreen;
    private long iconCycle = -1;

    public ClientMultiblockPreviewElement(int width, int height, PreviewVisibility state,
                                          boolean showAvailability) {
        this.state = state;
        this.showAvailability = showAvailability;
        getStyle().backgroundTexture(GuiTextures.BACKGROUND_INVERSE);
        materialViewportWidth = width - 36;
        layout(layout -> {
            layout.positionType(TaffyPosition.ABSOLUTE);
            layout.width(width);
            layout.height(height);
        });
        setOverflowVisible(false);
        Level clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) {
            throw new IllegalStateException("Cannot create a multiblock preview before the client level is loaded");
        }
        sourceLevel = clientLevel;
        previewLevel = new MultiblockPreviewLevel(clientLevel);

        int sceneHeight = height - MATERIAL_BAR_HEIGHT;
        sceneAspect = (float) width / sceneHeight;
        scene = new PreviewScene(0, 0, width, sceneHeight);
        scene.setAllowXEILookup(false);
        scene.setRenderFacing(false);
        scene.setRenderSelect(false);
        scene.setAfterWorldRender(ignored -> renderHatchOutlines());
        scene.setOnSelected((pos, facing) -> selectCell(pos));
        scene.addEventListener(UIEvents.HOVER_TOOLTIPS, this::addCellTooltips);
        addChild(scene);

        materialViewport = new UIElement();
        materialViewport.layout(layout -> layout.positionType(TaffyPosition.ABSOLUTE)
                .left(18).top(height - MATERIAL_BAR_HEIGHT + 3).width(materialViewportWidth).height(18));
        materialViewport.setOverflowVisible(false);
        previousMaterials = navigationButton(2, height - MATERIAL_BAR_HEIGHT + 3,
                "gtpm.multiblock.preview.tooltip.previous_materials");
        previousMaterials.getLayout().width(14);
        previousMaterials.setText(Component.literal("<"));
        previousMaterials.setOnClick(event -> {
            materialPage--;
            updateMaterialPage();
            event.stopPropagation();
        });
        nextMaterials = navigationButton(width - 16, height - MATERIAL_BAR_HEIGHT + 3,
                "gtpm.multiblock.preview.tooltip.next_materials");
        nextMaterials.getLayout().width(14);
        nextMaterials.setText(Component.literal(">"));
        nextMaterials.setOnClick(event -> {
            materialPage++;
            updateMaterialPage();
            event.stopPropagation();
        });
        // The container survives material changes; per-slot recipe widgets would retain removed slots.
        if (GTPM.MODS.isEMILoaded()) {
            EmiIngredient.recipeItemSlot(materialViewport, () -> getHoveredItem(materialViewport));
            EmiIngredient.recipeItemSlot(predicateViewport, () -> getHoveredItem(predicateViewport));
        }
        if (GTPM.MODS.isJEILoaded()) {
            updateJEIMaterial = JeiIngredient.recipeItemSlot(materialViewport,
                    () -> getHoveredItem(materialViewport));
            updateJEIPredicate = JeiIngredient.recipeItemSlot(predicateViewport,
                    () -> getHoveredItem(predicateViewport));
        }
        addChildren(materialViewport, previousMaterials, nextMaterials);

        focusButton = navigationButton(width - 22, 4, "gtpm.multiblock.preview.tooltip.focus");
        focusButton.setOnClick(event -> {
            state.focusNext();
            rebuildRenderedCore(false);
            updateNavigationText();
            event.stopPropagation();
        });
        addChild(focusButton);

        layerButton = navigationButton(width - 22, 24, "gtpm.multiblock.preview.tooltip.layer");
        layerButton.setOnClick(event -> {
            state.layerNext();
            rebuildRenderedCore(false);
            updateNavigationText();
            event.stopPropagation();
        });
        addChild(layerButton);

        hatchButton = navigationButton(width - 22, 44, "gtpm.multiblock.preview.tooltip.hatches");
        hatchButton.setText(Component.translatable("gtpm.multiblock.preview.button.hatches"));
        hatchButton.setOnClick(event -> {
            state.setHighlightHatches(!state.isHighlightHatches());
            updateNavigationText();
            event.stopPropagation();
        });
        addChild(hatchButton);

        diagnosticLabel = new GTLabelElement(8, sceneHeight - 14, width - 16, 10, Component.empty());
        diagnosticLabel.textStyle(style -> style.adaptiveWidth(false).adaptiveHeight(false).fontSize(8));
        diagnosticLabel.setTextColor(0xffff6060);
        diagnosticLabel.setTextShadow(true);
        diagnosticLabel.setTextWrap(TextWrap.HIDE);
        diagnosticLabel.setAllowHitTest(false);
        addChild(diagnosticLabel);

        predicateViewport.layout(layout -> layout.positionType(TaffyPosition.ABSOLUTE)
                .left(4).top(4).width(18).height(sceneHeight - 8));
        predicateViewport.setOverflowVisible(false);
        predicateViewport.setAllowHitTest(false);
        predicateViewport.setDisplay(false);
        addChild(predicateViewport);

        rebuildPreview(true);
    }

    public void resize(int width, int height) {
        getLayout().width(width).height(height);
        materialViewportWidth = width - 36;
        int sceneHeight = height - (fullscreen ? 0 : MATERIAL_BAR_HEIGHT);
        sceneAspect = (float) width / sceneHeight;
        scene.getLayout().width(width).height(sceneHeight);
        materialViewport.getLayout().top(height - MATERIAL_BAR_HEIGHT + 3).width(materialViewportWidth);
        previousMaterials.getLayout().top(height - MATERIAL_BAR_HEIGHT + 3);
        nextMaterials.getLayout().left(width - 16).top(height - MATERIAL_BAR_HEIGHT + 3);
        focusButton.getLayout().left(width - (fullscreen ? 108 : 22)).top(fullscreen ? 8 : 4);
        layerButton.getLayout().left(width - (fullscreen ? 88 : 22)).top(fullscreen ? 8 : 24);
        hatchButton.getLayout().left(width - (fullscreen ? 68 : 22)).top(fullscreen ? 8 : 44);
        diagnosticLabel.getLayout().top(height - MATERIAL_BAR_HEIGHT - 14).width(width - 16);
        layoutCandidates(height);
        fitCamera();
        updateMaterialPage();
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.fullscreen && !fullscreen && GTPM.MODS.FTB_LIBRARY.isLoaded()) FTBSupport.setSidebarVisible(true);
        this.fullscreen = fullscreen;
        getStyle().backgroundTexture(fullscreen ? IGuiTexture.EMPTY : GuiTextures.BACKGROUND_INVERSE);
    }

    public void setPreview(StructurePreview preview) {
        clearCandidates();
        state.setPreview(preview);
        rebuildPreview(true);
    }

    public void reset(StructurePreview preview) {
        clearCandidates();
        state = new PreviewVisibility(preview);
        materialPage = 0;
        rebuildPreview(true);
    }

    private void rebuildPreview(boolean resetCamera) {
        runOnRenderThread(() -> {
            if (removed) return;
            releaseLoadedBlocks();
            if (sourceLevel == null) {
                sceneCells.clear();
                scene.setLevel(previewLevel);
                scene.setBlocks(LongList.of(), renderHook, resetCamera);
                rebuildMaterials();
                updateNavigationText();
                updateDiagnosticText();
                return;
            }
            sceneCells.clear();
            BlockState controllerState = null;
            var scenePos = new BlockPos.MutableBlockPos();
            for (StructurePreview.Cell cell : state.getPreview().cells()) {
                if (cell.renderState().isAir()) continue;
                long packedPos = BlockPos.offset(cell.relativePos().asLong(), 0, 64, 0);
                scenePos.set(packedPos);
                MultiblockBlockInfo blockInfo = cell.renderInfo().createBlockInfo();
                previewLevel.addBlock(scenePos, blockInfo);
                if (cell.worldPos() != null) {
                    previewLevel.copyMachineAppearance(sourceLevel, cell.worldPos(), scenePos);
                }
                loadedPositions.add(packedPos);
                sceneCells.put(packedPos, cell);
                if (cell.controller()) controllerState = cell.renderState();
            }
            // Scene installs a core filter: attach it only after every block initializer has run.
            scene.setLevel(previewLevel);
            if (controllerState != null && controllerState != cameraControllerState) {
                cameraControllerState = controllerState;
                orientCamera(controllerState);
            }
            rebuildRenderedCore(resetCamera);
            rebuildMaterials();
            updateNavigationText();
            updateDiagnosticText();
        });
    }

    private void orientCamera(BlockState controllerState) {
        if (!(controllerState.getBlock() instanceof MetaMachineBlock block)) return;
        var definition = block.getDefinition();
        var rotation = definition.getRotationState();
        Direction front = rotation == RotationState.NONE ? Direction.NORTH :
                controllerState.getValue(rotation.property);
        Direction upwards = controllerState.hasProperty(GTBlockStateProperties.UPWARDS_FACING) ?
                controllerState.getValue(GTBlockStateProperties.UPWARDS_FACING) : Direction.NORTH;
        Direction up = RelativeDirection.UP.getRelative(front, upwards, false);
        Direction right = RelativeDirection.RIGHT.getRelative(front, upwards, false);
        // Look at the controller's front, right and top in the same coordinate frame as the resolved structure.
        var eye = new Vector3f(front.getStepX(), front.getStepY(), front.getStepZ()).mul(2)
                .add(right.getStepX(), right.getStepY(), right.getStepZ())
                .add(up.getStepX(), up.getStepY(), up.getStepZ());
        scene.setCameraYawAndPitch((float) Math.toDegrees(Math.atan2(eye.z, eye.x)),
                (float) Math.toDegrees(Math.atan2(eye.y, Math.hypot(eye.x, eye.z))));
    }

    private void rebuildRenderedCore(boolean resetCamera) {
        var visible = state.visibleCells();
        hatchPositions.clear();
        var rendered = new LongArrayList(visible.size());
        for (var cell : visible) {
            if (cell.renderState().isAir()) continue;
            long pos = BlockPos.offset(cell.relativePos().asLong(), 0, 64, 0);
            rendered.add(pos);
            if (cell.contributions().stream()
                    .anyMatch(StructurePreview.Contribution::hatchReplaceable)) {
                hatchPositions.add(pos);
            }
        }
        renderedPositions = rendered;
        scene.setBlocks(rendered, renderHook, resetCamera);
        if (resetCamera) fitCamera();
        renderedViewRevision = state.getViewRevision();
    }

    private void fitCamera() {
        if (!renderedPositions.isEmpty()) {
            var center = scene.getCenter();
            double radiusSquared = 0;
            for (int i = 0; i < renderedPositions.size(); i++) {
                long pos = renderedPositions.getLong(i);
                double x = Math.abs(BlockPos.getX(pos) + 0.5 - center.x) + 0.5;
                double y = Math.abs(BlockPos.getY(pos) + 0.5 - center.y) + 0.5;
                double z = Math.abs(BlockPos.getZ(pos) + 0.5 - center.z) + 0.5;
                radiusSquared = Math.max(radiusSquared, x * x + y * y + z * z);
            }
            double halfFov = Math.atan(Math.tan(Math.toRadians(scene.getRenderer().getFov() / 2)) *
                    Math.min(1, sceneAspect));
            scene.setZoom((float) (Math.sqrt(radiusSquared) / Math.sin(halfFov) * 1.1));
        }
    }

    private ItemStack getHoveredItem(UIElement viewport) {
        var ui = getModularUI();
        var hit = ui.ui.rootElement.hitTest(ui.getLastMouseX(), ui.getLastMouseY());
        var hovered = hit == null ? null : hit.getA();
        return hovered instanceof GTItemSlotElement slot && viewport.isAncestorOf(slot) ?
                slot.getValue() : ItemStack.EMPTY;
    }

    public void clearCandidates() {
        if (updateJEIPredicate != null) updateJEIPredicate.accept(ItemStack.EMPTY);
        predicateViewport.clearAllChildren();
        predicateItems.clear();
        predicateViewport.setDisplay(false);
    }

    public void addCandidates(List<ItemStack> items, List<Component> tooltips) {
        if (items.isEmpty()) return;
        var slot = new GTItemSlotElement();
        slot.layout(layout -> layout.positionType(TaffyPosition.ABSOLUTE).width(18).height(18));
        slot.setCanPutItems(false).setCanTakeItems(false).setBackgroundTexture(GuiTextures.SLOT);
        slot.setOnAddedTooltips((element, tips) -> tips.addAll(tooltips));
        slot.setItem(items.getFirst(), false);
        predicateItems.put(slot, items);
        predicateViewport.addChild(slot);
        layoutCandidates((int) getSizeHeight());
        predicateViewport.setDisplay(true);
    }

    private void layoutCandidates(int previewHeight) {
        int rows = Math.max(1, (previewHeight - MATERIAL_BAR_HEIGHT - (fullscreen ? 38 : 8)) / 18);
        int index = 0;
        for (var slot : predicateItems.keySet()) {
            slot.getLayout().left(index / rows * 18).top(index % rows * 18);
            index++;
        }
        predicateViewport.getLayout().left(fullscreen ? 8 : 4).top(fullscreen ? 34 : 4)
                .width(Math.ceilDiv(index, rows) * 18).height(Math.min(index, rows) * 18);
    }

    private void rebuildMaterials() {
        if (updateJEIMaterial != null) updateJEIMaterial.accept(ItemStack.EMPTY);
        materialViewport.clearAllChildren();
        materialSlots.clear();
        materialCandidates = state.getPreview().materials().stream()
                .<List<ItemStack>>map(material -> material.candidates().stream()
                        .map(candidate -> candidate
                                .copyWithCount((int) Math.min(material.required(), Integer.MAX_VALUE)))
                        .collect(ObjectImmutableList.toList()))
                .collect(ObjectImmutableList.toList());
        if (materialCandidates.isEmpty()) {
            updateMaterialPage();
            return;
        }
        for (int index = 0; index < materialCandidates.size(); index++) {
            GTItemSlotElement slot = getSlot(index);
            materialViewport.addChild(slot);
            materialSlots.add(slot);
        }
        updateMaterialIcons(System.currentTimeMillis() / 1000);
        updateMaterialPage();
    }

    private GTItemSlotElement getSlot(int slotIndex) {
        GTItemSlotElement slot = new GTItemSlotElement();
        slot.layout(layout -> {
            layout.positionType(TaffyPosition.ABSOLUTE);
            layout.left(slotIndex * 18);
            layout.top(0);
            layout.width(18);
            layout.height(18);
        });
        slot.setIngredientIO(GTXEIHelper.input());
        slot.setBackgroundTexture(GuiTextures.SLOT);
        slot.setItemCountDecorationXOffset(ClientMultiblockPreviewElement::countDecorationOffset);
        slot.setOnAddedTooltips((element, tooltips) -> addMaterialTooltips(
                state.getPreview().materials().get(slotIndex), tooltips));
        slot.xeiRecipeIngredient();
        return slot;
    }

    private void updateMaterialPage() {
        if (updateJEIMaterial != null) updateJEIMaterial.accept(ItemStack.EMPTY);
        int perPage = Math.max(1, materialViewportWidth / 18);
        int lastPage = Math.max(0, (materialSlots.size() - 1) / perPage);
        materialPage = Math.clamp(materialPage, 0, lastPage);
        int first = materialPage * perPage;
        // Keep every ingredient in the XEI tree; the viewport clips slots outside the current page.
        for (int index = 0; index < materialSlots.size(); index++) {
            materialSlots.get(index).getLayout().left((index - first) * 18);
        }
        materialViewport.getLayout().width(perPage * 18);
        previousMaterials.setActive(materialPage > 0);
        nextMaterials.setActive(materialPage < lastPage);
    }

    private void updateMaterialIcons(long cycle) {
        for (int i = 0; i < materialSlots.size(); i++) {
            var values = materialCandidates.get(i);
            materialSlots.get(i).setItem(values.isEmpty() ? ItemStack.EMPTY : values.get((int) (cycle % values.size())),
                    false);
        }
    }

    private void addMaterialTooltips(StructurePreview.Material material, List<Component> tooltips) {
        tooltips.add(Component.translatable("gtpm.multiblock.preview.material.required", material.required()));
        for (StructurePreview.StructureAmount structure : material.structures()) {
            tooltips.add(Component.translatable("gtpm.multiblock.preview.material.structure",
                    structureName(structure.structureName()), structure.required()));
        }
        if (showAvailability && state.getPreview().availabilityKnown()) {
            if (material.missing() > 0) {
                tooltips.add(Component.translatable("gtpm.multiblock.preview.material.missing", material.missing()));
            }
        }
    }

    private void addCellTooltips(UIEvent event) {
        BlockPosFace hovered = scene.getLastHoverPosFace();
        if (hovered == null) return;
        Cell cell = sceneCells.get(hovered.pos().asLong());
        if (cell == null) return;
        ObjectList<Component> tooltips = new ObjectArrayList<>();
        tooltips.add(cell.renderState().getBlock().getName());
        tooltips.add(Component.translatable("gtpm.multiblock.preview.cell.position",
                cell.relativePos().getX(), cell.relativePos().getY(), cell.relativePos().getZ()));
        for (StructurePreview.Contribution contribution : cell.contributions()) {
            StructurePreview.CellKey key = contribution.cellKey();
            tooltips.add(Component.translatable("gtpm.multiblock.preview.cell.contribution",
                    structureName(contribution.structureName()),
                    contribution.targetState().getBlock().getName(),
                    Component.translatable("gtpm.multiblock.preview.cell.action." +
                            contribution.action().name().toLowerCase(Locale.ROOT)),
                    key.unitIndex(), key.repetition(), key.innerSlice(), key.row(), key.column()));
            Direction requiredDirection = contribution.requiredDirection();
            if (requiredDirection != null) {
                tooltips.add(Component.translatable("gtpm.multiblock.preview.cell.direction",
                        Component.translatable("gtpm.multiblock.preview.direction." + requiredDirection.getName())));
            }
        }
        if (cell.conflict()) {
            tooltips.add(Component.translatable("gtpm.multiblock.preview.cell.conflict"));
        }
        if (cell.demolitionTarget()) {
            tooltips.add(Component.translatable("gtpm.multiblock.preview.cell.demolition"));
        }
        if (cell.contributions().stream().anyMatch(StructurePreview.Contribution::hatchReplaceable)) {
            tooltips.add(Component.translatable("gtpm.multiblock.preview.cell.hatch_replaceable"));
        }
        event.hoverTooltips = new HoverTooltips(tooltips, null, null, ItemStack.EMPTY);
    }

    private void selectCell(BlockPos scenePos) {
        Cell cell = sceneCells.get(scenePos.asLong());
        state.setFocusedStructure(cell.representative().structureName());
        rebuildRenderedCore(false);
        updateNavigationText();
        if (onSelected != null) onSelected.accept(cell);
    }

    private void updateNavigationText() {
        String focus = state.getFocusedStructure();
        focusButton.setText(focus == null ?
                Component.translatable("gtpm.multiblock.preview.button.structure_all") :
                Component.translatable("gtpm.multiblock.preview.button.structure"));
        focusButton.getStyle().tooltips(focus == null ?
                Component.translatable("gtpm.multiblock.preview.tooltip.focus") :
                structureName(focus));
        layerButton.setText(state.isAllLayers() ?
                Component.translatable("gtpm.multiblock.preview.button.layer_all") :
                Component.translatable("gtpm.multiblock.preview.button.layer", state.getVisibleLayer()));
        hatchButton.textStyle(style -> style.textColor(state.isHighlightHatches() ? 0xff167129 : 0xff404040));
    }

    private static Component structureName(String name) {
        return Component.translatable(name.equals(MultiblockControllerMachine.DEFAULT_STRUCTURE) ?
                "gtpm.multiblock.configuration.main" : name);
    }

    private void updateDiagnosticText() {
        diagnosticLabel.setText(state.getPreview().materials().stream().anyMatch(material -> material.missing() > 0) ?
                Component.translatable("gtpm.multiblock.preview.diagnostic.missing_material") : Component.empty());
    }

    private void releaseLoadedBlocks() {
        // Hidden cells still own block entities that must be initialized and released with the whole preview.
        previewLevel.setBlockFilter(null);
        var pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < loadedPositions.size(); i++) {
            pos.set(loadedPositions.getLong(i));
            BlockEntity blockEntity = previewLevel.getBlockEntity(pos);
            if (blockEntity != null && !blockEntity.isRemoved()) blockEntity.setRemoved();
            previewLevel.removeBlock(pos, false);
        }
        loadedPositions.clear();
        hatchPositions.clear();
    }

    @Override
    public void screenTick() {
        super.screenTick();
        long cycle = System.currentTimeMillis() / 1000;
        if (iconCycle != cycle) {
            iconCycle = cycle;
            updateMaterialIcons(cycle);
            for (var entry : predicateItems.object2ObjectEntrySet()) {
                var items = entry.getValue();
                entry.getKey().setItem(items.get((int) (cycle % items.size())), false);
            }
        }
        Level currentLevel = Minecraft.getInstance().level;
        if (currentLevel != sourceLevel) {
            rebuildForLevel(currentLevel);
        } else if (renderedViewRevision != state.getViewRevision()) {
            rebuildRenderedCore(false);
            updateNavigationText();
        }
    }

    @Override
    public void drawBackgroundAdditional(GUIContext context) {
        if (fullscreen && GTPM.MODS.FTB_LIBRARY.isLoaded()) FTBSupport.setSidebarVisible(false);
        if (updateJEIMaterial != null) updateJEIMaterial.accept(getHoveredItem(materialViewport));
        if (updateJEIPredicate != null) updateJEIPredicate.accept(getHoveredItem(predicateViewport));
        super.drawBackgroundAdditional(context);
    }

    private void rebuildForLevel(@Nullable Level currentLevel) {
        runOnRenderThread(() -> {
            if (removed || sourceLevel == currentLevel) return;
            releaseLoadedBlocks();
            sceneCells.clear();
            sourceLevel = currentLevel;
            previewLevel = currentLevel == null ?
                    new MultiblockPreviewLevel() : new MultiblockPreviewLevel(currentLevel);
            if (currentLevel == null) {
                scene.setLevel(previewLevel);
                scene.setBlocks(LongList.of(), renderHook, true);
            } else {
                rebuildPreview(true);
            }
        });
    }

    @Override
    protected void onRemoved() {
        if (fullscreen && GTPM.MODS.FTB_LIBRARY.isLoaded()) FTBSupport.setSidebarVisible(true);
        removed = true;
        releaseLoadedBlocks();
        sourceLevel = null;
        previewLevel = new MultiblockPreviewLevel();
        sceneCells.clear();
        super.onRemoved();
    }

    private static GTButtonElement navigationButton(int x, int y, String tooltipKey) {
        GTButtonElement button = new GTButtonElement(x, y, 18, 18);
        button.getLayout().paddingAll(1);
        button.text.getLayout().marginAll(0).widthPercent(100).minWidth(0);
        button.textStyle(style -> style.adaptiveWidth(false).adaptiveHeight(false).fontSize(8)
                .textColor(0xff404040).textShadow(false).textWrap(TextWrap.HIDE));
        button.setButtonTexture(GuiTextures.BUTTON);
        button.style(style -> style.tooltips(Component.translatable(tooltipKey)));
        return button;
    }

    private static int countDecorationOffset(ItemStack stack) {
        int count = stack.getCount();
        if (count >= 100_000) return 9;
        if (count >= 10_000) return 6;
        if (count >= 1_000) return 3;
        return 0;
    }

    private void renderHatchOutlines() {
        if (!state.isHighlightHatches() || hatchPositions.isEmpty()) return;
        var buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        var lines = buffers.getBuffer(RenderType.lines());
        var pose = new PoseStack();
        for (int i = 0; i < hatchPositions.size(); i++) {
            long pos = hatchPositions.getLong(i);
            double x = BlockPos.getX(pos), y = BlockPos.getY(pos), z = BlockPos.getZ(pos);
            LevelRenderer.renderLineBox(pose, lines, x - 0.002, y - 0.002, z - 0.002,
                    x + 1.002, y + 1.002, z + 1.002, 0.2f, 0.85f, 1.0f, 1.0f);
        }
        buffers.endBatch(RenderType.lines());
    }

    private static void runOnRenderThread(Runnable action) {
        if (RenderSystem.isOnRenderThread()) action.run();
        else RenderSystem.recordRenderCall(action::run);
    }

    private static final class FTBSupport {

        private static void setSidebarVisible(boolean visible) {
            var screen = Minecraft.getInstance().screen;
            if (screen == null) return;
            for (var child : screen.children()) {
                if (child instanceof SidebarGroupGuiButton sidebar) {
                    sidebar.visible = visible;
                    sidebar.active = visible;
                }
            }
        }
    }

    private final class PreviewCellRenderHook implements ISceneBlockRenderHook {

        @Override
        public void applyVertexConsumerWrapper(Level world, BlockPos pos, BlockState state,
                                               WorldSceneRenderer.VertexConsumerWrapper wrapper,
                                               RenderType layer, float partialTicks) {
            Cell cell = sceneCells.get(pos.asLong());
            float red;
            float green;
            float blue;
            if (cell.conflict()) {
                red = 1.0f;
                green = 0.18f;
                blue = 0.92f;
            } else {
                red = green = blue = 1.0f;
            }
            if (!ClientMultiblockPreviewElement.this.state.isFocused(cell)) {
                red *= 0.3f;
                green *= 0.3f;
                blue *= 0.3f;
            }
            wrapper.setColorMultiplier(red, green, blue, 1);
        }
    }
}

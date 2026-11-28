package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.item.IComponentItem;
import com.modularmc.gtpm.api.item.IGTTool;
import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.api.item.tool.ToolHelper;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.common.item.LampBlockItem;
import com.modularmc.gtpm.common.pipelike.cable.Insulation;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.TranslationKey;
import com.modularmc.registrate.util.entry.RegistryEntry;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;

import org.jetbrains.annotations.NotNull;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("Convert2MethodRef")
public class GTCreativeModeTabs {

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUID = REGISTRATE
            .defaultCreativeTab("material_fluid",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_fluid", REGISTRATE))
                            .icon(() -> GTItems.FLUID_CELL.asStack())
                            .title(REGISTRATE.addLocalization(
                                    new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.material_fluid"),
                                    text -> text.source(GTPM.MOD_NAME + " Material Fluid Containers")
                                            .translation(MinecraftLocale.ZH_CN, "格雷科技 | 材料-流体容器")))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEM = REGISTRATE
            .defaultCreativeTab("material_item",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_item", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.ingot, GTMaterials.Aluminium))
                            .title(REGISTRATE.addLocalization(
                                    new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.material_item"),
                                    text -> text.source(GTPM.MOD_NAME + " Material Items")
                                            .translation(MinecraftLocale.ZH_CN, "格雷科技 | 材料-物品")))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCK = REGISTRATE
            .defaultCreativeTab("material_block",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_block", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.block, GTMaterials.Gold))
                            .title(REGISTRATE.addLocalization(
                                    new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.material_block"),
                                    text -> text.source(GTPM.MOD_NAME + " Material Blocks")
                                            .translation(MinecraftLocale.ZH_CN, "格雷科技 | 材料-方块")))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_PIPE = REGISTRATE
            .defaultCreativeTab("material_pipe",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_pipe", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(Insulation.WIRE_DOUBLE.getTagPrefix(), GTMaterials.Copper))
                            .title(REGISTRATE.addLocalization(
                                    new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.material_pipe"),
                                    text -> text.source(GTPM.MOD_NAME + " Material Pipes")
                                            .translation(MinecraftLocale.ZH_CN, "格雷科技 | 材料-管道")))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION = REGISTRATE
            .defaultCreativeTab("decoration",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("decoration", REGISTRATE))
                            .icon(() -> GTBlocks.COIL_CUPRONICKEL.asStack())
                            .title(REGISTRATE.addLocalization(
                                    new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.decoration"),
                                    text -> text.source(GTPM.MOD_NAME + " Decoration Blocks")
                                            .translation(MinecraftLocale.ZH_CN, "格雷科技 | 装饰方块")))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TOOL = REGISTRATE.defaultCreativeTab("tool",
            builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("tool", REGISTRATE))
                    .icon(() -> ToolHelper.get(GTToolType.WRENCH, GTMaterials.Steel))
                    .title(REGISTRATE.addLocalization(new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.tool"),
                            text -> text.source(GTPM.MOD_NAME + " Tools")
                                    .translation(MinecraftLocale.ZH_CN, "格雷科技 | 工具")))
                    .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINE = REGISTRATE.defaultCreativeTab("machine",
            builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("machine", REGISTRATE))
                    .icon(() -> GTMachines.ELECTROLYZER[GTValues.LV].asStack())
                    .title(REGISTRATE.addLocalization(new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.machine"),
                            text -> text.source(GTPM.MOD_NAME + " Machines")
                                    .translation(MinecraftLocale.ZH_CN, "格雷科技 | 机器")))
                    .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> ITEM = REGISTRATE.defaultCreativeTab("item",
            builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("item", REGISTRATE))
                    .icon(() -> GTItems.BASIC_TAPE.asStack())
                    .title(REGISTRATE.addLocalization(new TranslationKey(GTPM.MOD_ID, "itemGroup.gtpm.item"),
                            text -> text.source(GTPM.MOD_NAME + " Items")
                                    .translation(MinecraftLocale.ZH_CN, "格雷科技 | 物品")))
                    .build())
            .register();

    public static void init() {}

    public record RegistrateDisplayItemsGenerator(String name, GTRegistrate registrate)
            implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(@NotNull CreativeModeTab.ItemDisplayParameters itemDisplayParameters,
                           @NotNull CreativeModeTab.Output output) {
            var tab = registrate.get(name, Registries.CREATIVE_MODE_TAB);
            for (var entry : registrate.getAll(Registries.ITEM)) {
                if (!registrate.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                switch (item) {
                    case IComponentItem componentItem -> {
                        NonNullList<ItemStack> list = NonNullList.create();
                        componentItem.fillItemCategory(tab.get(), list);
                        list.forEach(output::accept);
                    }
                    case IGTTool tool -> {
                        NonNullList<ItemStack> list = NonNullList.create();
                        tool.definition$fillItemCategory(tab.get(), list);
                        list.forEach(output::accept);
                    }
                    case LampBlockItem lamp -> {
                        NonNullList<ItemStack> list = NonNullList.create();
                        lamp.fillItemCategory(tab.get(), list);
                        list.forEach(output::accept);
                    }
                    default -> output.accept(item);
                }
            }
        }
    }
}

package com.modularmclib.gtpm.common.data;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmclib.gtpm.api.data.tag.TagPrefix;
import com.modularmclib.gtpm.api.item.IComponentItem;
import com.modularmclib.gtpm.api.item.IGTTool;
import com.modularmclib.gtpm.api.item.tool.GTToolType;
import com.modularmclib.gtpm.api.item.tool.ToolHelper;
import com.modularmclib.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmclib.gtpm.common.item.LampBlockItem;
import com.modularmclib.gtpm.common.pipelike.cable.Insulation;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;

import com.tterrag.registrate.util.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import static com.modularmclib.gtpm.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("Convert2MethodRef")
public class GTCreativeModeTabs {

    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_FLUID = REGISTRATE
            .defaultCreativeTab("material_fluid",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_fluid", REGISTRATE))
                            .icon(() -> GTItems.FLUID_CELL.asStack())
                            .title(REGISTRATE.addLang("itemGroup", GTPM.id("material_fluid"),
                                    GTPM.MOD_NAME + " Material Fluid Containers"))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_ITEM = REGISTRATE
            .defaultCreativeTab("material_item",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_item", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.ingot, GTMaterials.Aluminium))
                            .title(REGISTRATE.addLang("itemGroup", GTPM.id("material_item"),
                                    GTPM.MOD_NAME + " Material Items"))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_BLOCK = REGISTRATE
            .defaultCreativeTab("material_block",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_block", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(TagPrefix.block, GTMaterials.Gold))
                            .title(REGISTRATE.addLang("itemGroup", GTPM.id("material_block"),
                                    GTPM.MOD_NAME + " Material Blocks"))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MATERIAL_PIPE = REGISTRATE
            .defaultCreativeTab("material_pipe",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("material_pipe", REGISTRATE))
                            .icon(() -> ChemicalHelper.get(Insulation.WIRE_DOUBLE.getTagPrefix(), GTMaterials.Copper))
                            .title(REGISTRATE.addLang("itemGroup", GTPM.id("material_pipe"),
                                    GTPM.MOD_NAME + " Material Pipes"))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> DECORATION = REGISTRATE
            .defaultCreativeTab("decoration",
                    builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("decoration", REGISTRATE))
                            .icon(() -> GTBlocks.COIL_CUPRONICKEL.asStack())
                            .title(REGISTRATE.addLang("itemGroup", GTPM.id("decoration"),
                                    GTPM.MOD_NAME + " Decoration Blocks"))
                            .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> TOOL = REGISTRATE.defaultCreativeTab("tool",
            builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("tool", REGISTRATE))
                    .icon(() -> ToolHelper.get(GTToolType.WRENCH, GTMaterials.Steel))
                    .title(REGISTRATE.addLang("itemGroup", GTPM.id("tool"), GTPM.MOD_NAME + " Tools"))
                    .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> MACHINE = REGISTRATE.defaultCreativeTab("machine",
            builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("machine", REGISTRATE))
                    .icon(() -> GTMachines.ELECTROLYZER[GTValues.LV].asStack())
                    .title(REGISTRATE.addLang("itemGroup", GTPM.id("machine"), GTPM.MOD_NAME + " Machines"))
                    .build())
            .register();
    public static RegistryEntry<CreativeModeTab, CreativeModeTab> ITEM = REGISTRATE.defaultCreativeTab("item",
            builder -> builder.displayItems(new RegistrateDisplayItemsGenerator("item", REGISTRATE))
                    .icon(() -> GTItems.BASIC_TAPE.asStack())
                    .title(REGISTRATE.addLang("itemGroup", GTPM.id("item"), GTPM.MOD_NAME + " Items"))
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

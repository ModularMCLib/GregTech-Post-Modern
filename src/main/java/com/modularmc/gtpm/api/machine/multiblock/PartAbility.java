package com.modularmc.gtpm.api.machine.multiblock;

import com.modularmc.gtpm.utils.memoization.GTMemoizer;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.Supplier;

@TransVariants(keyPrefix = "gtpm.multiblock.ability",
               value = {
                       @Variant(id = "coke_oven_hatch",
                                source = "Coke oven hatch",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "焦炉舱")),
                       @Variant(id = "computation_data_reception",
                                source = "Computation reception",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "算力接收")),
                       @Variant(id = "computation_data_transmission",
                                source = "Computation transmission",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "算力传输")),
                       @Variant(id = "data_access",
                                source = "Data access",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "数据访问")),
                       @Variant(id = "export_fluids",
                                source = "Fluid output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体输出")),
                       @Variant(id = "export_fluids_1x",
                                source = "Fluid output (1 tank)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "单仓流体输出")),
                       @Variant(id = "export_fluids_4x",
                                source = "Fluid output (4 tanks)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "四仓流体输出")),
                       @Variant(id = "export_fluids_9x",
                                source = "Fluid output (9 tanks)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "九仓流体输出")),
                       @Variant(id = "export_items",
                                source = "Item output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品输出")),
                       @Variant(id = "hpca_component",
                                source = "HPCA component",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高性能计算组件")),
                       @Variant(id = "import_fluids",
                                source = "Fluid input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体输入")),
                       @Variant(id = "import_fluids_1x",
                                source = "Fluid input (1 tank)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "单仓流体输入")),
                       @Variant(id = "import_fluids_4x",
                                source = "Fluid input (4 tanks)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "四仓流体输入")),
                       @Variant(id = "import_fluids_9x",
                                source = "Fluid input (9 tanks)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "九仓流体输入")),
                       @Variant(id = "import_items",
                                source = "Item input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品输入")),
                       @Variant(id = "input_energy",
                                source = "Energy input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能源输入")),
                       @Variant(id = "input_laser",
                                source = "Laser input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "激光输入")),
                       @Variant(id = "maintenance",
                                source = "Maintenance",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "维护")),
                       @Variant(id = "muffler",
                                source = "Muffler",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消声仓")),
                       @Variant(id = "object_holder",
                                source = "Object holder",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品容器")),
                       @Variant(id = "optical_data_reception",
                                source = "Optical data reception",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "光学数据接收")),
                       @Variant(id = "optical_data_transmission",
                                source = "Optical data transmission",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "光学数据传输")),
                       @Variant(id = "output_energy",
                                source = "Energy output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能源输出")),
                       @Variant(id = "output_laser",
                                source = "Laser output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "激光输出")),
                       @Variant(id = "parallel_hatch",
                                source = "Parallel hatch",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "并行控制仓")),
                       @Variant(id = "passthrough_hatch",
                                source = "Passthrough",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "贯通仓")),
                       @Variant(id = "pump_fluid_hatch",
                                source = "Pump fluid hatch",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泵流体舱")),
                       @Variant(id = "rotor_holder",
                                source = "Rotor holder",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "转子座")),
                       @Variant(id = "steam",
                                source = "Steam input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "蒸汽输入")),
                       @Variant(id = "steam_export_items",
                                source = "Steam item output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "蒸汽物品输出")),
                       @Variant(id = "steam_import_items",
                                source = "Steam item input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "蒸汽物品输入")),
                       @Variant(id = "substation_input_energy",
                                source = "Substation energy input",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "变电站能源输入")),
                       @Variant(id = "substation_output_energy",
                                source = "Substation energy output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "变电站能源输出")),
                       @Variant(id = "tank_valve",
                                source = "Tank valve",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "储罐阀门")),
               })
public class PartAbility {

    private static final Map<String, PartAbility> REGISTRY = new LinkedHashMap<>();

    public static final PartAbility EXPORT_ITEMS = new PartAbility("export_items");
    public static final PartAbility IMPORT_ITEMS = new PartAbility("import_items");
    public static final PartAbility EXPORT_FLUIDS = new PartAbility("export_fluids");
    public static final PartAbility IMPORT_FLUIDS = new PartAbility("import_fluids");

    public static final PartAbility EXPORT_FLUIDS_1X = new PartAbility("export_fluids_1x");
    public static final PartAbility IMPORT_FLUIDS_1X = new PartAbility("import_fluids_1x");
    public static final PartAbility EXPORT_FLUIDS_4X = new PartAbility("export_fluids_4x");
    public static final PartAbility IMPORT_FLUIDS_4X = new PartAbility("import_fluids_4x");
    public static final PartAbility EXPORT_FLUIDS_9X = new PartAbility("export_fluids_9x");
    public static final PartAbility IMPORT_FLUIDS_9X = new PartAbility("import_fluids_9x");

    public static final PartAbility INPUT_ENERGY = new PartAbility("input_energy");
    public static final PartAbility OUTPUT_ENERGY = new PartAbility("output_energy");
    public static final PartAbility SUBSTATION_INPUT_ENERGY = new PartAbility("substation_input_energy");
    public static final PartAbility SUBSTATION_OUTPUT_ENERGY = new PartAbility("substation_output_energy");
    public static final PartAbility ROTOR_HOLDER = new PartAbility("rotor_holder");
    public static final PartAbility PUMP_FLUID_HATCH = new PartAbility("pump_fluid_hatch");
    public static final PartAbility STEAM = new PartAbility("steam");
    public static final PartAbility STEAM_IMPORT_ITEMS = new PartAbility("steam_import_items");
    public static final PartAbility STEAM_EXPORT_ITEMS = new PartAbility("steam_export_items");
    public static final PartAbility COKE_OVEN_HATCH = new PartAbility("coke_oven_hatch");
    public static final PartAbility MAINTENANCE = new PartAbility("maintenance");
    public static final PartAbility MUFFLER = new PartAbility("muffler");
    public static final PartAbility TANK_VALVE = new PartAbility("tank_valve");
    public static final PartAbility PASSTHROUGH_HATCH = new PartAbility("passthrough_hatch");
    public static final PartAbility PARALLEL_HATCH = new PartAbility("parallel_hatch");
    public static final PartAbility INPUT_LASER = new PartAbility("input_laser");
    public static final PartAbility OUTPUT_LASER = new PartAbility("output_laser");

    public static final PartAbility COMPUTATION_DATA_RECEPTION = new PartAbility("computation_data_reception");
    public static final PartAbility COMPUTATION_DATA_TRANSMISSION = new PartAbility("computation_data_transmission");
    public static final PartAbility OPTICAL_DATA_RECEPTION = new PartAbility("optical_data_reception");
    public static final PartAbility OPTICAL_DATA_TRANSMISSION = new PartAbility("optical_data_transmission");

    public static final PartAbility DATA_ACCESS = new PartAbility("data_access");

    public static final PartAbility HPCA_COMPONENT = new PartAbility("hpca_component");
    public static final PartAbility OBJECT_HOLDER = new PartAbility("object_holder");

    /**
     * tier -> available blocks
     */
    private final Int2ObjectMap<Set<Block>> registry = new Int2ObjectOpenHashMap<>();

    private final Supplier<Collection<Block>> allBlocks = GTMemoizer
            .memoize(() -> registry.values().stream().flatMap(Collection::stream).toList());

    @Getter
    private final String name;

    public PartAbility(String name) {
        this.name = name;
        PartAbility previous = REGISTRY.putIfAbsent(name, this);
        if (previous != null) {
            throw new IllegalArgumentException("Duplicate part ability name: " + name);
        }
    }

    public static Optional<PartAbility> byName(String name) {
        return Optional.ofNullable(REGISTRY.get(name));
    }

    public static Collection<PartAbility> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    public void register(int tier, Block block) {
        registry.computeIfAbsent(tier, T -> new HashSet<>()).add(block);
    }

    public Collection<Block> getAllBlocks() {
        return allBlocks.get();
    }

    public boolean isApplicable(Block block) {
        return getAllBlocks().contains(block);
    }

    public Collection<Block> getBlocks(int... tiers) {
        return registry.int2ObjectEntrySet().stream()
                .filter(entry -> ArrayUtils.contains(tiers, entry.getIntKey()))
                .flatMap(entry -> entry.getValue().stream())
                .toList();
    }

    /**
     * [from, to]
     */
    public Collection<Block> getBlockRange(int from, int to) {
        return registry.int2ObjectEntrySet().stream()
                .filter(entry -> entry.getIntKey() <= to && entry.getIntKey() >= from)
                .flatMap(entry -> entry.getValue().stream())
                .toList();
    }
}

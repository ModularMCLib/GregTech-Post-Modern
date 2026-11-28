package com.modularmc.gtpm.common.block;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.ActiveBlock;
import com.modularmc.gtpm.api.block.ICoilType;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@TransVariants(keyPrefix = "block.gtpm.wire_coil",
               value = {
                       @Variant(id = "tooltip_cracking",
                                source = "§8Cracking Unit:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8裂化机：")),
                       @Variant(id = "tooltip_energy_cracking",
                                source = "  §aEnergy Usage: §f%s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a耗能：§f%s%%")),
                       @Variant(id = "tooltip_energy_smelter",
                                source = "  §aEnergy Usage: §f%s EU/t §8per recipe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  §a功耗：§8每个配方 §f%s EU/t")),
                       @Variant(id = "tooltip_extended_info",
                                source = "§7Hold SHIFT to show Coil Bonus Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7按住§6SHIFT§7获得更多线圈信息")),
                       @Variant(id = "tooltip_heat",
                                source = "§cBase Heat Capacity: §f%d K",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c基础热容：§f%d K")),
                       @Variant(id = "tooltip_parallel_smelter",
                                source = "  §5Max Parallel: §f%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§5最大并行：§f%s")),
                       @Variant(id = "tooltip_pyro",
                                source = "§8Pyrolyse Oven:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8热解炉：")),
                       @Variant(id = "tooltip_smelter",
                                source = "§8Multi Smelter:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8工业熔炉：")),
                       @Variant(id = "tooltip_speed_pyro",
                                source = "  §bProcessing Speed: §f%s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b处理速度：§f%s%%")),
               })
public class CoilBlock extends ActiveBlock {

    public ICoilType coilType;

    public CoilBlock(Properties properties, ICoilType coilType) {
        super(properties);
        this.coilType = coilType;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (GTUtil.isShiftDown()) {
            int coilTier = coilType.getTier();
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_heat", coilType.getCoilTemperature()));
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_smelter"));
            tooltip.add(
                    Component.translatable("block.gtpm.wire_coil.tooltip_parallel_smelter", coilType.getLevel() * 32));
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_energy_smelter",
                    Math.max(1, 16 / coilType.getEnergyDiscount())));
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_pyro"));
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_speed_pyro",
                    coilTier == 0 ? 75 : 50 * (coilTier + 1)));
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_cracking"));
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_energy_cracking", 100 - 10 * coilTier));
        } else {
            tooltip.add(Component.translatable("block.gtpm.wire_coil.tooltip_extended_info"));
        }
    }

    public enum CoilType implements StringRepresentable, ICoilType {

        // spotless:off
        CUPRONICKEL("cupronickel", 1800, 1, 1, GTMaterials.Cupronickel, GTPM.id("block/casings/coils/machine_coil_cupronickel")),
        KANTHAL("kanthal", 2700, 2, 1, GTMaterials.Kanthal, GTPM.id("block/casings/coils/machine_coil_kanthal")),
        NICHROME("nichrome", 3600, 2, 2, GTMaterials.Nichrome, GTPM.id("block/casings/coils/machine_coil_nichrome")),
        RTMALLOY("rtm_alloy", 4500, 4, 2, GTMaterials.RTMAlloy, GTPM.id("block/casings/coils/machine_coil_rtm_alloy")),
        HSSG("hssg", 5400, 4, 4, GTMaterials.HSSG, GTPM.id("block/casings/coils/machine_coil_hssg")),
        NAQUADAH("naquadah", 7200, 8, 4, GTMaterials.Naquadah, GTPM.id("block/casings/coils/machine_coil_naquadah")),
        TRINIUM("trinium", 9001, 8, 8, GTMaterials.Trinium, GTPM.id("block/casings/coils/machine_coil_trinium")),
        TRITANIUM("tritanium", 10800, 16, 8, GTMaterials.Tritanium, GTPM.id("block/casings/coils/machine_coil_tritanium"));
        // spotless:on
        @NotNull
        @Getter
        private final String name;
        // electric blast furnace properties
        @Getter
        private final int coilTemperature;
        // multi smelter properties
        @Getter
        private final int level;
        @Getter
        private final int energyDiscount;
        @NotNull
        @Getter
        private final Material material;
        @NotNull
        @Getter
        private final ResourceLocation texture;

        CoilType(String name, int coilTemperature, int level, int energyDiscount, Material material,
                 ResourceLocation texture) {
            this.name = name;
            this.coilTemperature = coilTemperature;
            this.level = level;
            this.energyDiscount = energyDiscount;
            this.material = material;
            this.texture = texture;
        }

        public int getTier() {
            return this.ordinal();
        }

        @NotNull
        @Override
        public String toString() {
            return getName();
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return name;
        }
    }
}

package com.modularmc.gtpm.api.item.tool;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.item.IGTTool;
import com.modularmc.gtpm.api.registry.registrate.provider.GTLanguageProvider;
import com.modularmc.gtpm.api.sound.ExistingSoundEntry;
import com.modularmc.gtpm.api.sound.SoundEntry;
import com.modularmc.gtpm.common.data.GTSoundEntries;
import com.modularmc.gtpm.common.data.item.GTItemAbilities;
import com.modularmc.gtpm.common.item.tool.behavior.*;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.data.recipe.CustomTags;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
import com.modularmc.registrate.providers.core.ProviderType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.Tags;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Trans(key = "behaviour.boor.by",
       source = "by %s",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "通过%s"))
@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "tool.class.axe",
                                source = "Axe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "斧")),
                       @Variant(id = "tool.class.butchery_knife",
                                source = "Butchery Knife",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "屠刀")),
                       @Variant(id = "tool.class.crowbar",
                                source = "Crowbar",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "撬棍")),
                       @Variant(id = "tool.class.drill",
                                source = "Drill",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电钻")),
                       @Variant(id = "tool.class.file",
                                source = "File",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锉")),
                       @Variant(id = "tool.class.hammer",
                                source = "Hammer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锤")),
                       @Variant(id = "tool.class.hoe",
                                source = "Hoe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锄")),
                       @Variant(id = "tool.class.knife",
                                source = "Knife",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "刀")),
                       @Variant(id = "tool.class.mallet",
                                source = "Soft Mallet",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "软锤")),
                       @Variant(id = "tool.class.mining_hammer",
                                source = "Mining Hammer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "采矿锤")),
                       @Variant(id = "tool.class.mortar",
                                source = "Mortar",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研钵")),
                       @Variant(id = "tool.class.pickaxe",
                                source = "Pickaxe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "镐")),
                       @Variant(id = "tool.class.plunger",
                                source = "Plunger",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搋子")),
                       @Variant(id = "tool.class.rolling_pin",
                                source = "Rolling Pin",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "擀面杖")),
                       @Variant(id = "tool.class.saw",
                                source = "Saw",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锯")),
                       @Variant(id = "tool.class.screwdriver",
                                source = "Screwdriver",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "螺丝刀")),
                       @Variant(id = "tool.class.scythe",
                                source = "Scythe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "镰刀")),
                       @Variant(id = "tool.class.shears",
                                source = "Shears",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "剪刀")),
                       @Variant(id = "tool.class.shovel",
                                source = "Shovel",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锹")),
                       @Variant(id = "tool.class.spade",
                                source = "Spade",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "铲")),
                       @Variant(id = "tool.class.sword",
                                source = "Sword",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "剑")),
                       @Variant(id = "tool.class.wire_cutter",
                                source = "Wire Cutter",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "剪线钳")),
                       @Variant(id = "tool.class.wrench",
                                source = "Wrench",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "扳手")),
                       @Variant(id = "tool_action.crowbar",
                                source = "§8Use Crowbar to remove Covers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用撬棍卸下覆盖板")),
                       @Variant(id = "tool_action.hammer",
                                source = "§8Use Hard Hammer to muffle Sounds",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用锻造锤消除声响")),
                       @Variant(id = "tool_action.screwdriver.access_covers",
                                source = "§8Use Screwdriver to access Covers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用螺丝刀访问覆盖板")),
                       @Variant(id = "tool_action.screwdriver.auto_collapse",
                                source = "§8Use Screwdriver to toggle Item collapsing",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§8使用螺丝刀开关物品堆叠自动合并")),
                       @Variant(id = "tool_action.screwdriver.auto_output",
                                source = "§8Use Screwdriver to toggle Auto-Output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用螺丝刀切换自动输出")),
                       @Variant(id = "tool_action.screwdriver.auto_output_covers",
                                source = "§8Use Screwdriver to Allow Input from Output Side or access Covers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§8使用螺丝刀设置允许从输出端输入或访问覆盖板")),
                       @Variant(id = "tool_action.screwdriver.toggle_mode",
                                source = "§8Use Screwdriver to toggle Modes",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用螺丝刀切换模式")),
                       @Variant(id = "tool_action.screwdriver.toggle_mode_covers",
                                source = "§8Use Screwdriver to toggle Modes or access Covers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§8使用螺丝刀切换模式或访问覆盖板")),
                       @Variant(id = "tool_action.soft_mallet.reset",
                                source = "§8Use Soft Mallet to toggle Working",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用软锤开关机器")),
                       @Variant(id = "tool_action.soft_mallet.toggle_mode",
                                source = "§8Use Soft Mallet to toggle Modes",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用软锤切换模式")),
                       @Variant(id = "tool_action.tape",
                                source = "§8Use Tape to fix Maintenance Problems",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用胶带维护机器")),
                       @Variant(id = "tool_action.wire_cutter.connect",
                                source = "§8Use Wire Cutters to set Connections",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用剪线钳连接/断开电线")),
                       @Variant(id = "tool_action.wrench.connect",
                                source = "§8Use Wrench to set Connections, sneak to block Connections",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§8使用扳手连接/断开管道，潜行可以阻止输入")),
                       @Variant(id = "tool_action.wrench.set_facing",
                                source = "§8Use Wrench to set Facing",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8使用扳手设置朝向")),
               })
public class GTToolType {

    @Getter
    private static final Map<String, GTToolType> types = new HashMap<>();

    // FIXME the speed and breakability values are all broken. refer to (vanilla?) for correct-er values.
    public static final GTToolType SWORD = GTToolType.builder("sword")
            .localization(text -> text.source("%s Sword")
                    .translation(MinecraftLocale.ZH_CN, "%s剑"))
            .toolTag(ToolItemTagType.MATCH, ItemTags.SWORDS)
            .definition(b -> b.tool(new Tool(List.of(
                    Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F),
                    Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F)),
                    1.0F, 1))
                    .attacking().attackDamage(3.0F).attackSpeed(-2.4F))
            .toolClassNames("sword")
            .defaultAbilities(ItemAbilities.DEFAULT_SWORD_ACTIONS)
            .materialAmount(2 * GTValues.M)
            .build();
    public static final GTToolType PICKAXE = GTToolType.builder("pickaxe")
            .localization(text -> text.source("%s Pickaxe")
                    .translation(MinecraftLocale.ZH_CN, "%s镐"))
            .toolTag(ToolItemTagType.MATCH, ItemTags.PICKAXES)
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .definition(b -> b.blockBreaking().attackDamage(1.0F).attackSpeed(-2.8F)
                    .behaviors(TorchPlaceBehavior.INSTANCE))
            .toolClassNames("pickaxe")
            .defaultAbilities(ItemAbilities.DEFAULT_PICKAXE_ACTIONS)
            .materialAmount(3 * GTValues.M)
            .build();
    public static final GTToolType SHOVEL = GTToolType.builder("shovel")
            .localization(text -> text.source("%s Shovel")
                    .translation(MinecraftLocale.ZH_CN, "%s锹"))
            .toolTag(ToolItemTagType.MATCH, ItemTags.SHOVELS)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .definition(b -> b.blockBreaking().attackDamage(1.5F)
                    .behaviors(GrassPathBehavior.INSTANCE))
            .toolClassNames("shovel")
            .materialAmount(GTValues.M)
            .defaultAbilities(ItemAbilities.SHOVEL_DIG, ItemAbilities.SHOVEL_DOUSE)
            .build();
    public static final GTToolType AXE = GTToolType.builder("axe")
            .localization(text -> text.source("%s Axe")
                    .translation(MinecraftLocale.ZH_CN, "%s斧"))
            .toolTag(ToolItemTagType.MATCH, ItemTags.AXES)
            .harvestTag(BlockTags.MINEABLE_WITH_AXE)
            .definition(b -> b.blockBreaking()
                    .attackDamage(5.0F).baseEfficiency(2.0F).attackSpeed(-3.2F)
                    .behaviors(DisableShieldBehavior.INSTANCE, TreeFellingBehavior.INSTANCE,
                            LogStripBehavior.INSTANCE, ScrapeBehavior.INSTANCE, WaxOffBehavior.INSTANCE))
            .toolClassNames("axe")
            .materialAmount(3 * GTValues.M)
            .defaultAbilities(ItemAbilities.AXE_DIG)
            .build();
    @Trans(key = "behaviour.hoe",
           source = "Can till dirt",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可以翻耕泥土"))
    public static final GTToolType HOE = GTToolType.builder("hoe")
            .localization(text -> text.source("%s Hoe")
                    .translation(MinecraftLocale.ZH_CN, "%s锄"))
            .toolTag(ToolItemTagType.MATCH, ItemTags.HOES)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.cannotAttack().behaviors(HoeGroundBehavior.INSTANCE))
            .toolClassNames("hoe")
            .defaultAbilities(ItemAbilities.HOE_DIG)
            .materialAmount(2 * GTValues.M)
            .build();

    public static final GTToolType MINING_HAMMER = GTToolType.builder("mining_hammer")
            .localization(text -> text.source("%s Mining Hammer")
                    .translation(MinecraftLocale.ZH_CN, "%s采矿锤"))
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_MINING_HAMMER)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .definition(b -> b.blockBreaking().aoe(1, 1, 0)
                    .efficiencyMultiplier(0.4F).attackDamage(1.5F).durabilityMultiplier(3.0F).attackSpeed(-3.2F)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, TorchPlaceBehavior.INSTANCE))
            .toolClasses(GTToolType.PICKAXE)
            .defaultAbilities(ItemAbilities.DEFAULT_PICKAXE_ACTIONS)
            .materialAmount(6 * GTValues.M)
            .build();
    public static final GTToolType SPADE = GTToolType.builder("spade")
            .localization(text -> text.source("%s Spade")
                    .translation(MinecraftLocale.ZH_CN, "%s铲"))
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SPADE)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .definition(b -> b.blockBreaking().aoe(1, 1, 0)
                    .efficiencyMultiplier(0.4F).attackDamage(1.5F).attackSpeed(-3.2F)
                    .durabilityMultiplier(3.0F)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, GrassPathBehavior.INSTANCE))
            .toolClasses(GTToolType.SHOVEL)
            .defaultAbilities(ItemAbilities.SHOVEL_DIG, ItemAbilities.SHOVEL_DOUSE)
            .materialAmount(3 * GTValues.M)
            .build();
    public static final GTToolType SCYTHE = GTToolType.builder("scythe")
            .localization(text -> text.source("%s Scythe")
                    .translation(MinecraftLocale.ZH_CN, "%s镰刀"))
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SCYTHE)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking().attacking()
                    .attackDamage(5.0F).durabilityMultiplier(3.0F).attackSpeed(-3.0F)
                    .aoe(2, 2, 2)
                    .behaviors(HoeGroundBehavior.INSTANCE, HarvestCropsBehavior.INSTANCE)
                    .validEnchantmentTags(ItemTags.DURABILITY_ENCHANTABLE))
            .toolClassNames("scythe")
            .toolClasses(GTToolType.HOE)
            .defaultAbilities(ItemAbilities.HOE_DIG)
            .materialAmount(3 * GTValues.M)
            .build();

    public static final GTToolType SAW = GTToolType.builder("saw")
            .localization(text -> text.source("%s Saw")
                    .translation(MinecraftLocale.ZH_CN, "%s锯"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_SAWS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SAW)
            .harvestTag(CustomTags.MINEABLE_WITH_SAW)
            .definition(b -> b.crafting()
                    .damagePerCraftingAction(2).attackDamage(-1.0F).attackSpeed(-2.6F)
                    .behaviors(HarvestIceBehavior.INSTANCE))
            .sound(GTSoundEntries.SAW_TOOL)
            .symbol('s')
            .defaultAbilities(GTItemAbilities.SAW_DIG)
            .materialAmount(2 * GTValues.M)
            .build();
    @Trans(key = "behaviour.hammer",
           source = "Turns on and off Muffling for Machines (by hitting them)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击机器以开启或关闭消声功能"))
    public static final GTToolType HARD_HAMMER = GTToolType.builder("hammer")
            .localization(text -> text.source("%s Hammer")
                    .translation(MinecraftLocale.ZH_CN, "%s锻造锤"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_HAMMERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_HAMMER)
            .harvestTag(CustomTags.MINEABLE_WITH_HAMMER)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .definition(b -> b.blockBreaking().crafting().damagePerCraftingAction(2)
                    .attackDamage(1.0F).attackSpeed(-2.8F)
                    .behaviors(new EntityDamageBehavior(2.0F, CustomTags.IRON_GOLEMS), ProspectingBehavior.INSTANCE))
            .sound(GTSoundEntries.FORGE_HAMMER)
            .symbol('h')
            .toolClasses(GTToolType.PICKAXE)
            .defaultAbilities(ItemAbilities.DEFAULT_PICKAXE_ACTIONS)
            .defaultAbilities(GTItemAbilities.DEFAULT_HAMMER_ACTIONS)
            .materialAmount(6 * GTValues.M)
            .build();
    @Trans(key = "behaviour.soft_hammer",
           source = "Activates and Deactivates Machines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用来开启与关闭机器"))
    public static final GTToolType SOFT_MALLET = GTToolType.builder("mallet")
            .localization(text -> text.source("%s Soft Mallet")
                    .translation(MinecraftLocale.ZH_CN, "%s软锤"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_MALLETS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_MALLET)
            .definition(b -> b.crafting().cannotAttack().attackSpeed(-2.4F).sneakBypassUse()
                    .behaviors(ToolModeSwitchBehavior.INSTANCE))
            .sound(GTSoundEntries.SOFT_MALLET_TOOL)
            .symbol('r')
            .defaultAbilities(GTItemAbilities.DEFAULT_MALLET_ACTIONS, GTItemAbilities.INTERACT_WITH_COVER)
            .materialAmount(6 * GTValues.M)
            .build();
    @Trans(key = "behaviour.wrench",
           source = "Rotates Blocks on Rightclick",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击以旋转方块"))
    public static final GTToolType WRENCH = GTToolType.builder("wrench")
            .localization(text -> text.source("%s Wrench")
                    .translation(MinecraftLocale.ZH_CN, "%s扳手"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WRENCHES)
            .toolTag(Tags.Items.TOOLS_WRENCH)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_WRENCH)
            .harvestTag(CustomTags.MINEABLE_WITH_WRENCH)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .attackDamage(1.0F).attackSpeed(-2.8F)
                    .behaviors(BlockRotatingBehavior.INSTANCE,
                            new EntityDamageBehavior(3.0F, CustomTags.IRON_GOLEMS),
                            ToolModeSwitchBehavior.INSTANCE))
            .sound(GTSoundEntries.WRENCH_TOOL, true)
            .symbol('w')
            .defaultAbilities(GTItemAbilities.WRENCH_DIG, GTItemAbilities.WRENCH_DISMANTLE,
                    GTItemAbilities.WRENCH_CONNECT)
            .materialAmount(4 * GTValues.M)
            .build();
    public static final GTToolType FILE = GTToolType.builder("file")
            .localization(text -> text.source("%s File")
                    .translation(MinecraftLocale.ZH_CN, "%s锉"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_FILES)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_FILE)
            .definition(b -> b.crafting().cannotAttack()
                    .damagePerCraftingAction(4).attackSpeed(-2.4F))
            .sound(GTSoundEntries.FILE_TOOL)
            .symbol('f')
            .materialAmount(2 * GTValues.M)
            .build();
    public static final GTToolType CROWBAR = GTToolType.builder("crowbar")
            .localization(text -> text.source("%s Crowbar")
                    .translation(MinecraftLocale.ZH_CN, "%s撬棍"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_CROWBARS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_CROWBAR)
            .harvestTag(CustomTags.MINEABLE_WITH_CROWBAR)
            .definition(b -> b.blockBreaking().crafting()
                    .attackDamage(2.0F).attackSpeed(-2.4F)
                    .sneakBypassUse().behaviors(RotateRailBehavior.INSTANCE))
            .sound(new ExistingSoundEntry(SoundEvents.ITEM_BREAK, SoundSource.BLOCKS), true)
            .symbol('c')
            .defaultAbilities(GTItemAbilities.CROWBAR_DIG, GTItemAbilities.CROWBAR_REMOVE_COVER)
            .materialAmount(3 * GTValues.M / 2)
            .build();
    public static final GTToolType SCREWDRIVER = GTToolType.builder("screwdriver")
            .localization(text -> text.source("%s Screwdriver")
                    .translation(MinecraftLocale.ZH_CN, "%s螺丝刀"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_SCREWDRIVERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SCREWDRIVER)
            .definition(b -> b.crafting().damagePerCraftingAction(4).sneakBypassUse()
                    .attackDamage(-1.0F).attackSpeed(3.0F).efficiencyMultiplier(3.0F)
                    .behaviors(new EntityDamageBehavior(3.0F, CustomTags.SPIDERS)))
            .sound(GTSoundEntries.SCREWDRIVER_TOOL)
            .symbol('d')
            .defaultAbilities(GTItemAbilities.DEFAULT_SCREWDRIVER_ACTIONS)
            .materialAmount(GTValues.M)
            .build();
    public static final GTToolType MORTAR = GTToolType.builder("mortar")
            .localization(text -> text.source("%s Mortar")
                    .translation(MinecraftLocale.ZH_CN, "%s研钵"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_MORTARS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_MORTAR)
            .definition(b -> b.crafting().damagePerCraftingAction(2)
                    .cannotAttack().attackSpeed(-2.4F))
            .sound(GTSoundEntries.MORTAR_TOOL)
            .symbol('m')
            .materialAmount(2 * GTValues.M)
            .build();
    public static final GTToolType WIRE_CUTTER = GTToolType.builder("wire_cutter")
            .localization(text -> text.source("%s Wire Cutter")
                    .translation(MinecraftLocale.ZH_CN, "%s剪线钳"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WIRE_CUTTERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_WIRE_CUTTER)
            .harvestTag(CustomTags.MINEABLE_WITH_WIRE_CUTTER)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .damagePerCraftingAction(4).attackDamage(-1.0F).attackSpeed(-2.4F))
            .sound(GTSoundEntries.WIRECUTTER_TOOL, true)
            .symbol('x')
            .defaultAbilities(GTItemAbilities.DEFAULT_WIRE_CUTTER_ACTIONS)
            .materialAmount(4 * GTValues.M) // 3 plates + 2 rods
            .build();
    public static final GTToolType KNIFE = GTToolType.builder("knife")
            .localization(text -> text.source("%s Knife")
                    .translation(MinecraftLocale.ZH_CN, "%s刀"))
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_KNIVES)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_KNIFE)
            .harvestTag(CustomTags.MINEABLE_WITH_KNIFE)
            .definition(b -> b.crafting().attacking().attackSpeed(3.0F))
            .symbol('k')
            .toolClasses(GTToolType.SWORD)
            .defaultAbilities(GTItemAbilities.KNIFE_DIG)
            .materialAmount(GTValues.M)
            .build();
    public static final GTToolType BUTCHERY_KNIFE = GTToolType.builder("butchery_knife")
            .localization(text -> text.source("%s Butchery Knife")
                    .translation(MinecraftLocale.ZH_CN, "%s屠刀"))
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_BUTCHERY_KNIFE)
            .definition(b -> b.attacking()
                    .attackDamage(1.5F).attackSpeed(-1.3F)
                    .defaultEnchantment(Enchantments.LOOTING, 3))
            .materialAmount(4 * GTValues.M)
            .build();
    public static final GTToolType PLUNGER = GTToolType.builder("plunger")
            .localization(text -> text.source("%s Plunger")
                    .translation(MinecraftLocale.ZH_CN, "%s搋子"))
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_PLUNGER)
            .definition(b -> b.cannotAttack().sneakBypassUse()
                    .attackSpeed(-2.4F).attackSpeed(-2.4F)
                    .behaviors(PlungerBehavior.INSTANCE))
            .sound(GTSoundEntries.PLUNGER_TOOL)
            .build();
    public static final GTToolType SHEARS = GTToolType.builder("shears")
            .localization(text -> text.source("%s Shears")
                    .translation(MinecraftLocale.ZH_CN, "%s剪刀"))
            .toolTag(ToolItemTagType.MATCH, Tags.Items.TOOLS_SHEAR)
            .harvestTag(CustomTags.MINEABLE_WITH_SHEARS)
            .definition(b -> b)
            .defaultAbilities(ItemAbilities.DEFAULT_SHEARS_ACTIONS)
            .build();
    public static final GTToolType DRILL_LV = GTToolType.builder("lv_drill")
            .localization(text -> text.source("%s Drill (LV)")
                    .translation(MinecraftLocale.ZH_CN, "%s电钻（§7LV§r）"))
            .idFormat("lv_%s_drill")
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_DRILL)
            .toolTag(ItemTags.PICKAXES)
            .toolTag(ItemTags.SHOVELS)
            .toolTag(ItemTags.HOES)
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking().aoe(1, 1, 0)
                    .attackDamage(1.0F).durabilityMultiplier(3.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, TorchPlaceBehavior.INSTANCE))
            .sound(GTSoundEntries.DRILL_TOOL, true)
            .electric(GTValues.LV)
            .toolClassNames("drill")
            .defaultAbilities(GTItemAbilities.DEFAULT_DRILL_ACTIONS)
            .build();
    public static final GTToolType DRILL_MV = GTToolType.builder("mv_drill")
            .localization(text -> text.source("%s Drill (MV)")
                    .translation(MinecraftLocale.ZH_CN, "%s电钻（§bMV§r）"))
            .idFormat("mv_%s_drill")
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_DRILL)
            .toolTag(ItemTags.PICKAXES)
            .toolTag(ItemTags.SHOVELS)
            .toolTag(ItemTags.HOES)
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking().aoe(1, 1, 2)
                    .attackDamage(1.0F).durabilityMultiplier(4.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_MV)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, TorchPlaceBehavior.INSTANCE))
            .sound(GTSoundEntries.DRILL_TOOL, true)
            .electric(GTValues.MV)
            .toolClassNames("drill")
            .defaultAbilities(GTItemAbilities.DEFAULT_DRILL_ACTIONS)
            .build();
    public static final GTToolType DRILL_HV = GTToolType.builder("hv_drill")
            .localization(text -> text.source("%s Drill (HV)")
                    .translation(MinecraftLocale.ZH_CN, "%s电钻（§6HV§r）"))
            .idFormat("hv_%s_drill")
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_DRILL)
            .toolTag(ItemTags.PICKAXES)
            .toolTag(ItemTags.SHOVELS)
            .toolTag(ItemTags.HOES)
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking().aoe(2, 2, 4)
                    .attackDamage(1.0F).durabilityMultiplier(5.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_HV)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, TorchPlaceBehavior.INSTANCE))
            .sound(GTSoundEntries.DRILL_TOOL, true)
            .electric(GTValues.HV)
            .toolClassNames("drill")
            .defaultAbilities(GTItemAbilities.DEFAULT_DRILL_ACTIONS)
            .build();
    public static final GTToolType DRILL_EV = GTToolType.builder("ev_drill")
            .localization(text -> text.source("%s Drill (EV)")
                    .translation(MinecraftLocale.ZH_CN, "%s电钻（§5EV§r）"))
            .idFormat("ev_%s_drill")
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_DRILL)
            .toolTag(ItemTags.PICKAXES)
            .toolTag(ItemTags.SHOVELS)
            .toolTag(ItemTags.HOES)
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking().aoe(3, 3, 6)
                    .attackDamage(1.0F).durabilityMultiplier(6.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_EV)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, TorchPlaceBehavior.INSTANCE))
            .sound(GTSoundEntries.DRILL_TOOL, true)
            .electric(GTValues.EV)
            .toolClassNames("drill")
            .defaultAbilities(GTItemAbilities.DEFAULT_DRILL_ACTIONS)
            .build();
    public static final GTToolType DRILL_IV = GTToolType.builder("iv_drill")
            .localization(text -> text.source("%s Drill (IV)")
                    .translation(MinecraftLocale.ZH_CN, "%s电钻（§9IV§r）"))
            .idFormat("iv_%s_drill")
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_DRILL)
            .toolTag(ItemTags.PICKAXES)
            .toolTag(ItemTags.SHOVELS)
            .toolTag(ItemTags.HOES)
            .toolTag(ItemTags.CLUSTER_MAX_HARVESTABLES)
            .harvestTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .harvestTag(BlockTags.MINEABLE_WITH_SHOVEL)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking().aoe(4, 4, 8)
                    .attackDamage(1.0F).durabilityMultiplier(7.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV)
                    .behaviors(AOEConfigUIBehavior.INSTANCE, TorchPlaceBehavior.INSTANCE))
            .sound(GTSoundEntries.DRILL_TOOL, true)
            .electric(GTValues.IV)
            .toolClassNames("drill")
            .defaultAbilities(GTItemAbilities.DEFAULT_DRILL_ACTIONS)
            .build();
    public static final GTToolType CHAINSAW_LV = GTToolType.builder("lv_chainsaw")
            .localization(text -> text.source("%s Chainsaw (LV)")
                    .translation(MinecraftLocale.ZH_CN, "%s链锯（§7LV§r）"))
            .idFormat("lv_%s_chainsaw")
            .toolTag(ToolItemTagType.MATCH, ItemTags.AXES)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_CHAINSAW)
            .harvestTag(BlockTags.MINEABLE_WITH_AXE)
            .harvestTag(BlockTags.SWORD_EFFICIENT)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking()
                    .efficiencyMultiplier(2.0F).attackDamage(5.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV)
                    .behaviors(HarvestIceBehavior.INSTANCE, DisableShieldBehavior.INSTANCE,
                            TreeFellingBehavior.INSTANCE))
            .sound(GTSoundEntries.CHAINSAW_TOOL, true)
            .electric(GTValues.LV)
            .toolClasses(GTToolType.AXE)
            .defaultAbilities(ItemAbilities.AXE_DIG, ItemAbilities.SWORD_DIG, ItemAbilities.HOE_DIG,
                    GTItemAbilities.SAW_DIG)
            .build();
    public static final GTToolType CHAINSAW_HV = GTToolType.builder("hv_chainsaw")
            .localization(text -> text.source("%s Chainsaw (HV)")
                    .translation(MinecraftLocale.ZH_CN, "%s链锯（§6HV§r）"))
            .idFormat("hv_%s_chainsaw")
            .toolTag(ToolItemTagType.MATCH, ItemTags.AXES)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_CHAINSAW)
            .harvestTag(BlockTags.MINEABLE_WITH_AXE)
            .harvestTag(BlockTags.SWORD_EFFICIENT)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking()
                    .efficiencyMultiplier(3.0F)
                    .attackDamage(5.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_HV)
                    .behaviors(HarvestIceBehavior.INSTANCE, DisableShieldBehavior.INSTANCE,
                            TreeFellingBehavior.INSTANCE))
            .sound(GTSoundEntries.CHAINSAW_TOOL, true)
            .electric(GTValues.HV)
            .toolClasses(GTToolType.AXE)
            .defaultAbilities(ItemAbilities.AXE_DIG, ItemAbilities.SWORD_DIG, ItemAbilities.HOE_DIG,
                    GTItemAbilities.SAW_DIG)
            .build();
    public static final GTToolType CHAINSAW_IV = GTToolType.builder("iv_chainsaw")
            .localization(text -> text.source("%s Chainsaw (IV)")
                    .translation(MinecraftLocale.ZH_CN, "%s链锯（§9IV§r）"))
            .idFormat("iv_%s_chainsaw")
            .toolTag(ToolItemTagType.MATCH, ItemTags.AXES)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_CHAINSAW)
            .harvestTag(BlockTags.MINEABLE_WITH_AXE)
            .harvestTag(BlockTags.SWORD_EFFICIENT)
            .harvestTag(BlockTags.MINEABLE_WITH_HOE)
            .definition(b -> b.blockBreaking()
                    .efficiencyMultiplier(4.0F)
                    .attackDamage(5.0F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV)
                    .behaviors(HarvestIceBehavior.INSTANCE, DisableShieldBehavior.INSTANCE,
                            TreeFellingBehavior.INSTANCE))
            .sound(GTSoundEntries.CHAINSAW_TOOL, true)
            .electric(GTValues.IV)
            .toolClasses(GTToolType.AXE)
            .defaultAbilities(ItemAbilities.AXE_DIG, ItemAbilities.SWORD_DIG, ItemAbilities.HOE_DIG,
                    GTItemAbilities.SAW_DIG)
            .build();
    public static final GTToolType WRENCH_LV = GTToolType.builder("lv_wrench")
            .localization(text -> text.source("%s Wrench (LV)")
                    .translation(MinecraftLocale.ZH_CN, "%s扳手（§7LV§r）"))
            .idFormat("lv_%s_wrench")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WRENCHES)
            .toolTag(ToolItemTagType.MATCH, Tags.Items.TOOLS_WRENCH)
            .harvestTag(CustomTags.MINEABLE_WITH_WRENCH)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .efficiencyMultiplier(2.0F).attackDamage(1.0F).attackSpeed(-2.8F)
                    .behaviors(BlockRotatingBehavior.INSTANCE,
                            new EntityDamageBehavior(3.0F, CustomTags.IRON_GOLEMS),
                            ToolModeSwitchBehavior.INSTANCE)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV))
            .sound(GTSoundEntries.WRENCH_TOOL, true)
            .electric(GTValues.LV)
            .toolClasses(GTToolType.WRENCH)
            .defaultAbilities(GTItemAbilities.WRENCH_DIG, GTItemAbilities.WRENCH_DISMANTLE,
                    GTItemAbilities.WRENCH_CONNECT)
            .build();
    public static final GTToolType WRENCH_HV = GTToolType.builder("hv_wrench")
            .localization(text -> text.source("%s Wrench (HV)")
                    .translation(MinecraftLocale.ZH_CN, "%s扳手（§6HV§r）"))
            .idFormat("hv_%s_wrench")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WRENCHES)
            .toolTag(ToolItemTagType.MATCH, Tags.Items.TOOLS_WRENCH)
            .harvestTag(CustomTags.MINEABLE_WITH_WRENCH)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .efficiencyMultiplier(3.0F).attackDamage(1.0F).attackSpeed(-2.8F)
                    .behaviors(BlockRotatingBehavior.INSTANCE,
                            new EntityDamageBehavior(3.0F, CustomTags.IRON_GOLEMS),
                            ToolModeSwitchBehavior.INSTANCE)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_HV))
            .sound(GTSoundEntries.WRENCH_TOOL, true)
            .electric(GTValues.HV)
            .toolClasses(GTToolType.WRENCH)
            .defaultAbilities(GTItemAbilities.WRENCH_DIG, GTItemAbilities.WRENCH_DISMANTLE,
                    GTItemAbilities.WRENCH_CONNECT)
            .build();
    public static final GTToolType WRENCH_IV = GTToolType.builder("iv_wrench")
            .localization(text -> text.source("%s Wrench (IV)")
                    .translation(MinecraftLocale.ZH_CN, "%s扳手（§9IV§r）"))
            .idFormat("iv_%s_wrench")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WRENCHES)
            .toolTag(ToolItemTagType.MATCH, Tags.Items.TOOLS_WRENCH)
            .harvestTag(CustomTags.MINEABLE_WITH_WRENCH)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .efficiencyMultiplier(4.0F).attackDamage(1.0F).attackSpeed(-2.8F)
                    .behaviors(BlockRotatingBehavior.INSTANCE,
                            new EntityDamageBehavior(3.0F, CustomTags.IRON_GOLEMS),
                            ToolModeSwitchBehavior.INSTANCE)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV))
            .sound(GTSoundEntries.WRENCH_TOOL, true)
            .electric(GTValues.IV)
            .toolClasses(GTToolType.WRENCH)
            .defaultAbilities(GTItemAbilities.WRENCH_DIG, GTItemAbilities.WRENCH_DISMANTLE,
                    GTItemAbilities.WRENCH_CONNECT)
            .build();

    public static final GTToolType WIRE_CUTTER_LV = GTToolType.builder("lv_wirecutter")
            .localization(text -> text.source("%s Wire Cutter (LV)")
                    .translation(MinecraftLocale.ZH_CN, "%s剪线钳（LV）"))
            .idFormat("lv_%s_wire_cutter")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WIRE_CUTTERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_WIRE_CUTTER)
            .harvestTag(CustomTags.MINEABLE_WITH_WIRE_CUTTER)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .damagePerCraftingAction(4).attackDamage(-1.0F).attackSpeed(-2.4F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV))
            .sound(GTSoundEntries.WIRECUTTER_TOOL, true)
            .electric(GTValues.LV)
            .toolClasses(GTToolType.WIRE_CUTTER)
            .defaultAbilities(GTItemAbilities.DEFAULT_WIRE_CUTTER_ACTIONS)
            .build();

    public static final GTToolType WIRE_CUTTER_HV = GTToolType.builder("hv_wirecutter")
            .localization(text -> text.source("%s Wire Cutter (HV)")
                    .translation(MinecraftLocale.ZH_CN, "%s剪线钳（HV）"))
            .idFormat("hv_%s_wire_cutter")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WIRE_CUTTERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_WIRE_CUTTER)
            .harvestTag(CustomTags.MINEABLE_WITH_WIRE_CUTTER)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .damagePerCraftingAction(4).attackDamage(-1.0F).attackSpeed(-2.4F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_HV))
            .sound(GTSoundEntries.WIRECUTTER_TOOL, true)
            .electric(GTValues.HV)
            .toolClasses(GTToolType.WIRE_CUTTER)
            .defaultAbilities(GTItemAbilities.DEFAULT_WIRE_CUTTER_ACTIONS)
            .build();

    public static final GTToolType WIRE_CUTTER_IV = GTToolType.builder("iv_wirecutter")
            .localization(text -> text.source("%s Wire Cutter (IV)")
                    .translation(MinecraftLocale.ZH_CN, "%s剪线钳（IV）"))
            .idFormat("iv_%s_wire_cutter")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_WIRE_CUTTERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_WIRE_CUTTER)
            .harvestTag(CustomTags.MINEABLE_WITH_WIRE_CUTTER)
            .definition(b -> b.blockBreaking().crafting().sneakBypassUse()
                    .damagePerCraftingAction(4).attackDamage(-1.0F).attackSpeed(-2.4F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV))
            .sound(GTSoundEntries.WIRECUTTER_TOOL, true)
            .electric(GTValues.IV)
            .toolClasses(GTToolType.WIRE_CUTTER)
            .defaultAbilities(GTItemAbilities.DEFAULT_WIRE_CUTTER_ACTIONS)
            .build();
    public static final GTToolType BUZZSAW_LV = GTToolType.builder("lv_buzzsaw")
            .localization(text -> text.source("%s Buzzsaw (LV)")
                    .translation(MinecraftLocale.ZH_CN, "%s圆锯（§7LV§r）"))
            .idFormat("lv_%s_buzzsaw")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_SAWS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SAW)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_BUZZSAW)
            .definition(b -> b.crafting().attackDamage(1.5F).attackSpeed(-3.2F)
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV))
            .sound(GTSoundEntries.CHAINSAW_TOOL, true)
            .electric(GTValues.LV)
            .toolClasses(GTToolType.SAW)
            .build();
    public static final GTToolType SCREWDRIVER_LV = GTToolType.builder("lv_screwdriver")
            .localization(text -> text.source("%s Screwdriver (LV)")
                    .translation(MinecraftLocale.ZH_CN, "%s螺丝刀（§7LV§r）"))
            .idFormat("lv_%s_screwdriver")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_SCREWDRIVERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SCREWDRIVER)
            .definition(b -> b.crafting().sneakBypassUse()
                    .attackDamage(-1.0F).efficiencyMultiplier(3.0F)
                    .behaviors(new EntityDamageBehavior(3.0F, CustomTags.SPIDERS))
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_LV))
            .sound(GTSoundEntries.SCREWDRIVER_TOOL)
            .electric(GTValues.LV)
            .toolClasses(GTToolType.SCREWDRIVER)
            .defaultAbilities(GTItemAbilities.DEFAULT_SCREWDRIVER_ACTIONS)
            .build();
    public static final GTToolType SCREWDRIVER_HV = GTToolType.builder("hv_screwdriver")
            .localization(text -> text.source("%s Screwdriver (HV)")
                    .translation(MinecraftLocale.ZH_CN, "%s螺丝刀（§6HV§r）"))
            .idFormat("hv_%s_screwdriver")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_SCREWDRIVERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SCREWDRIVER)
            .definition(b -> b.crafting().sneakBypassUse()
                    .attackDamage(-1.0F).attackSpeed(3.0F)
                    .behaviors(new EntityDamageBehavior(3.0F, EntityType.SPIDER))
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_HV))
            .sound(GTSoundEntries.SCREWDRIVER_TOOL)
            .electric(GTValues.HV)
            .toolClasses(GTToolType.SCREWDRIVER)
            .defaultAbilities(GTItemAbilities.DEFAULT_SCREWDRIVER_ACTIONS)
            .build();
    public static final GTToolType SCREWDRIVER_IV = GTToolType.builder("iv_screwdriver")
            .localization(text -> text.source("%s Screwdriver (IV)")
                    .translation(MinecraftLocale.ZH_CN, "%s螺丝刀（§9IV§r）"))
            .idFormat("iv_%s_screwdriver")
            .toolTag(ToolItemTagType.CRAFTING, CustomTags.CRAFTING_SCREWDRIVERS)
            .toolTag(ToolItemTagType.MATCH, CustomTags.TOOLS_SCREWDRIVER)
            .definition(b -> b.crafting().sneakBypassUse()
                    .attackDamage(-1.0F).attackSpeed(3.0F)
                    .behaviors(new EntityDamageBehavior(3.0F, EntityType.SPIDER))
                    .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV))
            .sound(GTSoundEntries.SCREWDRIVER_TOOL)
            .electric(GTValues.IV)
            .toolClasses(GTToolType.SCREWDRIVER)
            .defaultAbilities(GTItemAbilities.DEFAULT_SCREWDRIVER_ACTIONS)
            .build();

    public final String name;
    public final String idFormat;
    // at least one has to be set. first one MUST be the main tag.
    public final List<TagKey<Item>> itemTags;
    public final List<TagKey<Item>> matchTags;
    public final List<TagKey<Item>> craftingTags;
    public final List<TagKey<Block>> harvestTags;
    public final Set<ItemAbility> defaultAbilities;
    public final ResourceLocation modelLocation;
    public final Set<String> toolClassNames;
    public final Set<GTToolType> toolClasses;
    @Nullable
    public final SoundEntry soundEntry;
    public final boolean playSoundOnBlockDestroy;
    public final char symbol;
    public final long materialAmount;

    public final IGTToolDefinition toolDefinition;
    public final ToolConstructor constructor;
    public final int electricTier;
    @Getter
    private @Nullable LocalizationDeclaration localization;

    public GTToolType(String name, String idFormat, char symbol,
                      Set<GTToolType> toolClasses, ToolConstructor constructor, IGTToolDefinition toolDefinition,
                      List<TagKey<Item>> itemTags, List<TagKey<Item>> matchTags, List<TagKey<Item>> craftingTags,
                      List<TagKey<Block>> harvestTags, Set<ItemAbility> defaultAbilities,
                      Set<String> toolClassNames, ResourceLocation modelLocation,
                      @Nullable SoundEntry soundEntry, boolean playSoundOnBlockDestroy,
                      int electricTier, long materialAmount) {
        this.name = name;
        this.idFormat = idFormat;
        this.symbol = symbol;
        toolClasses.add(this);
        this.toolClasses = toolClasses;
        this.toolDefinition = toolDefinition;
        this.constructor = constructor;
        this.itemTags = itemTags;
        this.matchTags = matchTags;
        this.craftingTags = craftingTags;
        this.harvestTags = harvestTags;
        this.defaultAbilities = defaultAbilities;
        this.modelLocation = modelLocation;
        this.toolClassNames = toolClassNames;
        this.soundEntry = soundEntry;
        this.playSoundOnBlockDestroy = playSoundOnBlockDestroy;
        this.electricTier = electricTier;
        this.materialAmount = materialAmount;

        types.put(name, this);
        if (GTPM.isDataGen()) {
            GTRegistration.REGISTRATE.addDataGeneratorFirst(ProviderType.LANG, provider -> {
                if (types.get(name) != this) return;

                String key = getUnlocalizedName();
                if (localization == null) {
                    provider.add(key, FormattingUtil.toEnglishName(name));
                } else {
                    GTLanguageProvider.addLocalization(provider, GTRegistration.REGISTRATE, key, localization);
                }
            });
        }
    }

    public boolean is(ItemStack itemStack) {
        return ToolHelper.is(itemStack, this);
    }

    public String getUnlocalizedName() {
        return "item.gtpm.tool." + name;
    }

    public enum ToolItemTagType {
        NONE,
        MATCH,
        CRAFTING
    }

    @FunctionalInterface
    public interface ToolConstructor {

        IGTTool create(GTToolType type, MaterialToolTier tier, Material material, IGTToolDefinition definition,
                       Item.Properties properties);
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    @Accessors(fluent = true, chain = true)
    public static class Builder {

        private final String name;
        private @Nullable LocalizationDeclaration localization;
        @Setter
        private String idFormat;

        private final List<TagKey<Item>> itemTags = new ArrayList<>();
        private final List<TagKey<Item>> matchTags = new ArrayList<>();
        private final List<TagKey<Item>> craftingTags = new ArrayList<>();
        private final List<TagKey<Block>> harvestTags = new ArrayList<>();
        private final Set<ItemAbility> defaultAbilities = Sets.newIdentityHashSet();
        @Setter
        private Set<String> toolClassNames = new HashSet<>();
        private final Set<GTToolType> toolClasses = Sets.newIdentityHashSet();
        @Setter
        private IGTToolDefinition toolDefinition;
        @Setter
        private long materialAmount;
        @Setter
        private int tier = -1;
        @Setter
        private char symbol = ' ';
        @Setter
        private ToolConstructor constructor = GTToolItem::new;
        @Setter
        private ResourceLocation modelLocation;
        private SoundEntry sound;
        private boolean playSoundOnBlockDestroy;

        public Builder(String name) {
            this.name = name;
            this.idFormat = "%s_" + name;
            this.modelLocation = GTPM.id("item/tools/" + name);
        }

        @SafeVarargs
        public final Builder toolTag(TagKey<Item>... tags) {
            return toolTag(ToolItemTagType.NONE, tags);
        }

        @SafeVarargs
        public final Builder toolTag(ToolItemTagType tagType, TagKey<Item>... tags) {
            itemTags.addAll(Arrays.asList(tags));
            if (tagType == ToolItemTagType.MATCH) {
                matchTags.addAll(Arrays.asList(tags));
            }
            if (tagType == ToolItemTagType.CRAFTING) {
                craftingTags.addAll(Arrays.asList(tags));
            }
            return this;
        }

        public Builder harvestTag(TagKey<Block> tag) {
            harvestTags.add(tag);
            return this;
        }

        public Builder defaultAbilities(ItemAbility... abilities) {
            defaultAbilities.addAll(Arrays.asList(abilities));
            return this;
        }

        public Builder defaultAbilities(Collection<ItemAbility> abilities) {
            defaultAbilities.addAll(abilities);
            return this;
        }

        public Builder defaultAbilities(Collection<ItemAbility> abilities, ItemAbility... extra) {
            defaultAbilities.addAll(abilities);
            defaultAbilities.addAll(Arrays.asList(extra));
            return this;
        }

        @Tolerate
        public Builder toolClasses(GTToolType... classes) {
            this.toolClasses.addAll(Arrays.stream(classes).toList());
            this.toolClassNames.addAll(Arrays.stream(classes).map(type -> type.name).toList());
            return this;
        }

        @Tolerate
        public Builder toolClassNames(String... classes) {
            this.toolClassNames.addAll(Arrays.stream(classes).toList());
            return this;
        }

        @Tolerate
        public Builder definition(UnaryOperator<ToolDefinitionBuilder> builder) {
            this.toolDefinition = builder.apply(new ToolDefinitionBuilder()).build();
            return this;
        }

        public Builder sound(SoundEntry sound) {
            return this.sound(sound, false);
        }

        public Builder sound(SoundEntry sound, boolean playSoundOnBlockDestroy) {
            this.sound = sound;
            this.playSoundOnBlockDestroy = playSoundOnBlockDestroy;
            return this;
        }

        public Builder electric(int tier) {
            return tier(tier);
        }

        /** Declares the material-dependent item name format for this tool type. */
        public Builder localization(Consumer<LocalizationDeclaration.Builder> configure) {
            var text = new LocalizationDeclaration.Builder();
            configure.accept(text);
            localization = text.build();
            return this;
        }

        private GTToolType get() {
            var tool = new GTToolType(name, idFormat, symbol,
                    toolClasses, constructor,
                    toolDefinition, itemTags, matchTags, craftingTags, harvestTags, defaultAbilities,
                    toolClassNames, modelLocation,
                    sound, playSoundOnBlockDestroy,
                    tier, materialAmount);
            tool.localization = localization;
            return tool;
        }

        public GTToolType build() {
            if (toolClassNames.isEmpty()) {
                toolClassNames.add(name);
            }
            if (this.symbol == ' ') {
                return get();
            }
            GTToolType existing = ToolHelper.getToolFromSymbol(this.symbol);
            if (existing != null) {
                throw new IllegalArgumentException(
                        String.format("Symbol %s has been taken by %s already!", symbol, existing));
            }
            GTToolType supplied = get();
            ToolHelper.registerToolSymbol(this.symbol, supplied);
            return supplied;
        }
    }
}

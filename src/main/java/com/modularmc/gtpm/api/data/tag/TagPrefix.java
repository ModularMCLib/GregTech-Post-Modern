package com.modularmc.gtpm.api.data.tag;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.block.MaterialBlock;
import com.modularmc.gtpm.api.block.OreBlock;
import com.modularmc.gtpm.api.data.chemical.material.ItemMaterialData;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags;
import com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconType;
import com.modularmc.gtpm.api.data.chemical.material.properties.OreProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.chemical.material.stack.MaterialStack;
import com.modularmc.gtpm.api.item.MaterialBlockItem;
import com.modularmc.gtpm.api.item.TagPrefixItem;
import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sync_system.FieldCodecs;
import com.modularmc.gtpm.common.data.GTBlocks;
import com.modularmc.gtpm.common.data.GTMaterialBlocks;
import com.modularmc.gtpm.common.data.GTMaterialItems;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.data.recipe.CustomTags;
import com.modularmc.gtpm.integration.xei.widgets.GTOreByProduct;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.TagUtil;
import com.modularmc.gtpm.utils.memoization.GTMemoizer;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static com.modularmc.gtpm.api.data.tag.TagPrefix.Conditions.*;

@TransVariants(keyPrefix = "metaitem",
               value = {
                       @Variant(id = "crushed.tooltip.purify",
                                source = "Right click a Cauldron to get Purified Ore",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击盛有水的炼药锅以清洗")),
                       @Variant(id = "dust.tooltip.purify",
                                source = "Right click a Cauldron to get clean Dust",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击盛有水的炼药锅以清洗")),
               })
@SuppressWarnings("unused")
@Accessors(chain = true, fluent = true)
public class TagPrefix {

    public final static Map<String, TagPrefix> PREFIXES = new HashMap<>();
    public static final Map<TagPrefix, OreType> ORES = new Object2ObjectLinkedOpenHashMap<>();

    public static final Codec<TagPrefix> CODEC = Codec.STRING.flatXmap(
            str -> Optional.ofNullable(get(str)).map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "invalid TagPrefix: " + str)),
            prefix -> DataResult.success(prefix.name));

    static {
        FieldCodecs.register(TagPrefix.class, CODEC);
    }

    public static void init() {}

    public static TagPrefix get(String name) {
        return GTRegistries.TAG_PREFIXES.get(GTPM.id(name));
    }

    public boolean isEmpty() {
        return this == NULL_PREFIX;
    }

    public static final TagPrefix NULL_PREFIX = new TagPrefix("null")
            .localization(text -> text.source("%s Null")
                    .translation(MinecraftLocale.ZH_CN, "%s Null"));

    @TransVariants(keyPrefix = "tag.item.c",
                   value = {
                           @Variant(id = "ores", source = "Ores",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿石")),
                           @Variant(id = "ores_in_ground.stone", source = "Ores in Stone",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于石头的矿石")),
                   })
    public static final TagPrefix ore = oreTagPrefix("stone", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("%s Ore")
                    .translation(MinecraftLocale.ZH_CN, "%s矿石"))
            .registerOre(
                    Blocks.STONE::defaultBlockState, () -> GTMaterials.Stone, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    ResourceLocation.withDefaultNamespace("block/stone"), false, false, true);

    @Trans(key = "tag.item.c.ores_in_ground.granite",
           source = "Ores in Granite",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于花岗岩的矿石"))
    public static final TagPrefix oreGranite = oreTagPrefix("granite", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Granite %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "花岗岩%s矿石"))
            .registerOre(
                    Blocks.GRANITE::defaultBlockState, () -> GTMaterials.Granite, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIRT).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    ResourceLocation.withDefaultNamespace("block/granite"));

    @Trans(key = "tag.item.c.ores_in_ground.diorite",
           source = "Ores in Diorite",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于闪长岩的矿石"))
    public static final TagPrefix oreDiorite = oreTagPrefix("diorite", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Diorite %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "闪长岩%s矿石"))
            .registerOre(
                    Blocks.DIORITE::defaultBlockState, () -> GTMaterials.Diorite, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    ResourceLocation.withDefaultNamespace("block/diorite"));

    @Trans(key = "tag.item.c.ores_in_ground.andesite",
           source = "Ores in Andesite",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于安山岩的矿石"))
    public static final TagPrefix oreAndesite = oreTagPrefix("andesite", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Andesite %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "安山岩%s矿石"))
            .registerOre(
                    Blocks.ANDESITE::defaultBlockState, () -> GTMaterials.Andesite, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIRT).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    ResourceLocation.withDefaultNamespace("block/andesite"));

    @Trans(key = "tag.item.c.ores_in_ground.red_granite",
           source = "Ores in Red Granite",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于红花岗岩的矿石"))
    public static final TagPrefix oreRedGranite = oreTagPrefix("red_granite", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Red Granite %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "红花岗岩%s矿石"))
            .registerOre(() -> GTBlocks.RED_GRANITE.getDefaultState(), () -> GTMaterials.RedGranite,
                    BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED).requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F),
                    GTPM.id("block/red_granite"));

    @Trans(key = "tag.item.c.ores_in_ground.marble",
           source = "Ores in Marble",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于大理岩的矿石"))
    public static final TagPrefix oreMarble = oreTagPrefix("marble", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Marble %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "大理岩%s矿石"))
            .registerOre(
                    () -> GTBlocks.MARBLE.getDefaultState(), () -> GTMaterials.Marble, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.QUARTZ).requiresCorrectToolForDrops().strength(3.0F, 3.0F),
                    GTPM.id("block/marble"));

    @Trans(key = "tag.item.c.ores_in_ground.deepslate",
           source = "Ores in Deepslate",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于深板岩的矿石"))
    public static final TagPrefix oreDeepslate = oreTagPrefix("deepslate", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Deepslate %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "深板岩%s矿石"))
            .registerOre(
                    Blocks.DEEPSLATE::defaultBlockState, () -> GTMaterials.Deepslate, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE).requiresCorrectToolForDrops().strength(4.5F, 3.0F)
                            .sound(SoundType.DEEPSLATE),
                    ResourceLocation.withDefaultNamespace("block/deepslate"), false, false, true);

    @Trans(key = "tag.item.c.ores_in_ground.tuff",
           source = "Ores in Tuff",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于凝灰岩的矿石"))
    public static final TagPrefix oreTuff = oreTagPrefix("tuff", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Tuff %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "凝灰岩%s矿石"))
            .registerOre(
                    Blocks.TUFF::defaultBlockState, () -> GTMaterials.Tuff, BlockBehaviour.Properties.of()
                            .mapColor(MapColor.TERRACOTTA_GRAY).requiresCorrectToolForDrops().strength(3.0F, 3.0F)
                            .sound(SoundType.TUFF),
                    ResourceLocation.withDefaultNamespace("block/tuff"));

    @Trans(key = "tag.item.c.ores_in_ground.sand",
           source = "Ores in Sand",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于沙子的矿石"))
    public static final TagPrefix oreSand = oreTagPrefix("sand", BlockTags.MINEABLE_WITH_SHOVEL)
            .localization(text -> text.source("Sand %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "沙子%s矿石"))
            .registerOre(Blocks.SAND::defaultBlockState, () -> GTMaterials.SiliconDioxide,
                    BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE)
                            .strength(0.5F).sound(SoundType.SAND),
                    ResourceLocation.withDefaultNamespace("block/sand"), false, true, false);

    @Trans(key = "tag.item.c.ores_in_ground.red_sand",
           source = "Ores in Red Sand",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于红沙的矿石"))
    public static final TagPrefix oreRedSand = oreTagPrefix("redSand", BlockTags.MINEABLE_WITH_SHOVEL)
            .localization(text -> text.source("Red Sand %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "红沙%s矿石"))
            .registerOre(Blocks.RED_SAND::defaultBlockState, () -> GTMaterials.SiliconDioxide,
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.SNARE)
                            .strength(0.5F).sound(SoundType.SAND),
                    ResourceLocation.withDefaultNamespace("block/red_sand"), false, true, false);

    @Trans(key = "tag.item.c.ores_in_ground.gravel",
           source = "Ores in Gravel",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于沙砾的矿石"))
    public static final TagPrefix oreGravel = oreTagPrefix("gravel", BlockTags.MINEABLE_WITH_SHOVEL)
            .localization(text -> text.source("Gravel %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "沙砾%s矿石"))
            .registerOre(Blocks.GRAVEL::defaultBlockState, () -> GTMaterials.Flint,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.SNARE)
                            .strength(0.6F).sound(SoundType.GRAVEL),
                    ResourceLocation.withDefaultNamespace("block/gravel"), false, true, false);

    @Trans(key = "tag.item.c.ores_in_ground.basalt",
           source = "Ores in Basalt",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于玄武岩的矿石"))
    public static final TagPrefix oreBasalt = oreTagPrefix("basalt", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Basalt %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "玄武岩%s矿石"))
            .registerOre(Blocks.BASALT::defaultBlockState, () -> GTMaterials.Basalt,
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.5F, 4.2F)
                            .sound(SoundType.BASALT),
                    ResourceLocation.withDefaultNamespace("block/basalt"), true);

    @Trans(key = "tag.item.c.ores_in_ground.netherrack",
           source = "Ores in Netherrack",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于下界岩的矿石"))
    public static final TagPrefix oreNetherrack = oreTagPrefix("netherrack", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Nether %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "下界岩%s矿石"))
            .registerOre(Blocks.NETHERRACK::defaultBlockState, () -> GTMaterials.Netherrack,
                    BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.NETHER_ORE),
                    ResourceLocation.withDefaultNamespace("block/netherrack"), true, false, true);

    @Trans(key = "tag.item.c.ores_in_ground.blackstone",
           source = "Ores in Blackstone",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于黑石的矿石"))
    public static final TagPrefix oreBlackstone = oreTagPrefix("blackstone", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("Blackstone %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "黑石%s矿石"))
            .registerOre(Blocks.BLACKSTONE::defaultBlockState, () -> GTMaterials.Blackstone,
                    BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
                            .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F),
                    ResourceLocation.withDefaultNamespace("block/blackstone"), true, false, false);

    @Trans(key = "tag.item.c.ores_in_ground.endstone",
           source = "Ores in End Stone",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "赋存于末地石的矿石"))
    public static final TagPrefix oreEndstone = oreTagPrefix("endstone", BlockTags.MINEABLE_WITH_PICKAXE)
            .localization(text -> text.source("End %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "末地石%s矿石"))
            .registerOre(Blocks.END_STONE::defaultBlockState, () -> GTMaterials.Endstone,
                    BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM)
                            .requiresCorrectToolForDrops().strength(4.5F, 9.0F),
                    ResourceLocation.withDefaultNamespace("block/end_stone"), true, false, true);

    @Trans(key = "tag.item.c.raw_materials",
           source = "Raw Materials",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "粗矿"))
    public static final TagPrefix rawOre = new TagPrefix("raw")
            .localization(text -> text.source("Raw %s")
                    .translation(MinecraftLocale.ZH_CN, "粗%s"))
            .idPattern("raw_%s")
            .defaultTagPath("raw_materials/%s")
            .unformattedTagPath("raw_materials")
            .materialIconType(MaterialIconType.rawOre)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasOreProperty);

    public static final TagPrefix rawOreBlock = new TagPrefix("rawOreBlock")
            .localization(text -> text.source("Block of Raw %s")
                    .translation(MinecraftLocale.ZH_CN, "粗%s块"))
            .idPattern("raw_%s_block")
            .defaultTagPath("storage_blocks/raw_%s")
            .unformattedTagPath("storage_blocks")
            .materialIconType(MaterialIconType.rawOreBlock)
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .unificationEnabled(true)
            .generateBlock(true)
            .generationCondition(hasOreProperty);

    @Trans(key = "tag.item.c.refined_ores",
           source = "Refined Ores",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "精炼矿石"))
    public static final TagPrefix crushedRefined = new TagPrefix("refinedOre")
            .localization(text -> text.source("Refined %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "精炼%s矿石"))
            .idPattern("refined_%s_ore")
            .defaultTagPath("refined_ores/%s")
            .defaultTagPath("refined_ores")
            .materialIconType(MaterialIconType.crushedRefined)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasOreProperty);

    @TransVariants(keyPrefix = "tag.item.c",
                   value = {
                           @Variant(id = "purified_ores", source = "Purified Ores",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纯净矿石")),
                           @Variant(id = "siftables", source = "Siftable Materials",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可筛选材料")),
                   })
    public static final TagPrefix crushedPurified = new TagPrefix("purifiedOre")
            .localization(text -> text.source("Purified %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "纯净%s矿石"))
            .idPattern("purified_%s_ore")
            .defaultTagPath("purified_ores/%s")
            .defaultTagPath("purified_ores")
            .customTagPredicate("siftables", false, m -> m.hasProperty(PropertyKey.GEM))
            .materialIconType(MaterialIconType.crushedPurified)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasOreProperty);

    @Trans(key = "tag.item.c.crushed_ores",
           source = "Crushed Ores",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "粉碎矿石"))
    public static final TagPrefix crushed = new TagPrefix("crushedOre")
            .localization(text -> text.source("Crushed %s Ore")
                    .translation(MinecraftLocale.ZH_CN, "粉碎%s矿石"))
            .idPattern("crushed_%s_ore")
            .defaultTagPath("crushed_ores/%s")
            .unformattedTagPath("crushed_ores")
            .materialIconType(MaterialIconType.crushed)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasOreProperty)
            .tooltip((mat, tooltips) -> tooltips.add(Component.translatable("metaitem.crushed.tooltip.purify")));

    // A hot Ingot, which has to be cooled down by a Vacuum Freezer.
    @Trans(key = "tag.item.c.hot_ingots",
           source = "Hot Ingots",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "热锭"))
    public static final TagPrefix ingotHot = new TagPrefix("hotIngot")
            .localization(text -> text.source("Hot %s Ingot")
                    .translation(MinecraftLocale.ZH_CN, "热%s锭"))
            .idPattern("hot_%s_ingot")
            .defaultTagPath("hot_ingots/%s")
            .unformattedTagPath("hot_ingots")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.ingotHot)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(
                    hasBlastProperty.and(mat -> mat.getProperty(PropertyKey.BLAST).getBlastTemperature() > 1750));

    // A regular Ingot.
    @Trans(key = "tag.item.c.ingots",
           source = "Ingots",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锭"))
    public static final TagPrefix ingot = new TagPrefix("ingot")
            .localization(text -> text.source("%s Ingot")
                    .translation(MinecraftLocale.ZH_CN, "%s锭"))
            .defaultTagPath("ingots/%s")
            .unformattedTagPath("ingots")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.ingot)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasIngotProperty);

    // A regular Gem worth one Dust.
    @Trans(key = "tag.item.c.gems",
           source = "Gems",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "宝石"))
    public static final TagPrefix gem = new TagPrefix("gem")
            .localization(text -> text.source("%s")
                    .translation(MinecraftLocale.ZH_CN, "%s"))
            .defaultTagPath("gems/%s")
            .unformattedTagPath("gems")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.gem)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasGemProperty);

    // A regular Gem worth one small Dust.
    @Trans(key = "tag.item.c.chipped_gems",
           source = "Chipped Gems",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "破碎宝石"))
    public static final TagPrefix gemChipped = new TagPrefix("chippedGem")
            .localization(text -> text.source("Chipped %s")
                    .translation(MinecraftLocale.ZH_CN, "破碎的%s"))
            .idPattern("chipped_%s_gem")
            .defaultTagPath("chipped_gems/%s")
            .unformattedTagPath("chipped_gems")
            .materialAmount(GTValues.M / 4)
            .materialIconType(MaterialIconType.gemChipped)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasGemProperty.and(unused -> ConfigHolder.INSTANCE.recipes.generateLowQualityGems));

    // A regular Gem worth two small Dusts.
    @Trans(key = "tag.item.c.flawed_gems",
           source = "Flawed Gems",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "有瑕宝石"))
    public static final TagPrefix gemFlawed = new TagPrefix("flawedGem")
            .localization(text -> text.source("Flawed %s")
                    .translation(MinecraftLocale.ZH_CN, "有瑕的%s"))
            .idPattern("flawed_%s_gem")
            .defaultTagPath("flawed_gems/%s")
            .unformattedTagPath("flawed_gems")
            .materialAmount(GTValues.M / 2)
            .materialIconType(MaterialIconType.gemFlawed)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasGemProperty.and(unused -> ConfigHolder.INSTANCE.recipes.generateLowQualityGems));

    // A regular Gem worth two Dusts.
    @Trans(key = "tag.item.c.flawless_gems",
           source = "Flawless Gems",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无瑕宝石"))
    public static final TagPrefix gemFlawless = new TagPrefix("flawlessGem")
            .localization(text -> text.source("Flawless %s")
                    .translation(MinecraftLocale.ZH_CN, "无瑕的%s"))
            .idPattern("flawless_%s_gem")
            .defaultTagPath("flawless_gems/%s")
            .unformattedTagPath("flawless_gems")
            .materialAmount(GTValues.M * 2)
            .maxStackSize(32)
            .materialIconType(MaterialIconType.gemFlawless)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasGemProperty);

    // A regular Gem worth four Dusts.
    @Trans(key = "tag.item.c.exquisite_gems",
           source = "Exquisite Gems",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "精致宝石"))
    public static final TagPrefix gemExquisite = new TagPrefix("exquisiteGem")
            .localization(text -> text.source("Exquisite %s")
                    .translation(MinecraftLocale.ZH_CN, "精致的%s"))
            .idPattern("exquisite_%s_gem")
            .defaultTagPath("exquisite_gems/%s")
            .unformattedTagPath("exquisite_gems")
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.gemExquisite)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasGemProperty);

    // 1/4th of a Dust.
    @Trans(key = "tag.item.c.small_dusts",
           source = "Small Piles of Dust",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小堆粉"))
    public static final TagPrefix dustSmall = new TagPrefix("smallDust")
            .localization(text -> text.source("Small Pile of %s Dust")
                    .translation(MinecraftLocale.ZH_CN, "小堆%s粉"))
            .idPattern("small_%s_dust")
            .defaultTagPath("small_dusts/%s")
            .unformattedTagPath("small_dusts")
            .materialAmount(GTValues.M / 4)
            .materialIconType(MaterialIconType.dustSmall)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasDustProperty);

    // 1/9th of a Dust.
    @Trans(key = "tag.item.c.tiny_dusts",
           source = "Tiny Piles of Dust",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小撮粉"))
    public static final TagPrefix dustTiny = new TagPrefix("tinyDust")
            .localization(text -> text.source("Tiny Pile of %s Dust")
                    .translation(MinecraftLocale.ZH_CN, "小撮%s粉"))
            .idPattern("tiny_%s_dust")
            .defaultTagPath("tiny_dusts/%s")
            .unformattedTagPath("tiny_dusts")
            .materialAmount(GTValues.M / 9)
            .materialIconType(MaterialIconType.dustTiny)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasDustProperty);

    // Dust with impurities. 1 Unit of Main Material and 1/9 - 1/4 Unit of secondary Material
    @Trans(key = "tag.item.c.impure_dusts",
           source = "Impure Dusts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "含杂粉"))
    public static final TagPrefix dustImpure = new TagPrefix("impureDust")
            .localization(text -> text.source("Impure Pile of %s Dust")
                    .translation(MinecraftLocale.ZH_CN, "含杂%s粉"))
            .idPattern("impure_%s_dust")
            .defaultTagPath("impure_dusts/%s")
            .unformattedTagPath("impure_dusts")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.dustImpure)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasOreProperty)
            .tooltip((mat, tooltips) -> tooltips.add(Component.translatable("metaitem.dust.tooltip.purify")));

    // Pure Dust worth of one Ingot or Gem.
    @Trans(key = "tag.item.c.pure_dusts",
           source = "Purified Dusts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "洁净粉"))
    public static final TagPrefix dustPure = new TagPrefix("pureDust")
            .localization(text -> text.source("Purified Pile of %s Dust")
                    .translation(MinecraftLocale.ZH_CN, "洁净%s粉"))
            .idPattern("pure_%s_dust")
            .defaultTagPath("pure_dusts/%s")
            .unformattedTagPath("pure_dusts")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.dustPure)
            .unificationEnabled(true)
            .generateItem(true)
            .generationCondition(hasOreProperty)
            .tooltip((mat, tooltips) -> tooltips.add(Component.translatable("metaitem.dust.tooltip.purify")));

    @Trans(key = "tag.item.c.dusts",
           source = "Dusts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "粉"))
    public static final TagPrefix dust = new TagPrefix("dust")
            .localization(text -> text.source("%s Dust")
                    .translation(MinecraftLocale.ZH_CN, "%s粉"))
            .defaultTagPath("dusts/%s")
            .unformattedTagPath("dusts")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.dust)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasDustProperty);

    // A Nugget.
    @Trans(key = "tag.item.c.nuggets",
           source = "Nuggets",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "粒"))
    public static final TagPrefix nugget = new TagPrefix("nugget")
            .localization(text -> text.source("%s Nugget")
                    .translation(MinecraftLocale.ZH_CN, "%s粒"))
            .defaultTagPath("nuggets/%s")
            .unformattedTagPath("nuggets")
            .materialAmount(GTValues.M / 9)
            .materialIconType(MaterialIconType.nugget)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasIngotProperty);

    // 9 Plates combined in one Item.
    @Trans(key = "tag.item.c.dense_plates",
           source = "Dense Plates",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "致密板"))
    public static final TagPrefix plateDense = new TagPrefix("densePlate")
            .localization(text -> text.source("Dense %s Plate")
                    .translation(MinecraftLocale.ZH_CN, "致密%s板"))
            .idPattern("dense_%s_plate")
            .defaultTagPath("dense_plates/%s")
            .unformattedTagPath("dense_plates")
            .materialAmount(GTValues.M * 9)
            .maxStackSize(7)
            .materialIconType(MaterialIconType.plateDense)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_DENSE));

    // 2 Plates combined in one Item
    @Trans(key = "tag.item.c.double_plates",
           source = "Double Plates",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "双层板"))
    public static final TagPrefix plateDouble = new TagPrefix("doublePlate")
            .localization(text -> text.source("Double %s Plate")
                    .translation(MinecraftLocale.ZH_CN, "双层%s板"))
            .idPattern("double_%s_plate")
            .defaultTagPath("double_plates/%s")
            .unformattedTagPath("double_plates")
            .materialAmount(GTValues.M * 2)
            .maxStackSize(32)
            .materialIconType(MaterialIconType.plateDouble)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasIngotProperty
                    .and(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE) && !mat.hasFlag(MaterialFlags.NO_SMASHING)));

    // Regular Plate made of one Ingot/Dust.
    @Trans(key = "tag.item.c.plates",
           source = "Plates",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "板"))
    public static final TagPrefix plate = new TagPrefix("plate")
            .localization(text -> text.source("%s Plate")
                    .translation(MinecraftLocale.ZH_CN, "%s板"))
            .defaultTagPath("plates/%s")
            .unformattedTagPath("plates")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.plate)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE));

    // Round made of 1 Nugget
    @Trans(key = "tag.item.c.rounds",
           source = "Rounds",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "滚珠"))
    public static final TagPrefix round = new TagPrefix("round")
            .localization(text -> text.source("%s Round")
                    .translation(MinecraftLocale.ZH_CN, "%s滚珠"))
            .defaultTagPath("rounds/%s")
            .unformattedTagPath("rounds")
            .materialAmount(GTValues.M / 9)
            .materialIconType(MaterialIconType.round)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_ROUND));

    // Foil made of 1/4 Ingot/Dust.
    @Trans(key = "tag.item.c.foils",
           source = "Foils",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "箔"))
    public static final TagPrefix foil = new TagPrefix("foil")
            .localization(text -> text.source("%s Foil")
                    .translation(MinecraftLocale.ZH_CN, "%s箔"))
            .defaultTagPath("foils/%s")
            .unformattedTagPath("foils")
            .materialAmount(GTValues.M / 4)
            .materialIconType(MaterialIconType.foil)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_FOIL));

    // Stick made of an Ingot.
    @Trans(key = "tag.item.c.rods.long",
           source = "Long Rods",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "长杆"))
    public static final TagPrefix rodLong = new TagPrefix("longRod")
            .localization(text -> text.source("Long %s Rod")
                    .translation(MinecraftLocale.ZH_CN, "长%s杆"))
            .idPattern("long_%s_rod")
            .defaultTagPath("rods/long/%s")
            .unformattedTagPath("rods/long")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.rodLong)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_LONG_ROD));

    // Stick made of half an Ingot.
    @Trans(key = "tag.item.c.rods",
           source = "Rods",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "杆"))
    public static final TagPrefix rod = new TagPrefix("rod")
            .localization(text -> text.source("%s Rod")
                    .translation(MinecraftLocale.ZH_CN, "%s杆"))
            .defaultTagPath("rods/%s")
            .unformattedTagPath("rods")
            .materialAmount(GTValues.M / 2)
            .materialIconType(MaterialIconType.rod)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_ROD));

    // consisting out of 1/8 Ingot or 1/4 Stick.
    @Trans(key = "tag.item.c.bolts",
           source = "Bolts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "螺栓"))
    public static final TagPrefix bolt = new TagPrefix("bolt")
            .localization(text -> text.source("%s Bolt")
                    .translation(MinecraftLocale.ZH_CN, "%s螺栓"))
            .defaultTagPath("bolts/%s")
            .unformattedTagPath("bolts")
            .materialAmount(GTValues.M / 8)
            .materialIconType(MaterialIconType.bolt)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_BOLT_SCREW));

    // consisting out of 1/9 Ingot.
    @Trans(key = "tag.item.c.screws",
           source = "Screws",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "螺丝"))
    public static final TagPrefix screw = new TagPrefix("screw")
            .localization(text -> text.source("%s Screw")
                    .translation(MinecraftLocale.ZH_CN, "%s螺丝"))
            .defaultTagPath("screws/%s")
            .unformattedTagPath("screws")
            .materialAmount(GTValues.M / 9)
            .materialIconType(MaterialIconType.screw)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_BOLT_SCREW));

    // consisting out of 1/2 Stick.
    @Trans(key = "tag.item.c.rings",
           source = "Rings",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "环"))
    public static final TagPrefix ring = new TagPrefix("ring")
            .localization(text -> text.source("%s Ring")
                    .translation(MinecraftLocale.ZH_CN, "%s环"))
            .defaultTagPath("rings/%s")
            .unformattedTagPath("rings")
            .materialAmount(GTValues.M / 4)
            .materialIconType(MaterialIconType.ring)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_RING));

    // consisting out of 1 Fine Wire.
    @Trans(key = "tag.item.c.small_springs",
           source = "Small Springs",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小型弹簧"))
    public static final TagPrefix springSmall = new TagPrefix("smallSpring")
            .localization(text -> text.source("Small %s Spring")
                    .translation(MinecraftLocale.ZH_CN, "小型%s弹簧"))
            .idPattern("small_%s_spring")
            .defaultTagPath("small_springs/%s")
            .unformattedTagPath("small_springs")
            .materialAmount(GTValues.M / 4)
            .materialIconType(MaterialIconType.springSmall)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(
                    mat -> mat.hasFlag(MaterialFlags.GENERATE_SPRING_SMALL) && !mat.hasFlag(MaterialFlags.NO_SMASHING));

    // consisting out of 2 Sticks.
    @Trans(key = "tag.item.c.springs",
           source = "Springs",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "弹簧"))
    public static final TagPrefix spring = new TagPrefix("spring")
            .localization(text -> text.source("%s Spring")
                    .translation(MinecraftLocale.ZH_CN, "%s弹簧"))
            .defaultTagPath("springs/%s")
            .unformattedTagPath("springs")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.spring)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(
                    mat -> mat.hasFlag(MaterialFlags.GENERATE_SPRING) && !mat.hasFlag(MaterialFlags.NO_SMASHING));

    // consisting out of 1/8 Ingot or 1/4 Wire.
    @Trans(key = "tag.item.c.fine_wires",
           source = "Fine Wires",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "细导线"))
    public static final TagPrefix wireFine = new TagPrefix("fineWire")
            .localization(text -> text.source("Fine %s Wire")
                    .translation(MinecraftLocale.ZH_CN, "细%s导线"))
            .idPattern("fine_%s_wire")
            .defaultTagPath("fine_wires/%s")
            .unformattedTagPath("fine_wires")
            .materialAmount(GTValues.M / 8)
            .materialIconType(MaterialIconType.wireFine)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_FINE_WIRE));

    // consisting out of 4 Plates, 1 Ring and 1 Screw.
    @Trans(key = "tag.item.c.rotors",
           source = "Rotors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "转子"))
    public static final TagPrefix rotor = new TagPrefix("rotor")
            .localization(text -> text.source("%s Rotor")
                    .translation(MinecraftLocale.ZH_CN, "%s转子"))
            .defaultTagPath("rotors/%s")
            .unformattedTagPath("rotors")
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.rotor)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_ROTOR));

    // Consisting of 1 Plate.
    @Trans(key = "tag.item.c.small_gears",
           source = "Small Gears",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小型齿轮"))
    public static final TagPrefix gearSmall = new TagPrefix("smallGear")
            .localization(text -> text.source("Small %s Gear")
                    .translation(MinecraftLocale.ZH_CN, "小型%s齿轮"))
            .idPattern("small_%s_gear")
            .defaultTagPath("small_gears/%s")
            .unformattedTagPath("small_gears")
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.gearSmall)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_SMALL_GEAR));

    // Consisting of 4 Plates.
    @Trans(key = "tag.item.c.gears",
           source = "Gears",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "齿轮"))
    public static final TagPrefix gear = new TagPrefix("gear")
            .localization(text -> text.source("%s Gear")
                    .translation(MinecraftLocale.ZH_CN, "%s齿轮"))
            .defaultTagPath("gears/%s")
            .unformattedTagPath("gears")
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.gear)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_GEAR));

    // 3/4 of a Plate or Gem used to shape a Lens. Normally only used on Transparent Materials.
    @Trans(key = "tag.item.c.lenses",
           source = "Lenses",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "透镜"))
    public static final TagPrefix lens = new TagPrefix("lens")
            .localization(text -> text.source("%s Lens")
                    .translation(MinecraftLocale.ZH_CN, "%s透镜"))
            .defaultTagPath("lenses/%s")
            .unformattedTagPath("lenses")
            .materialAmount((GTValues.M * 3) / 4)
            .materialIconType(MaterialIconType.lens)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(mat -> mat.hasFlag(MaterialFlags.GENERATE_LENS));

    @Trans(key = "tag.item.c.dyes",
           source = "Dyes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "染料"))
    public static final TagPrefix dye = new TagPrefix("dye")
            .localization(text -> text.source("%s Dye")
                    .translation(MinecraftLocale.ZH_CN, "%s染料"))
            .defaultTagPath("dyes/%s")
            .unformattedTagPath("dyes")
            .materialAmount(-1);

    // made of 4 Ingots.
    public static final TagPrefix toolHeadBuzzSaw = new TagPrefix("buzzSawBlade")
            .localization(text -> text.source("%s Buzzsaw Blade")
                    .translation(MinecraftLocale.ZH_CN, "%s圆锯锯片"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.toolHeadBuzzSaw)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasNoCraftingToolProperty.and(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE))
                    .and(mat -> mat.getProperty(PropertyKey.TOOL).hasType(GTToolType.BUZZSAW_LV)));

    // made of 1 Ingots.
    public static final TagPrefix toolHeadScrewdriver = new TagPrefix("screwdriverTip")
            .localization(text -> text.source("%s Screwdriver Tip")
                    .translation(MinecraftLocale.ZH_CN, "%s螺丝刀刀头"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.toolHeadScrewdriver)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasNoCraftingToolProperty.and(mat -> mat.hasFlag(MaterialFlags.GENERATE_LONG_ROD))
                    .and(mat -> mat.getProperty(PropertyKey.TOOL).hasType(GTToolType.SCREWDRIVER_LV)));

    // made of 4 Ingots.
    public static final TagPrefix toolHeadDrill = new TagPrefix("drillHead")
            .localization(text -> text.source("%s Drill Head")
                    .translation(MinecraftLocale.ZH_CN, "%s钻头"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.toolHeadDrill)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasToolProperty.and(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE))
                    .and(mat -> mat.getProperty(PropertyKey.TOOL).hasType(GTToolType.DRILL_LV)));

    // made of 2 Ingots.
    public static final TagPrefix toolHeadChainsaw = new TagPrefix("chainsawHead")
            .localization(text -> text.source("%s Chainsaw Head")
                    .translation(MinecraftLocale.ZH_CN, "%s链锯头"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M * 2)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.toolHeadChainsaw)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasToolProperty.and(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE))
                    .and(mat -> mat.getProperty(PropertyKey.TOOL).hasType(GTToolType.CHAINSAW_LV)));

    // made of 4 Ingots.
    public static final TagPrefix toolHeadWrench = new TagPrefix("wrenchTip")
            .localization(text -> text.source("%s Wrench Tip")
                    .translation(MinecraftLocale.ZH_CN, "%s扳手顶"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.toolHeadWrench)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasNoCraftingToolProperty.and(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE))
                    .and(mat -> mat.getProperty(PropertyKey.TOOL).hasType(GTToolType.WRENCH_LV)));

    public static final TagPrefix toolHeadWireCutter = new TagPrefix("wireCutterHead")
            .localization(text -> text.source("%s Wire Cutter Head")
                    .translation(MinecraftLocale.ZH_CN, "%s剪线钳钳头"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M * 4)
            .maxStackSize(16)
            .materialIconType(MaterialIconType.toolHeadWireCutter)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasNoCraftingToolProperty.and(mat -> mat.hasFlag(MaterialFlags.GENERATE_PLATE))
                    .and(mat -> mat.getProperty(PropertyKey.TOOL).hasType(GTToolType.WIRE_CUTTER_LV)));

    // made of 5 Ingots.
    public static final TagPrefix turbineBlade = new TagPrefix("turbineBlade")
            .localization(text -> text.source("%s Turbine Blade")
                    .translation(MinecraftLocale.ZH_CN, "%s涡轮扇叶"))
            .itemTable(() -> GTMaterialItems.MATERIAL_ITEMS)
            .materialAmount(GTValues.M * 10)
            .materialIconType(MaterialIconType.turbineBlade)
            .unificationEnabled(true)
            .enableRecycling()
            .generateItem(true)
            .generationCondition(hasRotorProperty
                    .and(m -> m.hasFlags(MaterialFlags.GENERATE_BOLT_SCREW, MaterialFlags.GENERATE_PLATE) &&
                            !m.hasProperty(PropertyKey.GEM)));

    // Storage Block consisting out of 9 Ingots/Gems/Dusts.
    @Trans(key = "tag.item.c.storage_blocks",
           source = "Storage Blocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存储方块"))
    public static final TagPrefix block = new TagPrefix("block")
            .localization(text -> text.source("Block of %s")
                    .translation(MinecraftLocale.ZH_CN, "%s块"))
            .defaultTagPath("storage_blocks/%s")
            .unformattedTagPath("storage_blocks")
            .materialAmount(GTValues.M * 9)
            .materialIconType(MaterialIconType.block)
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .generateBlock(true)
            .generationCondition(material -> material.hasProperty(PropertyKey.INGOT) ||
                    material.hasProperty(PropertyKey.GEM) || material.hasFlag(MaterialFlags.FORCE_GENERATE_BLOCK))
            .unificationEnabled(true)
            .enableRecycling();

    public static final TagPrefix log = new TagPrefix("log")
            .localization(text -> text.source("%s Log")
                    .translation(MinecraftLocale.ZH_CN, "%s原木"))
            .unformattedTagPath("logs", true);
    public static final TagPrefix planks = new TagPrefix("planks")
            .localization(text -> text.source("%s Planks")
                    .translation(MinecraftLocale.ZH_CN, "%s木板"))
            .unformattedTagPath("planks", true);
    public static final TagPrefix slab = new TagPrefix("slab")
            .localization(text -> text.source("%s Slab")
                    .translation(MinecraftLocale.ZH_CN, "%s台阶"))
            .unformattedTagPath("slabs", true);
    public static final TagPrefix stairs = new TagPrefix("stairs")
            .localization(text -> text.source("%s Stairs")
                    .translation(MinecraftLocale.ZH_CN, "%s楼梯"))
            .unformattedTagPath("stairs", true);
    public static final TagPrefix fence = new TagPrefix("fence")
            .localization(text -> text.source("%s Fence")
                    .translation(MinecraftLocale.ZH_CN, "%s栅栏"))
            .unformattedTagPath("fences");
    public static final TagPrefix fenceGate = new TagPrefix("fenceGate")
            .localization(text -> text.source("%s Fence Gate")
                    .translation(MinecraftLocale.ZH_CN, "%s栅栏门"))
            .unformattedTagPath("fence_gates");
    public static final TagPrefix door = new TagPrefix("door")
            .localization(text -> text.source("%s Door")
                    .translation(MinecraftLocale.ZH_CN, "%s门"))
            .unformattedTagPath("doors", true);

    // Prefix to determine which kind of Rock this is.
    // Also has a base tag path of only the material, for things like obsidian etc.
    public static final TagPrefix rock = new TagPrefix("rock")
            .localization(text -> text.source("%s")
                    .translation(MinecraftLocale.ZH_CN, "%s"))
            .defaultTagPath("%ss")
            .tagLocalization("endstones", text -> text.source("End Stone")
                    .translation(MinecraftLocale.ZH_CN, "末地石"))
            .tagLocalization("marbles", text -> text.source("Marble")
                    .translation(MinecraftLocale.ZH_CN, "大理岩"))
            .miningToolTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .unificationEnabled(false)
            .generateBlock(true) // generate a block but not really, for TagPrefix#setIgnoredBlock
            .generationCondition((material) -> false);

    @Trans(key = "tag.item.c.frames",
           source = "Frames",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "框架"))
    public static final TagPrefix frameGt = new TagPrefix("frame")
            .localization(text -> text.source("%s Frame")
                    .translation(MinecraftLocale.ZH_CN, "%s框架"))
            .defaultTagPath("frames/%s")
            .unformattedTagPath("frames")
            .materialAmount(GTValues.M * 2)
            .materialIconType(MaterialIconType.frameGt)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .unificationEnabled(true)
            .enableRecycling()
            .generateBlock(true)
            .blockProperties(() -> RenderType::translucent, BlockBehaviour.Properties::noOcclusion)
            .generationCondition(material -> material.hasProperty(PropertyKey.DUST) &&
                    material.hasFlag(MaterialFlags.GENERATE_FRAME));

    // Pipes
    public static final TagPrefix pipeTinyFluid = new TagPrefix("pipeTinyFluid")
            .localization(text -> text.source("Tiny %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "微型%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M / 2)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeSmallFluid = new TagPrefix("pipeSmallFluid")
            .localization(text -> text.source("Small %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "小型%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeNormalFluid = new TagPrefix("pipeNormalFluid")
            .localization(text -> text.source("Normal %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "标准%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 3)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeLargeFluid = new TagPrefix("pipeLargeFluid")
            .localization(text -> text.source("Large %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "大型%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 6)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeHugeFluid = new TagPrefix("pipeHugeFluid")
            .localization(text -> text.source("Huge %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "巨型%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 12)
            .unificationEnabled(true)
            .enableRecycling();

    public static final TagPrefix pipeQuadrupleFluid = new TagPrefix("pipeQuadrupleFluid")
            .localization(text -> text.source("Quadruple %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "四重%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 4)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeNonupleFluid = new TagPrefix("pipeNonupleFluid")
            .localization(text -> text.source("Nonuple %s Fluid Pipe")
                    .translation(MinecraftLocale.ZH_CN, "九重%s流体管道"))
            .itemTable(() -> GTMaterialBlocks.FLUID_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 9)
            .unificationEnabled(true)
            .enableRecycling();

    public static final TagPrefix pipeSmallItem = new TagPrefix("pipeSmallItem")
            .localization(text -> text.source("Small %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "小型%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeNormalItem = new TagPrefix("pipeNormalItem")
            .localization(text -> text.source("Normal %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "标准%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 3)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeLargeItem = new TagPrefix("pipeLargeItem")
            .localization(text -> text.source("Large %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "大型%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 6)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeHugeItem = new TagPrefix("pipeHugeItem")
            .localization(text -> text.source("Huge %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "巨型%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M * 12)
            .unificationEnabled(true)
            .enableRecycling();

    public static final TagPrefix pipeSmallRestrictive = new TagPrefix("pipeSmallRestrictive")
            .localization(text -> text.source("Small Restrictive %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "小型低优先%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .materialAmount(GTValues.M)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeNormalRestrictive = new TagPrefix("pipeNormalRestrictive")
            .localization(text -> text.source("Normal Restrictive %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "标准低优先%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH).materialAmount(GTValues.M * 3)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeLargeRestrictive = new TagPrefix("pipeLargeRestrictive")
            .localization(text -> text.source("Large Restrictive %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "大型低优先%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH).materialAmount(GTValues.M * 6)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix pipeHugeRestrictive = new TagPrefix("pipeHugeRestrictive")
            .localization(text -> text.source("Huge Restrictive %s Item Pipe")
                    .translation(MinecraftLocale.ZH_CN, "巨型低优先%s物品管道"))
            .itemTable(() -> GTMaterialBlocks.ITEM_PIPE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH).materialAmount(GTValues.M * 12)
            .unificationEnabled(true)
            .enableRecycling();

    // Wires and cables
    public static final TagPrefix wireGtHex = new TagPrefix("wireGtHex")
            .localization(text -> text.source("16x %s Wire")
                    .translation(MinecraftLocale.ZH_CN, "16x%s导线"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M * 8)
            .materialIconType(MaterialIconType.wire)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix wireGtOctal = new TagPrefix("wireGtOctal")
            .localization(text -> text.source("8x %s Wire")
                    .translation(MinecraftLocale.ZH_CN, "8x%s导线"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M * 4)
            .materialIconType(MaterialIconType.wire)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix wireGtQuadruple = new TagPrefix("wireGtQuadruple")
            .localization(text -> text.source("4x %s Wire")
                    .translation(MinecraftLocale.ZH_CN, "4x%s导线"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M * 2)
            .materialIconType(MaterialIconType.wire)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix wireGtDouble = new TagPrefix("wireGtDouble")
            .localization(text -> text.source("2x %s Wire")
                    .translation(MinecraftLocale.ZH_CN, "2x%s导线"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M)
            .materialIconType(MaterialIconType.wire)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix wireGtSingle = new TagPrefix("wireGtSingle")
            .localization(text -> text.source("1x %s Wire")
                    .translation(MinecraftLocale.ZH_CN, "1x%s导线"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M / 2)
            .materialIconType(MaterialIconType.wire)
            .unificationEnabled(true)
            .enableRecycling();

    public static final TagPrefix cableGtHex = new TagPrefix("cableGtHex")
            .localization(text -> text.source("16x %s Cable")
                    .translation(MinecraftLocale.ZH_CN, "16x%s线缆"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M * 8)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix cableGtOctal = new TagPrefix("cableGtOctal")
            .localization(text -> text.source("8x %s Cable")
                    .translation(MinecraftLocale.ZH_CN, "8x%s线缆"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M * 4)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix cableGtQuadruple = new TagPrefix("cableGtQuadruple")
            .localization(text -> text.source("4x %s Cable")
                    .translation(MinecraftLocale.ZH_CN, "4x%s线缆"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M * 2)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix cableGtDouble = new TagPrefix("cableGtDouble")
            .localization(text -> text.source("2x %s Cable")
                    .translation(MinecraftLocale.ZH_CN, "2x%s线缆"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M)
            .unificationEnabled(true)
            .enableRecycling();
    public static final TagPrefix cableGtSingle = new TagPrefix("cableGtSingle")
            .localization(text -> text.source("1x %s Cable")
                    .translation(MinecraftLocale.ZH_CN, "1x%s线缆"))
            .itemTable(() -> GTMaterialBlocks.CABLE_BLOCKS)
            .miningToolTag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
            .materialAmount(GTValues.M / 2)
            .unificationEnabled(true)
            .enableRecycling();

    @Trans(key = "tag.item.c.surface_rocks",
           source = "Surface Rocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "地表岩石"))
    public static final TagPrefix surfaceRock = new TagPrefix("surfaceRock")
            .localization(text -> text.source("%s Surface Rock")
                    .translation(MinecraftLocale.ZH_CN, "地表岩石（%s）"))
            .defaultTagPath("surface_rocks/%s")
            .unformattedTagPath("surface_rocks")
            .materialAmount(GTValues.M / 3);

    public static class Conditions {

        public static final Predicate<Material> hasToolProperty = mat -> mat.hasProperty(PropertyKey.TOOL);
        public static final Predicate<Material> hasNoCraftingToolProperty = hasToolProperty
                .and(mat -> !mat.getProperty(PropertyKey.TOOL).isIgnoreCraftingTools());
        public static final Predicate<Material> hasOreProperty = mat -> mat.hasProperty(PropertyKey.ORE);
        public static final Predicate<Material> hasGemProperty = mat -> mat.hasProperty(PropertyKey.GEM);
        public static final Predicate<Material> hasDustProperty = mat -> mat.hasProperty(PropertyKey.DUST);
        public static final Predicate<Material> hasIngotProperty = mat -> mat.hasProperty(PropertyKey.INGOT);
        public static final Predicate<Material> hasBlastProperty = mat -> mat.hasProperty(PropertyKey.BLAST);
        public static final Predicate<Material> hasRotorProperty = mat -> mat.hasProperty(PropertyKey.ROTOR);
    }

    public record OreType(Supplier<BlockState> stoneType, Supplier<Material> material,
                          Supplier<BlockBehaviour.Properties> template, ResourceLocation baseModelLocation,
                          boolean isDoubleDrops, boolean isSand, boolean shouldDropAsItem) {}

    public record BlockProperties(Supplier<Supplier<RenderType>> renderType,
                                  UnaryOperator<BlockBehaviour.Properties> properties) {}

    @Getter
    public final String name;
    @Getter
    @Setter
    private String idPattern;

    protected final List<TagType> tags = new ArrayList<>();
    @Setter
    @Getter
    public String langValue;

    @Getter
    private @Nullable LocalizationDeclaration localization;

    @Getter
    @Setter
    private long materialAmount = -1;

    @Setter
    @Getter
    private boolean unificationEnabled;
    @Setter
    @Getter
    private boolean generateRecycling = false;
    @Setter
    private boolean generateItem;
    @Getter
    @Setter
    private ItemConstructor itemConstructor = TagPrefixItem::new;
    @Setter
    private boolean generateBlock;
    @Getter
    @Setter
    private BlockConstructor blockConstructor = MaterialBlock::new;
    @Getter
    @Setter
    private BlockItemConstructor blockItemConstructor = MaterialBlockItem::new;
    @Getter
    @Setter
    private BlockProperties blockProperties = new BlockProperties(() -> RenderType::translucent,
            UnaryOperator.identity());

    @Getter
    @Setter
    private @Nullable Predicate<Material> generationCondition;

    @Nullable
    @Getter
    @Setter
    private MaterialIconType materialIconType;

    @Setter
    private Supplier<Table<TagPrefix, Material, ? extends Supplier<? extends ItemLike>>> itemTable;

    @Nullable
    @Getter
    @Setter
    private BiConsumer<Material, List<Component>> tooltip;

    private final Map<Material, Collection<Supplier<? extends ItemLike>>> ignoredMaterials = new HashMap<>();
    private final Object2FloatMap<Material> materialAmounts = new Object2FloatOpenHashMap<>();

    @Getter
    @Setter
    private int maxStackSize = 64;

    @Getter
    private final List<MaterialStack> secondaryMaterials = new ArrayList<>();

    @Getter
    protected final Set<TagKey<Block>> miningToolTag = new HashSet<>();

    public TagPrefix(String name) {
        this.name = name;
        String lowerCaseUnder = getLowerCaseName();
        this.idPattern = "%s_" + lowerCaseUnder;
        this.langValue = "%s " + FormattingUtil.toEnglishName(lowerCaseUnder);
        GTRegistries.register(GTRegistries.TAG_PREFIXES, GTPM.id(lowerCaseUnder), this);
    }

    public TagPrefix localization(Consumer<LocalizationDeclaration.Builder> configure) {
        var text = new LocalizationDeclaration.Builder();
        configure.accept(text);
        localization = text.build();
        langValue = localization.source();
        return this;
    }

    private TagPrefix tagLocalization(String path, Consumer<LocalizationDeclaration.Builder> configure) {
        if (GTPM.isDataGen()) {
            var id = TagUtil.createItemTag(path).location();
            GTRegistration.REGISTRATE.addDefaultLocalization(itemTagTranslationKey(id), () -> {
                var text = new LocalizationDeclaration.Builder();
                configure.accept(text);
                return text.build();
            });
        }
        return this;
    }

    public static TagPrefix oreTagPrefix(String name, TagKey<Block> miningToolTag) {
        return new TagPrefix(name)
                .defaultTagPath("ores/%s")
                .prefixOnlyTagPath("ores_in_ground/%s")
                .unformattedTagPath("ores")
                .materialIconType(MaterialIconType.ore)
                .miningToolTag(miningToolTag)
                .unificationEnabled(true)
                .blockConstructor(OreBlock::new)
                .generationCondition(hasOreProperty);
    }

    public void addSecondaryMaterial(MaterialStack secondaryMaterial) {
        Preconditions.checkNotNull(secondaryMaterial, "secondaryMaterial");
        secondaryMaterials.add(secondaryMaterial);
    }

    public TagPrefix registerOre(Supplier<BlockState> stoneType, Supplier<Material> material,
                                 BlockBehaviour.Properties properties, ResourceLocation baseModelLocation) {
        return registerOre(stoneType, material, properties, baseModelLocation, false);
    }

    public TagPrefix registerOre(Supplier<BlockState> stoneType, Supplier<Material> material,
                                 BlockBehaviour.Properties properties, ResourceLocation baseModelLocation,
                                 boolean doubleDrops) {
        return registerOre(stoneType, material, properties, baseModelLocation, doubleDrops, false, false);
    }

    public TagPrefix registerOre(Supplier<BlockState> stoneType, Supplier<Material> material,
                                 BlockBehaviour.Properties properties, ResourceLocation baseModelLocation,
                                 boolean doubleDrops, boolean isSand, boolean shouldDropAsItem) {
        return registerOre(stoneType, material, () -> properties, baseModelLocation, doubleDrops, isSand,
                shouldDropAsItem);
    }

    public TagPrefix registerOre(Supplier<BlockState> stoneType, Supplier<Material> material,
                                 Supplier<BlockBehaviour.Properties> properties, ResourceLocation baseModelLocation,
                                 boolean doubleDrops, boolean isSand, boolean shouldDropAsItem) {
        ORES.put(this,
                new OreType(stoneType, material, properties, baseModelLocation, doubleDrops, isSand, shouldDropAsItem));
        if (shouldDropAsItem) {
            GTOreByProduct.addOreByProductPrefix(this);
        }
        return this;
    }

    public TagPrefix defaultTagPath(String path) {
        return this.defaultTagPath(path, false);
    }

    public TagPrefix defaultTagPath(String path, boolean isVanilla) {
        this.tags.add(TagType.withDefaultFormatter(path, isVanilla));
        return this;
    }

    public TagPrefix prefixTagPath(String path) {
        this.tags.add(TagType.withPrefixFormatter(path));
        return this;
    }

    public TagPrefix prefixOnlyTagPath(String path) {
        this.tags.add(TagType.withPrefixOnlyFormatter(path));
        return this;
    }

    public TagPrefix unformattedTagPath(String path) {
        return unformattedTagPath(path, false);
    }

    public TagPrefix unformattedTagPath(String path, boolean isVanilla) {
        this.tags.add(TagType.withNoFormatter(path, isVanilla));
        return this;
    }

    public TagPrefix customTagPath(String path, BiFunction<TagPrefix, Material, TagKey<Item>> formatter) {
        this.tags.add(TagType.withCustomFormatter(path, formatter));
        return this;
    }

    public TagPrefix customTagPredicate(String path, boolean isVanilla, Predicate<Material> materialPredicate) {
        this.tags.add(TagType.withCustomFilter(path, isVanilla, materialPredicate));
        return this;
    }

    public TagPrefix miningToolTag(TagKey<Block> tag) {
        this.miningToolTag.add(tag);
        return this;
    }

    public TagPrefix blockProperties(Supplier<Supplier<RenderType>> renderType,
                                     UnaryOperator<BlockBehaviour.Properties> properties) {
        return this.blockProperties(new BlockProperties(renderType, properties));
    }

    public TagPrefix enableRecycling() {
        this.generateRecycling = true;
        return this;
    }

    public long getMaterialAmount(@NotNull Material material) {
        if (material.isNull() || !isAmountModified(material)) {
            return this.materialAmount;
        }
        return (long) (GTValues.M * materialAmounts.getFloat(material));
    }

    @Unmodifiable
    public List<TagKey<Item>> getItemParentTags() {
        return tags.stream()
                .filter(TagType::isParentTag)
                .map(type -> type.getTag(this, GTMaterials.NULL))
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Item>> getItemTags(@NotNull Material mat) {
        return tags.stream()
                .filter(type -> !type.isParentTag())
                .map(type -> type.getTag(this, mat))
                .filter(Objects::nonNull)
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Item>> getAllItemTags(@NotNull Material mat) {
        return tags.stream()
                .map(type -> type.getTag(this, mat))
                .filter(Objects::nonNull)
                .toList();
    }

    /** Registers the names of the tags that are created for this material and prefix. */
    @ApiStatus.Internal
    public void localizeMaterialTags(Material material) {
        if (!GTPM.isDataGen() || !GTPM.MOD_ID.equals(material.getModid()) || material.isNull()) return;

        for (TagType type : tags) {
            if (!type.isMaterialSpecific()) continue;
            TagKey<Item> tag = type.getTag(this, material);
            if (tag == null) continue;
            ResourceLocation id = tag.location();
            if (!id.getNamespace().equals("c")) continue;

            // Ore variants share a material tag across all host rocks.
            TagPrefix prefix = id.getPath().startsWith("ores/") ? ore : this;
            GTRegistration.REGISTRATE.addDefaultLocalization(itemTagTranslationKey(id),
                    () -> material.formatLocalization(prefix.localization != null &&
                            prefix.localization.source().equals(prefix.langValue) ?
                            prefix.localization : LocalizationDeclaration.sourceText(prefix.langValue)));
        }

        if (this == crushed) {
            var oreProperty = material.getProperty(PropertyKey.ORE);
            if (oreProperty != null) {
                var washFluid = oreProperty.getWashedIn().first();
                if (!washFluid.isNull()) CustomTags.localizeChemicalBathTag(washFluid);
            }
        }
    }

    private static String itemTagTranslationKey(ResourceLocation id) {
        return id.toLanguageKey("tag.item").replace('/', '.');
    }

    @Unmodifiable
    public List<TagKey<Block>> getBlockTags(@NotNull Material mat) {
        return tags.stream()
                .filter(type -> !type.isParentTag())
                .map(type -> type.getTag(this, mat))
                .map(itemTagKey -> TagKey.create(Registries.BLOCK, itemTagKey.location()))
                .toList();
    }

    @Unmodifiable
    public List<TagKey<Block>> getAllBlockTags(@NotNull Material mat) {
        return tags.stream()
                .map(type -> type.getTag(this, mat))
                .map(itemTagKey -> TagKey.create(Registries.BLOCK, itemTagKey.location()))
                .toList();
    }

    public boolean hasItemTable() {
        return itemTable != null;
    }

    public Supplier<? extends ItemLike> getItemFromTable(Material material) {
        return itemTable.get().get(this, material);
    }

    public boolean doGenerateItem() {
        return generateItem;
    }

    public boolean doGenerateItem(Material material) {
        return generateItem && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material)) ||
                (hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null);
    }

    public boolean doGenerateBlock() {
        return generateBlock;
    }

    public boolean doGenerateBlock(Material material) {
        return generateBlock && !isIgnored(material) &&
                (generationCondition == null || generationCondition.test(material)) ||
                hasItemTable() && this.itemTable.get() != null && getItemFromTable(material) != null;
    }

    public MaterialIconType getMaterialIconType(Material material) {
        // special case emissive ores
        if (materialIconType == MaterialIconType.ore && material.hasProperty(PropertyKey.ORE)) {
            OreProperty oreProp = material.getProperty(PropertyKey.ORE);
            if (oreProp.isEmissive()) {
                return MaterialIconType.oreEmissive;
            }
        }
        return materialIconType;
    }

    public String getLowerCaseName() {
        return FormattingUtil.toLowerCaseUnderscore(this.name);
    }

    public String getUnlocalizedName() {
        return "tagprefix." + getLowerCaseName();
    }

    public MutableComponent getLocalizedName(Material material) {
        return Component.translatable(getUnlocalizedName(material), material.getLocalizedName());
    }

    @TransVariants(keyPrefix = "tagprefix.polymer",
                   value = {
                           @Variant(id = "dense_plate",
                                    source = "Dense %s Sheet",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "致密%s片")),
                           @Variant(id = "double_plate",
                                    source = "Double %s Sheet",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "双层%s片")),
                           @Variant(id = "dust",
                                    source = "%s Pulp",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s末")),
                           @Variant(id = "foil",
                                    source = "Thin %s Sheet",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "薄%s片")),
                           @Variant(id = "ingot",
                                    source = "%s Ingot",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s条")),
                           @Variant(id = "nugget",
                                    source = "%s Chip",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s颗粒")),
                           @Variant(id = "plate",
                                    source = "%s Sheet",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s片")),
                           @Variant(id = "small_dust",
                                    source = "Small Pile of %s Pulp",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小堆%s末")),
                           @Variant(id = "tiny_dust",
                                    source = "Tiny Pile of %s Pulp",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小撮%s末")),
                   })
    private static final String POLYMER_TRANSLATION_PREFIX = "tagprefix.polymer.";

    public String getUnlocalizedName(Material material) {
        String matSpecificKey = String.format("item.%s.%s", material.getModid(),
                this.idPattern.formatted(material.getName()));
        if (Language.getInstance().has(matSpecificKey)) {
            return matSpecificKey;
        }
        if (material.hasProperty(PropertyKey.POLYMER)) {
            String localizationKey = POLYMER_TRANSLATION_PREFIX + getLowerCaseName();
            // Not every polymer tag prefix gets a special name
            if (Language.getInstance().has(localizationKey)) {
                return localizationKey;
            }
        }

        return getUnlocalizedName();
    }

    public boolean isIgnored(Material material) {
        return ignoredMaterials.containsKey(material);
    }

    @SafeVarargs
    public final void setIgnored(Material material, Supplier<? extends ItemLike>... items) {
        setIgnored(material, Arrays.asList(items));
    }

    public void setIgnored(Material material, Collection<Supplier<? extends ItemLike>> items) {
        ignoredMaterials.computeIfAbsent(material, m -> new HashSet<>()).addAll(items);
        if (!items.isEmpty()) {
            ItemMaterialData.registerMaterialEntries(items, this, material);
        }
    }

    public void setIgnored(Material material, ItemLike... items) {
        // go through setIgnoredBlock to wrap if this is a block prefix
        if (this.doGenerateBlock()) {
            this.setIgnoredBlock(material, Arrays.stream(items)
                    .filter(Block.class::isInstance)
                    .map(Block.class::cast)
                    .toArray(Block[]::new));
            // also add possible item-only entries
            this.setIgnored(material, Arrays.stream(items)
                    .filter(item -> !(item instanceof Block))
                    .map(item -> (Supplier<ItemLike>) () -> item)
                    .collect(Collectors.toSet()));
        } else {
            this.setIgnored(material, Arrays.stream(items)
                    .map(item -> (Supplier<ItemLike>) () -> item)
                    .collect(Collectors.toSet()));
        }
    }

    public void setIgnoredBlock(Material material, Block... blocks) {
        this.setIgnored(material, Arrays.stream(blocks)
                .map(block -> GTMemoizer.memoizeBlockSupplier(() -> block))
                .collect(Collectors.toSet()));
    }

    public void setIgnored(Material material) {
        ignoredMaterials.computeIfAbsent(material, m -> new HashSet<>());
    }

    public void removeIgnored(Material material) {
        ignoredMaterials.remove(material);
    }

    public @Unmodifiable Map<Material, @Unmodifiable Collection<Supplier<? extends ItemLike>>> getIgnored() {
        return Collections.unmodifiableMap(ignoredMaterials);
    }

    public boolean isAmountModified(Material material) {
        return materialAmounts.containsKey(material);
    }

    public void modifyMaterialAmount(@NotNull Material material, float amount) {
        materialAmounts.put(material, amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPrefix tagPrefix = (TagPrefix) o;
        return name.equals(tagPrefix.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static Iterable<TagPrefix> values() {
        return GTRegistries.TAG_PREFIXES;
    }

    @Override
    public String toString() {
        return name;
    }

    @FunctionalInterface
    public interface ItemConstructor {

        Item create(Item.Properties properties, TagPrefix prefix, Material material);
    }

    @FunctionalInterface
    public interface BlockConstructor {

        Block create(Block.Properties properties, TagPrefix prefix, Material material);
    }

    @FunctionalInterface
    public interface BlockItemConstructor {

        BlockItem create(Block block, Item.Properties properties, TagPrefix prefix, Material material);
    }
}

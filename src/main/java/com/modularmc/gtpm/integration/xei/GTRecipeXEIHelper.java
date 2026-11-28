package com.modularmc.gtpm.integration.xei;

import com.modularmc.gtpm.api.capability.recipe.CWURecipeCapability;
import com.modularmc.gtpm.api.recipe.GTRecipeDefinition;
import com.modularmc.gtpm.api.recipe.RecipeData;
import com.modularmc.gtpm.api.recipe.chance.boost.ChanceBoostFunction;
import com.modularmc.gtpm.api.recipe.chance.logic.ChanceLogic;
import com.modularmc.gtpm.api.recipe.content.Content;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "recipe.voltage",
                                source = "Usage: %s A @ %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "功耗：%sA @ %s")),
                       @Variant(id = "recipe.total_eu",
                                source = "Total Usage: %s EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "总功率：%s EU/t")),
                       @Variant(id = "recipe.not_consumed",
                                source = "Does not get consumed in the process",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "不在加工中消耗")),
                       @Variant(id = "recipe.chance",
                                source = "Chance: %s +%s/tier",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "概率：%s + %s/电压等级")),
                       @Variant(id = "recipe.explosive",
                                source = "Explosive: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "爆炸物：%s")),
                       @Variant(id = "recipe.dimensions",
                                source = "Dimensions: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "维度：%s")),
                       @Variant(id = "recipe.scan_for_research",
                                source = "Scan for Assembly Line",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "装配线配方扫描")),
                       @Variant(id = "gui.content.chance_base",
                                source = "Base Chance: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基础概率：%s%%")),
                       @Variant(id = "gui.content.chance_no_boost",
                                source = "Chance: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "概率：%s%%")),
                       @Variant(id = "gui.content.chance_tier_boost_minus",
                                source = "Bonus Chance: -%s%%/tier",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "奖励概率：- %s%%/电压等级")),
                       @Variant(id = "gui.content.per_tick",
                                source = "§aConsumed/Produced Per Tick§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a每刻（tick）消耗/产生§r")),
                       @Variant(id = "gui.content.range",
                                source = "%s-%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s-%s")),
                       @Variant(id = "gui.content.tips.per_second_short",
                                source = "§a/second§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a/秒§r")),
                       @Variant(id = "gui.content.units.per_second",
                                source = "/s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "/s")),
                       @Variant(id = "jei.bedrock_fluid_diagram",
                                source = "Bedrock Fluid Diagram",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基岩流体生成信息")),
                       @Variant(id = "jei.bedrock_ore_diagram",
                                source = "Bedrock Ore Diagram",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基岩矿脉生成信息")),
                       @Variant(id = "jei.fluid.dep_amount_hover",
                                source = "The amount the vein will be depleted by",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消耗后消耗的量")),
                       @Variant(id = "jei.fluid.dep_chance_hover",
                                source = "The percentage chance for the vein to be depleted upon harvest",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "开采流体矿脉时消耗的概率")),
                       @Variant(id = "jei.fluid.dep_yield_hover",
                                source = "The maximum yield of the vein when it is fully depleted",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体矿脉殆尽后能开采的最大流体量")),
                       @Variant(id = "jei.fluid.depleted_rate",
                                source = "Depleted Yield: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "殆尽后产量：%d")),
                       @Variant(id = "jei.fluid.depletion_amount",
                                source = "Depletion Amount: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消耗量：%d")),
                       @Variant(id = "jei.fluid.depletion_chance",
                                source = "Depletion Chance: %d%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消耗概率：%d%%")),
                       @Variant(id = "jei.fluid.dimension",
                                source = "Dimensions:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "维度：")),
                       @Variant(id = "jei.fluid.max_hover",
                                source = "The maximum yield that any fluid vein of this fluid can have",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体矿脉所能具有的的最大产量")),
                       @Variant(id = "jei.fluid.max_yield",
                                source = "Maximum Yield: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大产量：%d")),
                       @Variant(id = "jei.fluid.min_hover",
                                source = "The minimum yield that any fluid vein of this fluid can have",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体矿脉所能具有的的最小产量")),
                       @Variant(id = "jei.fluid.min_yield",
                                source = "Minimum Yield: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最小产量：%d")),
                       @Variant(id = "jei.fluid.vein_weight",
                                source = "Vein Weight: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉权重：%d")),
                       @Variant(id = "jei.fluid.weight_hover",
                                source = "The Weight of the vein. Hover over the fluid to see any possible biome modifications",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "流体矿脉的生成权重。鼠标悬于图标可查看该流体在特定生物群系中特殊的生成权重")),
                       @Variant(id = "jei.materials.average_mass",
                                source = "Average mass: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "平均质量：%d")),
                       @Variant(id = "jei.materials.average_neutrons",
                                source = "Average neutrons: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "平均中子数：%d")),
                       @Variant(id = "jei.materials.average_protons",
                                source = "Average protons: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "平均质子数：%d")),
                       @Variant(id = "jei.multiblock_info",
                                source = "Multiblock Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "多方块结构信息")),
                       @Variant(id = "jei.ore.between.0",
                                source = "Between Ore",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "中层矿石")),
                       @Variant(id = "jei.ore.between.1",
                                source = "Spawns in the middle %d layers of the vein, with other ores",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "和其他矿石一起生成在矿脉的中间%d层")),
                       @Variant(id = "jei.ore.biome_weighting",
                                source = "§d%s Weight: §3%d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§d%s的生成权重：§3%d")),
                       @Variant(id = "jei.ore.biome_weighting_no_spawn",
                                source = "§d%s Weight: §cCannot Spawn",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§d%s的生成权重：§c无法生成")),
                       @Variant(id = "jei.ore.biome_weighting_title",
                                source = "§dModified Biome Total Weights:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§d特定生物群系中的例外权重：")),
                       @Variant(id = "jei.ore.ore_weight",
                                source = "Weight in vein: %d%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉内生成权重：%d%%")),
                       @Variant(id = "jei.ore.primary.0",
                                source = "Top Ore",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "顶层矿石")),
                       @Variant(id = "jei.ore.primary.1",
                                source = "Spawns in the top %d layers of the vein",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "在矿脉的顶部%d层生成")),
                       @Variant(id = "jei.ore.secondary.0",
                                source = "Bottom Ore",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "底层矿石")),
                       @Variant(id = "jei.ore.secondary.1",
                                source = "Spawns in the bottom %d layers of the vein",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "在矿脉的底部%d层生成")),
                       @Variant(id = "jei.ore.sporadic.0",
                                source = "Sporadic Ore",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "散布矿石")),
                       @Variant(id = "jei.ore.sporadic.1",
                                source = "Spawns anywhere in the vein",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "生成在矿脉的任何地方")),
                       @Variant(id = "jei.ore.surface_rock.0",
                                source = "Surface Rocks with this material denote vein spawn locations.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "这种地表岩石标志着矿脉的生成位置。")),
                       @Variant(id = "jei.ore.surface_rock.1",
                                source = "They can be broken for 3 Tiny Piles of the dust, with Fortune giving a bonus.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "挖掘掉落3小撮粉受时运加成")),
                       @Variant(id = "jei.ore_processing_diagram",
                                source = "Ore Processing Diagram",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿物处理流程图")),
                       @Variant(id = "jei.ore_vein_diagram",
                                source = "Ore Vein Diagram",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉生成信息")),
                       @Variant(id = "jei.ore_vein_diagram.chance",
                                source = "§eChance: %s§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e概率：%s§r")),
                       @Variant(id = "jei.ore_vein_diagram.dimensions",
                                source = "Dimensions:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "维度：")),
                       @Variant(id = "jei.ore_vein_diagram.weight",
                                source = "Weight: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "权重：%s")),
                       @Variant(id = "jei.programmed_circuit",
                                source = "Programmed Circuit Page",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "编程电路页")),
                       @Variant(id = "gui.content.chance_base_logic",
                                source = "Base Chance: %s%% (%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基础概率：%s%% (%s)")),
                       @Variant(id = "gui.content.chance_boosted",
                                source = "Chance at Tier: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "当前等级概率：%s%%")),
                       @Variant(id = "gui.content.chance_boosted_logic",
                                source = "Chance at Tier: %s%% (%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "当前等级概率：%s%%（%s）")),
                       @Variant(id = "gui.content.chance_nc",
                                source = "§cNot Consumed§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c不会被消耗§r")),
                       @Variant(id = "gui.content.chance_no_boost_logic",
                                source = "Chance: %s%% (%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "概率：%s%%（%s）")),
                       @Variant(id = "recipe.duration",
                                source = "Duration: %s secs",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "耗时：%s秒")),
                       @Variant(id = "recipe.max_eu",
                                source = "Max. EU: %s EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大功率：%s EU")),
                       @Variant(id = "recipe.total",
                                source = "Total: %s EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "总计：%s EU")),
               })
public final class GTRecipeXEIHelper {

    public static final int LINE_HEIGHT = 10;

    private GTRecipeXEIHelper() {}

    @NotNull
    public static List<Component> getRecipeParaText(GTRecipeDefinition recipe, int duration, long eu) {
        List<Component> texts = new ArrayList<>();
        if (!RecipeData.getBoolean(recipe.data, "hide_duration")) {
            texts.add(Component.translatable("gtpm.recipe.duration", FormattingUtil.formatNumbers(duration / 20f)));
        }
        if (eu != 0) {
            long euTotal = Math.abs(eu) * duration;
            // Computation recipes store duration as total CWU, so total EU needs the minimum CWUt divisor.
            if (RecipeData.getBoolean(recipe.data, "duration_is_total_cwu") &&
                    recipe.tickInputs.containsKey(CWURecipeCapability.CAP)) {
                int minimumCWUt = Math.max(recipe.tickInputs.get(CWURecipeCapability.CAP).stream()
                        .map(Content::getContent).mapToInt(CWURecipeCapability.CAP::of).sum(), 1);
                texts.add(Component.translatable("gtpm.recipe.max_eu",
                        FormattingUtil.formatNumbers(euTotal / minimumCWUt)));
            } else {
                texts.add(Component.translatable("gtpm.recipe.total", FormattingUtil.formatNumbers(euTotal)));
            }
        }

        return texts;
    }

    public static void setConsumedChance(Content content, ChanceLogic logic, List<Component> tooltips, int recipeTier,
                                         int chanceTier, ChanceBoostFunction function) {
        if (content.chance >= ChanceLogic.getMaxChancedValue()) {
            return;
        }

        int boostedChance = function.getBoostedChance(content, recipeTier, chanceTier);
        if (boostedChance == 0) {
            tooltips.add(Component.translatable("gtpm.gui.content.chance_nc"));
            return;
        }

        float baseChanceFloat = 100f * content.chance / content.maxChance;
        if (content.tierChanceBoost != 0) {
            float boostedChanceFloat = 100f * boostedChance / content.maxChance;

            if (logic != ChanceLogic.NONE && logic != ChanceLogic.OR) {
                tooltips.add(Component.translatable("gtpm.gui.content.chance_base_logic",
                        FormattingUtil.formatNumber2Places(baseChanceFloat), logic.getTranslation())
                        .withStyle(ChatFormatting.YELLOW));
            } else {
                tooltips.add(FormattingUtil.formatPercentage2Places("gtpm.gui.content.chance_base",
                        baseChanceFloat));
            }

            String key = "gtpm.gui.content.chance_tier_boost_" +
                    ((content.tierChanceBoost > 0) ? "plus" : "minus");
            tooltips.add(FormattingUtil.formatPercentage2Places(key,
                    Math.abs(100f * content.tierChanceBoost / content.maxChance)));

            if (logic != ChanceLogic.NONE && logic != ChanceLogic.OR) {
                tooltips.add(Component.translatable("gtpm.gui.content.chance_boosted_logic",
                        FormattingUtil.formatNumber2Places(boostedChanceFloat), logic.getTranslation())
                        .withStyle(ChatFormatting.YELLOW));
            } else {
                tooltips.add(FormattingUtil.formatPercentage2Places("gtpm.gui.content.chance_boosted",
                        boostedChanceFloat));
            }
        } else if (logic != ChanceLogic.NONE && logic != ChanceLogic.OR) {
            tooltips.add(Component.translatable("gtpm.gui.content.chance_no_boost_logic",
                    FormattingUtil.formatNumber2Places(baseChanceFloat), logic.getTranslation())
                    .withStyle(ChatFormatting.YELLOW));
        } else {
            tooltips.add(FormattingUtil.formatPercentage2Places("gtpm.gui.content.chance_no_boost",
                    baseChanceFloat));
        }
    }
}

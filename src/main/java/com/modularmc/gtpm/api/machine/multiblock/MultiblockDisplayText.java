package com.modularmc.gtpm.api.machine.multiblock;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.capability.recipe.FluidRecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.ItemRecipeCapability;
import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.misc.EnergyContainerList;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.recipe.RecipeHelper;
import com.modularmc.gtpm.api.recipe.ingredient.IntProviderFluidIngredient;
import com.modularmc.gtpm.api.recipe.ingredient.IntProviderIngredient;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Consumer;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "multiblock.validation_failed",
                                source = "Invalid amount of inputs/outputs.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输入仓/输出仓数量无效。")),
                       @Variant(id = "multiblock.multiple_recipemaps.header",
                                source = "Machine Mode:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器模式：")),
                       @Variant(id = "multiblock.multiple_recipemaps.tooltip",
                                source = "Screwdriver the controller to change which machine mode to use.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "使用螺丝刀右键控制器以更改机器模式。")),
                       @Variant(id = "multiblock.multiple_recipemaps_recipes.tooltip",
                                source = "Machine Modes: §e%s§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器模式：§e%s§r")),
                       @Variant(id = "multiblock.multiple_recipemaps.switch_message",
                                source = "The machine must be off to switch modes!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器必须关闭才能切换模式！")),
                       @Variant(id = "multiblock.batch_enabled",
                                source = "- %dx from Batching",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "- %dx 来自批处理")),
                       @Variant(id = "multiblock.energy_consumption",
                                source = "Energy Usage: %s EU/t (%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能量消耗：%s EU/t（%s）")),
                       @Variant(id = "multiblock.generation_eu",
                                source = "Outputting: §a%s EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出功率：§a%s EU/t")),
                       @Variant(id = "multiblock.idling",
                                source = "§6Idling.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6待机。")),
                       @Variant(id = "multiblock.max_energy_per_tick",
                                source = "Max EU/t: §a%s (%s§r)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "最大功率：§a%s EU/t（%s§r）")),
                       @Variant(id = "multiblock.output_line.0",
                                source = "%s x §e%s§r (%ss/ea)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s x §e%s§r（%ss/ea）")),
                       @Variant(id = "multiblock.output_line.1",
                                source = "%s x §e%s§r (%s/s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s x §e%s§r（%s/s）")),
                       @Variant(id = "multiblock.output_line.2",
                                source = "%s ≈ §e%s§r (%ss/ea)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s ≈ §e%s§r（%ss/ea）")),
                       @Variant(id = "multiblock.output_line.3",
                                source = "%s ≈ §e%s§r (%s/s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s ≈ §e%s§r （%s/s）")),
                       @Variant(id = "multiblock.progress",
                                source = "Progress: %ss / %ss (%s%%)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "进度：%ss / %ss（%s%%）")),
                       @Variant(id = "multiblock.running",
                                source = "Running perfectly.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "运行正常。")),
                       @Variant(id = "multiblock.subtick_parallels",
                                source = "- %dx from Overclocking",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "- %dx 来自超频")),
                       @Variant(id = "multiblock.total_runs",
                                source = "Performing %d Recipes at once",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "同时处理%d个配方")),
                       @Variant(id = "multiblock.universal.distinct.info",
                                source = "If enabled, each Item Input Bus will be treated as fully distinct from each other for recipe lookup. Useful for things like Programmed Circuits, Extruder Shapes, etc.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "启用时，每个物品总线将在配方识别中被视为完全独立。对于消耗材料相同，使用的非消耗材料（模头、透镜等）不同则产物也不同的配方来说作用显著。")),
                       @Variant(id = "multiblock.universal.no_problems",
                                source = "No Maintenance Problems!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无故障！")),
                       @Variant(id = "multiblock.universal.problem.crowbar",
                                source = "§7That doesn't belong there. (§aCrowbar§7)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7这东西不属于这儿。（§a撬棍§7）")),
                       @Variant(id = "multiblock.universal.problem.hard_hammer",
                                source = "§7Plating is dented. (§aHard Hammer§7)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7外壳有些凹痕。（§a锻造锤§7）")),
                       @Variant(id = "multiblock.universal.problem.screwdriver",
                                source = "§7Screws are loose. (§aScrewdriver§7)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7螺丝没拧紧。（§a螺丝刀§7）")),
                       @Variant(id = "multiblock.universal.problem.soft_mallet",
                                source = "§7Something is stuck. (§aSoft Mallet§7)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7有什么东西卡住了。（§a软锤§7）")),
                       @Variant(id = "multiblock.universal.problem.wire_cutter",
                                source = "§7Wires burned out. (§aWire Cutter§7)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7有几根线烧焦了（§a剪线钳§7）")),
                       @Variant(id = "multiblock.universal.problem.wrench",
                                source = "§7Pipe is loose. (§aWrench§7)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7管道松动了。（§a扳手§7）")),
                       @Variant(id = "multiblock.waiting",
                                source = "WARNING: Machine is waiting.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "警告：机器待机")),
                       @Variant(id = "multiblock.work_paused",
                                source = "Work Paused.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "暂停。")),
                       @Variant(id = "gui.machinemode",
                                source = "Active Machine Mode: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "当前机器模式：%s")),
                       @Variant(id = "multiblock.computation.max",
                                source = "Max CWU/t: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大算力：%s")),
                       @Variant(id = "multiblock.computation.not_enough_computation",
                                source = "Machine needs more computation!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器需要更多算力！")),
                       @Variant(id = "multiblock.computation.usage",
                                source = "Using: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用：%s")),
                       @Variant(id = "multiblock.invalid_structure",
                                source = "Invalid structure.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "结构无效。")),
                       @Variant(id = "multiblock.invalid_structure.tooltip",
                                source = "This block is a controller of the multiblock structure. For building help, see structure template in JEI.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "该方块是多方块结构的控制器，请查看该方块的JEI界面以获取搭建图示。")),
                       @Variant(id = "multiblock.max_energy_per_tick_amps",
                                source = "Max EU/t: %s (%sA %s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大功率：%s（%sA %s）")),
                       @Variant(id = "multiblock.max_energy_per_tick_hover",
                                source = "The maximum EU/t available for running recipes or overclocking",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "可用于运行配方或超频的最大EU/t")),
                       @Variant(id = "multiblock.max_recipe_tier",
                                source = "Max Recipe Tier: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大配方等级：%s")),
                       @Variant(id = "multiblock.max_recipe_tier_hover",
                                source = "The maximum tier of recipes that can be run",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可运行配方的最大等级")),
                       @Variant(id = "multiblock.not_enough_energy",
                                source = "WARNING: Machine needs more energy.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "警告：能量不足。")),
                       @Variant(id = "multiblock.not_enough_energy_output",
                                source = "WARNING: Energy Dynamo Tier Too Low!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "警告：能源/动力仓等级过低！")),
                       @Variant(id = "multiblock.progress_percent",
                                source = "Progress: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "进度：%s%%")),
                       @Variant(id = "multiblock.turbine.energy_per_tick_maxed",
                                source = "Energy Output: %s EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能量输出：%s EU/t")),
                       @Variant(id = "multiblock.turbine.fuel_needed",
                                source = "Consumes %s per %s ticks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "消耗量：%s / %s ticks")),
                       @Variant(id = "multiblock.universal.has_problems",
                                source = "Has Maintenance Problems!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存在故障！")),
                       @Variant(id = "multiblock.universal.muffler_obstructed",
                                source = "Muffler Hatch is Obstructed!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消声仓受阻！")),
                       @Variant(id = "multiblock.universal.muffler_obstructed.tooltip",
                                source = "Muffler Hatch must have a block of airspace in front of it.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "消音仓口前方必须有一格空气。")),
               })
public class MultiblockDisplayText {

    private static final Component EMPTY_COMPONENT = Component.empty();

    /**
     * Construct a new Multiblock Display Text builder.
     * <br>
     * Automatically adds the "Invalid Structure" line if the structure is not formed.
     */
    public static Builder builder(List<Component> textList, boolean isStructureFormed) {
        return builder(textList, isStructureFormed, true);
    }

    public static Builder builder(List<Component> textList, boolean isStructureFormed,
                                  boolean showIncompleteStructureWarning) {
        return new Builder(textList, isStructureFormed, showIncompleteStructureWarning);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {

        private final List<Component> textList;
        private final boolean isStructureFormed;

        private boolean isWorkingEnabled, isActive;

        // Keys for the three-state working system, can be set custom by multiblocks.
        private String idlingKey = "gtpm.multiblock.idling";
        private String pausedKey = "gtpm.multiblock.work_paused";
        private String runningKey = "gtpm.multiblock.running";

        private Builder(List<Component> textList, boolean isStructureFormed,
                        boolean showIncompleteStructureWarning) {
            this.textList = textList;
            this.isStructureFormed = isStructureFormed;

            if (!isStructureFormed && showIncompleteStructureWarning) {
                MutableComponent base = Component.translatable("gtpm.multiblock.invalid_structure")
                        .withStyle(ChatFormatting.RED);
                Component hover = Component.translatable("gtpm.multiblock.invalid_structure.tooltip")
                        .withStyle(ChatFormatting.GRAY);
                textList.add(base
                        .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover))));
            }
        }

        /**
         * Set the current working enabled and active status of this multiblock, used by many line addition calls.
         */
        public Builder setWorkingStatus(boolean isWorkingEnabled, boolean isActive) {
            this.isWorkingEnabled = isWorkingEnabled;
            this.isActive = isActive;
            return this;
        }

        /**
         * Set custom translation keys for the three-state "Idling", "Paused", "Running" display text.
         * <strong>You still must call {@link Builder#addWorkingStatusLine()} for these to appear!</strong>
         * <br>
         * Pass any key as null for it to continue to use the default key.
         *
         * @param idlingKey  The translation key for the Idle state, or "!isActive && isWorkingEnabled".
         * @param pausedKey  The translation key for the Paused state, or "!isWorkingEnabled".
         * @param runningKey The translation key for the Running state, or "isActive".
         */
        public Builder setWorkingStatusKeys(String idlingKey, String pausedKey, String runningKey) {
            if (idlingKey != null)
                this.idlingKey = idlingKey;
            if (pausedKey != null)
                this.pausedKey = pausedKey;
            if (runningKey != null)
                this.runningKey = runningKey;
            return this;
        }

        /**
         * Adds the max EU/t that this multiblock can use.
         * <br>
         * Added if the structure is formed and if the passed energy container has greater than zero capacity.
         */
        public Builder addEnergyUsageLine(EnergyContainerList energyContainer) {
            if (!isStructureFormed)
                return this;
            if (energyContainer != null && energyContainer.getEnergyCapacity() > 0) {
                long maxVoltage = energyContainer.getTotalEUt();

                String energyFormatted = FormattingUtil.formatNumbers(maxVoltage);
                // wrap in text component to keep it from being formatted
                byte voltageTier = GTUtil.getFloorTierByVoltage(maxVoltage);
                Component voltageName = Component.literal(
                        GTValues.VNF[voltageTier]);

                MutableComponent bodyText = Component.translatable("gtpm.multiblock.max_energy_per_tick",
                        energyFormatted, voltageName).withStyle(ChatFormatting.GRAY);
                Component hoverText = Component.translatable("gtpm.multiblock.max_energy_per_tick_hover")
                        .withStyle(ChatFormatting.GRAY);
                textList.add(bodyText.withStyle(
                        style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
            }
            return this;
        }

        /**
         * Adds the max Recipe Tier that this multiblock can use for recipe lookup.
         * <br>
         * Added if the structure is formed and if the passed tier is a valid energy tier index for
         * {@link GTValues#VNF}.
         */
        public Builder addEnergyTierLine(int tier) {
            if (!isStructureFormed)
                return this;
            if (tier < GTValues.ULV || tier > GTValues.MAX)
                return this;

            Component voltageName = Component.literal(GTValues.VNF[tier]);
            MutableComponent bodyText = Component.translatable(
                    "gtpm.multiblock.max_recipe_tier",
                    voltageName).withStyle(ChatFormatting.GRAY);
            Component hoverText = Component.translatable("gtpm.multiblock.max_recipe_tier_hover")
                    .withStyle(ChatFormatting.GRAY);
            textList.add(bodyText
                    .withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
            return this;
        }

        /**
         * Adds the exact EU/t that this multiblock needs to run.
         * <br>
         * Added if the structure is formed and if the passed value is greater than zero.
         */
        public Builder addEnergyUsageExactLine(long energyUsage) {
            if (!isStructureFormed)
                return this;
            if (energyUsage > 0) {
                String energyFormatted = FormattingUtil.formatNumbers(energyUsage);
                // wrap in text component to keep it from being formatted
                Component voltageName = Component.literal(
                        GTValues.VNF[GTUtil.getTierByVoltage(energyUsage)]);

                textList.add(Component.translatable("gtpm.multiblock.energy_consumption",
                        energyFormatted, voltageName).withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds the max EU/t that this multiblock can produce.
         * <br>
         * Added if the structure is formed and if the max voltage is greater than zero and the recipe EU/t.
         */
        public Builder addEnergyProductionLine(long maxVoltage, long recipeEUt) {
            if (!isStructureFormed)
                return this;
            if (maxVoltage != 0 && maxVoltage >= -recipeEUt) {
                String energyFormatted = FormattingUtil.formatNumbers(maxVoltage);
                // wrap in text component to keep it from being formatted
                Component voltageName = Component.literal(
                        GTValues.VNF[GTUtil.getFloorTierByVoltage(maxVoltage)]);

                textList.add(Component.translatable("gtpm.multiblock.max_energy_per_tick",
                        energyFormatted, voltageName).withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds the max EU/t that this multiblock can produce, including how many amps. Recommended for multi-amp
         * outputting multis.
         * <br>
         * Added if the structure is formed, if the amperage is greater than zero and if the max voltage is greater than
         * zero.
         */
        public Builder addEnergyProductionAmpsLine(long maxVoltage, int amperage) {
            if (!isStructureFormed)
                return this;
            if (maxVoltage != 0 && amperage != 0) {
                String energyFormatted = FormattingUtil.formatNumbers(maxVoltage);
                // wrap in text component to keep it from being formatted
                Component voltageName = Component.literal(
                        GTValues.VNF[GTUtil.getFloorTierByVoltage(maxVoltage)]);

                textList.add(Component.translatable("gtpm.multiblock.max_energy_per_tick_amps",
                        energyFormatted, amperage, voltageName).withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds the max CWU/t that this multiblock can use.
         * <br>
         * Added if the structure is formed and if the max CWU/t is greater than zero.
         */
        public Builder addComputationUsageLine(int maxCWUt) {
            if (!isStructureFormed)
                return this;
            if (maxCWUt > 0) {
                Component computation = Component.literal(FormattingUtil.formatNumbers(maxCWUt))
                        .withStyle(ChatFormatting.AQUA);
                textList.add(Component.translatable("gtpm.multiblock.computation.max",
                        computation).withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds a currently used CWU/t line.
         * <br>
         * Added if the structure is formed, the machine is active, and the current CWU/t is greater than zero.
         */
        public Builder addComputationUsageExactLine(int currentCWUt) {
            if (!isStructureFormed)
                return this;
            if (isActive && currentCWUt > 0) {
                Component computation = Component.literal(FormattingUtil.formatNumbers(currentCWUt) + " CWU/t")
                        .withStyle(ChatFormatting.AQUA);
                textList.add(Component.translatable(
                        "gtpm.multiblock.computation.usage",
                        computation).withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds a three-state indicator line, showing if the machine is running, paused, or idling.
         * <br>
         * Added if the structure is formed.
         */
        public Builder addWorkingStatusLine() {
            if (!isStructureFormed)
                return this;

            if (!isWorkingEnabled) {
                return addWorkPausedLine(false);
            } else if (isActive) {
                return addRunningPerfectlyLine(false);
            } else {
                return addIdlingLine(false);
            }
        }

        /**
         * Adds the "Work Paused." line.
         * <br>
         * Added if working is not enabled, or if the checkState passed parameter is false.
         * Also added only if formed.
         */
        public Builder addWorkPausedLine(boolean checkState) {
            if (!isStructureFormed)
                return this;
            if (!checkState || !isWorkingEnabled) {
                textList.add(Component.translatable(pausedKey).withStyle(ChatFormatting.GOLD));
            }
            return this;
        }

        /**
         * Adds the "Running Perfectly." line.
         * <br>
         * Added if machine is active, or if the checkState passed parameter is false.
         * Also added only if formed.
         */
        public Builder addRunningPerfectlyLine(boolean checkState) {
            if (!isStructureFormed)
                return this;
            if (!checkState || isActive) {
                textList.add(Component.translatable(runningKey).withStyle(ChatFormatting.GREEN));
            }
            return this;
        }

        /**
         * Adds the "Idling." line.
         * <br>
         * Added if the machine is not active and working is enabled, or if the checkState passed parameter is false.
         * Also added only if formed.
         */
        public Builder addIdlingLine(boolean checkState) {
            if (!isStructureFormed)
                return this;
            if (!checkState || (isWorkingEnabled && !isActive)) {
                textList.add(Component.translatable(idlingKey).withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        public Builder addProgressLineOnlyPercent(double progressPercent) {
            if (!isStructureFormed || !isActive)
                return this;
            int currentProgress = (int) (progressPercent * 100);
            textList.add(Component.translatable("gtpm.multiblock.progress_percent", currentProgress));
            return this;
        }

        /**
         * Adds a progress line based on the recipe logic.
         *
         * @param recipeLogic The recipe logic that provides the progress info
         *
         * @see #addProgressLine(double, double, double)
         * @see #addCustomProgressLine(RecipeLogic)
         */
        public Builder addProgressLine(RecipeLogic recipeLogic) {
            if (recipeLogic.hasCustomProgressLine()) {
                return this.addCustomProgressLine(recipeLogic);
            } else {
                return this.addProgressLine(recipeLogic.getProgress(), recipeLogic.getMaxProgress(),
                        recipeLogic.getProgressPercent());
            }
        }

        /**
         * Adds a simple progress line that displays the current time of a recipe and its progress as a percentage.
         * <br>
         * Added if structure is formed and the machine is active.
         *
         * @param currentDuration The current duration of the recipe in ticks
         * @param maxDuration     The max duration of the recipe in ticks
         * @param progressPercent Progress formatted as a range of [0,1] representing the progress of the recipe.
         */
        public Builder addProgressLine(double currentDuration, double maxDuration, double progressPercent) {
            if (!isStructureFormed || !isActive)
                return this;
            int currentProgress = (int) (progressPercent * 100);
            double currentInSec = currentDuration / 20.0;
            double maxInSec = maxDuration / 20.0;
            textList.add(Component.translatable("gtpm.multiblock.progress",
                    String.format("%.2f", (float) currentInSec),
                    String.format("%.2f", (float) maxInSec), currentProgress));
            return this;
        }

        /**
         * Adds a customized progress line that is often used to display the current time of a recipe and its progress
         * as a percentage.
         * <p>
         * Added if structure if formed and the machine is active.
         *
         * @param recipeLogic The recipe logic that provides the line
         */
        public Builder addCustomProgressLine(RecipeLogic recipeLogic) {
            if (!isStructureFormed || !isActive)
                return this;
            Component line = recipeLogic.getCustomProgressLine();
            if (line != null) {
                textList.add(line);
            }
            return this;
        }

        public Builder addRecipeFailReasonLine(RecipeLogic recipeLogic) {
            if (!isStructureFormed || !recipeLogic.isIdle())
                return this;
            var reasons = recipeLogic.getFailureReasons();
            if (!reasons.isEmpty()) {
                textList.add(Component.translatable("gtpm.recipe_logic.setup_fail").withStyle(ChatFormatting.RED));
                for (var reason : reasons) {
                    textList.add(Component.literal(" - ").append(reason));
                }
            }
            return this;
        }

        public Builder addBatchModeLine(boolean batchEnabled, int batchAmount) {
            if (batchEnabled && batchAmount > 0) {
                Component runs = Component.literal(FormattingUtil.formatNumbers(batchAmount))
                        .withStyle(ChatFormatting.DARK_PURPLE);
                String key = "gtpm.multiblock.batch_enabled";
                textList.add(Component.translatable(key, runs)
                        .withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        public Builder addSubtickParallelsLine(int subtickParallels) {
            if (subtickParallels > 1) {
                Component runs = Component.literal(FormattingUtil.formatNumbers(subtickParallels))
                        .withStyle(ChatFormatting.DARK_PURPLE);
                String key = "gtpm.multiblock.subtick_parallels";
                textList.add(Component.translatable(key, runs)
                        .withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        public Builder addTotalRunsLine(int totalRuns) {
            if (totalRuns > 1) {
                Component runs = Component.literal(FormattingUtil.formatNumbers(totalRuns))
                        .withStyle(ChatFormatting.DARK_PURPLE);
                String key = "gtpm.multiblock.total_runs";
                textList.add(Component.translatable(key, runs)
                        .withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        public Builder addOutputLines(GTRecipe recipe) {
            if (!isStructureFormed || !isActive)
                return this;
            if (recipe != null) {
                int recipeTier = RecipeHelper.getPreOCRecipeEuTier(recipe);
                int chanceTier = recipeTier + recipe.ocLevel;
                var function = recipe.getType().getChanceFunction();
                double maxDurationSec = (double) recipe.duration / 20.0;
                var itemOutputs = recipe.getOutputContents(ItemRecipeCapability.CAP);
                var fluidOutputs = recipe.getOutputContents(FluidRecipeCapability.CAP);
                int runs = recipe.getTotalRuns();

                for (var item : itemOutputs) {
                    boolean rounded = false;
                    ItemStack stack;
                    // number of items output by a non-ranged ingredient
                    int count = 0;
                    // number of items output, but stored as a double. Used for accurate items/second display.
                    double countD = 1;
                    // number of items output which is actually displayed. Can be either a number, or a range.
                    Component displaycount;
                    if (item.content instanceof IntProviderIngredient provider) {
                        rounded = true;
                        stack = provider.getMaxSizeStack();
                        displaycount = Component.translatable("gtpm.gui.content.range",
                                provider.getCountProvider().getMinValue(),
                                provider.getCountProvider().getMaxValue());
                        if (item.chance < item.maxChance) {
                            countD = countD * runs * function.getBoostedChance(item, recipeTier, chanceTier) /
                                    item.maxChance;
                        }
                        countD = countD * provider.getMidRoll();
                    } else {
                        var stacks = ItemRecipeCapability.CAP.of(item.content).getItems();
                        if (stacks.length == 0) continue;
                        stack = stacks[0];
                        count = stack.getCount();
                        countD *= count;
                        if (item.chance < item.maxChance) {
                            rounded = true;
                            countD = countD * runs * function.getBoostedChance(item, recipeTier, chanceTier) /
                                    item.maxChance;
                        }
                        count = Math.max(1, (int) Math.round(countD));
                        displaycount = Component.literal(String.valueOf(count));
                    }
                    if (countD < maxDurationSec) {
                        String key = "gtpm.multiblock.output_line." + (rounded ? "2" : "0");
                        textList.add(Component.translatable(key, stack.getHoverName(), displaycount,
                                FormattingUtil.formatNumber2Places(maxDurationSec / countD)));
                    } else {
                        String key = "gtpm.multiblock.output_line." + (rounded ? "3" : "1");
                        textList.add(Component.translatable(key, stack.getHoverName(), displaycount,
                                FormattingUtil.formatNumber2Places(countD / maxDurationSec)));
                    }
                }
                for (var fluid : fluidOutputs) {
                    boolean rounded = false;
                    FluidStack stack;
                    // amount of fluid output by a non-ranged ingredient
                    int amount = 0;
                    // amount of fluid output, but stored as a double. Used for accurate fluid/second display.
                    double amountD = 1;
                    // amount of fluid output which is actually displayed. Can be either a number, or a range.
                    Component displaycount;
                    if (fluid.content instanceof IntProviderFluidIngredient provider) {
                        rounded = true;
                        stack = provider.getMaxSizeStack();
                        displaycount = Component.translatable("gtpm.gui.content.range",
                                provider.getCountProvider().getMinValue(),
                                provider.getCountProvider().getMaxValue());
                        if (fluid.chance < fluid.maxChance) {
                            amountD = amountD * runs * function.getBoostedChance(fluid, recipeTier, chanceTier) /
                                    fluid.maxChance;
                        }
                        amountD = amountD * provider.getMidRoll();
                    } else {
                        var stacks = FluidRecipeCapability.CAP.of(fluid.content).getFluids();
                        if (stacks.length == 0) continue;
                        stack = stacks[0];
                        amount = stack.getAmount();
                        amountD *= amount;
                        if (fluid.chance < fluid.maxChance) {
                            rounded = true;
                            amountD = amountD * runs * function.getBoostedChance(fluid, recipeTier, chanceTier) /
                                    fluid.maxChance;
                        }
                        amount = Math.max(1, (int) Math.round(amountD));
                        displaycount = Component.literal(String.valueOf(amount));
                    }
                    if (amountD < maxDurationSec) {
                        String key = "gtpm.multiblock.output_line." + (rounded ? "2" : "0");
                        textList.add(Component.translatable(key, stack.getHoverName(), displaycount,
                                FormattingUtil.formatNumber2Places(maxDurationSec / amountD)));
                    } else {
                        String key = "gtpm.multiblock.output_line." + (rounded ? "3" : "1");
                        textList.add(Component.translatable(key, stack.getHoverName(), displaycount,
                                FormattingUtil.formatNumber2Places(amountD / maxDurationSec)));
                    }
                }
            }
            return this;
        }

        /**
         * Adds a line indicating the current mode of the multi
         */
        public Builder addMachineModeLine(GTRecipeType recipeType, boolean hasMultipleModes) {
            if (!isStructureFormed || !hasMultipleModes)
                return this;
            textList.add(Component
                    .translatable("gtpm.gui.machinemode", recipeType.getName())
                    .withStyle(ChatFormatting.AQUA));
            return this;
        }

        public Builder addParallelsLine(int numParallels) {
            return addParallelsLine(numParallels, false);
        }

        /**
         * Adds a line indicating how many parallels this multi can potentially perform.
         * <br>
         * Added if structure is formed and the number of parallels is greater than one.
         */
        public Builder addParallelsLine(int numParallels, boolean exact) {
            if (!isStructureFormed)
                return this;
            if (numParallels > 1) {
                Component parallels = Component.literal(FormattingUtil.formatNumbers(numParallels))
                        .withStyle(ChatFormatting.DARK_PURPLE);
                String key = "gtpm.multiblock.parallel";
                if (exact) key += ".exact";
                textList.add(Component.translatable(key, parallels)
                        .withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds a warning line when the machine is low on power.
         * <br>
         * Added if the structure is formed and if the passed parameter is true.
         */
        public Builder addLowPowerLine(boolean isLowPower) {
            if (!isStructureFormed)
                return this;
            if (isLowPower) {
                textList.add(
                        Component.translatable("gtpm.multiblock.not_enough_energy").withStyle(ChatFormatting.YELLOW));
            }
            return this;
        }

        /**
         * Adds a warning line when the machine is low on computation.
         * <br>
         * Added if the structure is formed and if the passed parameter is true.
         */
        public Builder addLowComputationLine(boolean isLowComputation) {
            if (!isStructureFormed)
                return this;
            if (isLowComputation) {
                textList.add(Component.translatable("gtpm.multiblock.computation.not_enough_computation")
                        .withStyle(ChatFormatting.YELLOW));
            }
            return this;
        }

        /**
         * Adds a warning line when the machine's dynamo tier is too low for current conditions.
         * <br>
         * Added if the structure is formed and if the passed parameter is true.
         */
        public Builder addLowDynamoTierLine(boolean isTooLow) {
            if (!isStructureFormed)
                return this;
            if (isTooLow) {
                textList.add(Component.translatable("gtpm.multiblock.not_enough_energy_output")
                        .withStyle(ChatFormatting.YELLOW));
            }
            return this;
        }

        /**
         * Adds warning line(s) when the machine has maintenance problems.
         * <br>
         * Added if there are any maintenance problems, one line per problem as well as a header. <br>
         * Will check the config setting for if maintenance is enabled automatically.
         */
        public Builder addMaintenanceProblemLines(byte maintenanceProblems) {
            if (!isStructureFormed || !GTPMConfigHolder.INSTANCE.machines.enableMaintenance)
                return this;
            if (maintenanceProblems >= 0 && maintenanceProblems < 0b111111) {
                addMaintenanceProblemHeader();

                // Wrench
                if ((maintenanceProblems & 1) == 0) {
                    textList.add(Component.translatable("gtpm.multiblock.universal.problem.wrench")
                            .withStyle(ChatFormatting.GRAY));
                }

                // Screwdriver
                if (((maintenanceProblems >> 1) & 1) == 0) {
                    textList.add(Component.translatable("gtpm.multiblock.universal.problem.screwdriver")
                            .withStyle(ChatFormatting.GRAY));
                }

                // Soft Mallet
                if (((maintenanceProblems >> 2) & 1) == 0) {
                    textList.add(Component.translatable("gtpm.multiblock.universal.problem.soft_mallet")
                            .withStyle(ChatFormatting.GRAY));
                }

                // Hammer
                if (((maintenanceProblems >> 3) & 1) == 0) {
                    textList.add(Component.translatable("gtpm.multiblock.universal.problem.hard_hammer")
                            .withStyle(ChatFormatting.GRAY));
                }

                // Wire Cutters
                if (((maintenanceProblems >> 4) & 1) == 0) {
                    textList.add(Component.translatable("gtpm.multiblock.universal.problem.wire_cutter")
                            .withStyle(ChatFormatting.GRAY));
                }

                // Crowbar
                if (((maintenanceProblems >> 5) & 1) == 0) {
                    textList.add(Component.translatable("gtpm.multiblock.universal.problem.crowbar")
                            .withStyle(ChatFormatting.GRAY));
                }
            }
            return this;
        }

        private void addMaintenanceProblemHeader() {
            textList.add(
                    Component.translatable("gtpm.multiblock.universal.has_problems").withStyle(ChatFormatting.YELLOW));
        }

        /**
         * Adds two error lines when the machine's muffler hatch is obstructed.
         * <br>
         * Added if the structure is formed and if the passed parameter is true.
         */
        public Builder addMufflerObstructedLine(boolean isObstructed) {
            if (!isStructureFormed)
                return this;
            if (isObstructed) {
                textList.add(Component.translatable("gtpm.multiblock.universal.muffler_obstructed")
                        .withStyle(ChatFormatting.RED));
                textList.add(Component.translatable("gtpm.multiblock.universal.muffler_obstructed.tooltip")
                        .withStyle(ChatFormatting.GRAY));
            }
            return this;
        }

        /**
         * Adds a fuel consumption line showing the fuel name and the number of ticks per recipe run.
         * <br>
         * Added if structure is formed, the machine is active, and the passed fuelName parameter is not null.
         */
        public Builder addFuelNeededLine(String fuelName, int previousRecipeDuration) {
            if (!isStructureFormed || !isActive || fuelName == null)
                return this;
            Component fuelNeeded = Component.literal(fuelName).withStyle(ChatFormatting.RED);
            Component numTicks = Component.literal(FormattingUtil.formatNumbers(previousRecipeDuration))
                    .withStyle(ChatFormatting.AQUA);
            textList.add(Component.translatable(
                    "gtpm.multiblock.turbine.fuel_needed",
                    fuelNeeded, numTicks).withStyle(ChatFormatting.GRAY));
            return this;
        }

        /**
         * Insert an empty line into the text list.
         */
        public Builder addEmptyLine() {
            textList.add(EMPTY_COMPONENT);
            return this;
        }

        /**
         * Add custom text dynamically, allowing for custom application logic.
         */
        public Builder addCustom(Consumer<List<Component>> customConsumer) {
            customConsumer.accept(textList);
            return this;
        }

        /*
         * Add a line specifying the current EU/t
         */
        public Builder addCurrentEnergyProductionLine(long euOutput) {
            textList.add(Component.translatable("gtpm.multiblock.turbine.energy_per_tick_maxed",
                    FormattingUtil.formatNumbers(euOutput)).withStyle(ChatFormatting.GRAY));
            return this;
        }
    }
}

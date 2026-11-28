package com.modularmc.gtpm.utils;

import com.modularmc.gtpm.api.capability.recipe.FluidRecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.capability.recipe.IRecipeCapabilityHolder;
import com.modularmc.gtpm.api.capability.recipe.ItemRecipeCapability;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.GTRecipeDefinition;
import com.modularmc.gtpm.api.recipe.GTRecipeSerializer;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.sync_system.FieldCodecs;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.data.GTItems;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.item.datacomponents.DataItem;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.data.recipe.builder.GTRecipeBuilder;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

@TransVariants(keyPrefix = "gtpm.scanner",
               value = {
                       @Variant(id = "copy_stick_empty",
                                source = "§oEmpty Stick",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§o空闪存")),
                       @Variant(id = "copy_stick_from",
                                source = "§oStick to Copy",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§o要复制的闪存")),
                       @Variant(id = "copy_stick_to",
                                source = "§oCopy of Stick",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§o闪存（已复制）")),
               })
@Trans(key = "behavior.data_item.title",
       source = "§n%s Construction Data:",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§n%s结构数据："))
public final class ResearchManager {

    public static ItemStack getDefaultScannerItem() {
        return GTItems.TOOL_DATA_STICK.asStack();
    }

    public static ItemStack getDefaultResearchStationItem(int cwut) {
        if (cwut > 32) {
            return GTItems.TOOL_DATA_MODULE.asStack();
        }
        return GTItems.TOOL_DATA_ORB.asStack();
    }

    private ResearchManager() {}

    /**
     * @param stack      the stack to check
     * @param isDataBank if the caller is a Data Bank. Pass "true" here if your use-case does not matter for this check.
     * @return if the stack is a data item
     */
    public static boolean isStackDataItem(ItemStack stack, boolean isDataBank) {
        @Nullable
        DataItem dataItem = stack.get(GTDataComponents.DATA_ITEM);
        return dataItem != null && (!dataItem.requireDataBank() || isDataBank);
    }

    public static void addResearchOutputNames(Collection<GTRecipeDefinition> recipes, Consumer<Component> addLine) {
        var names = new ObjectOpenHashSet<Component>();
        for (GTRecipeDefinition recipe : recipes) {
            Component name = getResearchOutputName(recipe);
            if (name != null && names.add(name)) {
                addLine.accept(Component.translatable("behavior.data_item.data", name));
            }
        }
    }

    private static @Nullable Component getResearchOutputName(GTRecipeDefinition recipe) {
        for (var content : recipe.getOutputContents(ItemRecipeCapability.CAP)) {
            for (ItemStack stack : ItemRecipeCapability.CAP.of(content.content).getItems()) {
                if (!stack.isEmpty()) return stack.getHoverName();
            }
        }
        for (var content : recipe.getOutputContents(FluidRecipeCapability.CAP)) {
            for (FluidStack stack : FluidRecipeCapability.CAP.of(content.content).getFluids()) {
                if (!stack.isEmpty()) return stack.getHoverName();
            }
        }
        return null;
    }

    /**
     * Create the default research recipe
     *
     * @param builder the builder to retrieve recipe info from
     */
    public static void createDefaultResearchRecipe(GTRecipeBuilder builder, RecipeOutput provider) {
        if (!GTPMConfigHolder.INSTANCE.machines.enableResearch) return;

        for (GTRecipeBuilder.ResearchRecipeEntry entry : builder.researchRecipeEntries()) {
            if (entry.researchItem().isEmpty() && entry.researchFluid().isEmpty())
                throw new IllegalStateException("Both entry types in the research entry are null!");

            createDefaultResearchRecipe(builder.recipeType, entry.researchId(),
                    entry.researchItem(), entry.researchFluid(),
                    entry.dataStack(), entry.duration(), entry.tier(), entry.EUt(), entry.CWUt(), provider);
        }
    }

    public static void createDefaultResearchRecipe(GTRecipeType recipeType, String researchId,
                                                   ItemStack researchItem, FluidStack researchFluid,
                                                   ItemStack dataItem,
                                                   int duration, int tier, long eut, int CWUt,
                                                   RecipeOutput provider) {
        if (!GTPMConfigHolder.INSTANCE.machines.enableResearch) return;

        dataItem.set(GTDataComponents.RESEARCH_ITEM, new ResearchItem(researchId, recipeType));

        if (CWUt > 0) {
            var builder = GTRecipeTypes.RESEARCH_STATION_RECIPES
                    .recipeBuilder(researchId)
                    .inputItems(dataItem.getItem());

            if (!researchItem.isEmpty()) builder.inputItems(researchItem);
            if (!researchFluid.isEmpty()) builder.inputFluids(researchFluid);

            builder.outputItems(dataItem)
                    .EUt(eut)
                    .tier(tier)
                    .CWUt(CWUt)
                    .totalCWU(duration)
                    .save(provider);
        } else {
            var builder = GTRecipeTypes.SCANNER_RECIPES.recipeBuilder(FormattingUtil.toLowerCaseUnderscore(researchId))
                    .inputItems(dataItem.getItem());

            if (!researchItem.isEmpty()) builder.inputItems(researchItem);
            if (!researchFluid.isEmpty()) builder.inputFluids(researchFluid);

            builder.outputItems(dataItem)
                    .duration(duration)
                    .EUt(eut)
                    .tier(tier)
                    .researchScan(true)
                    .save(provider);
        }
    }

    public record ResearchItem(String researchId, GTRecipeType recipeType) implements TooltipProvider {

        // spotless:off
        public static final Codec<ResearchItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("research_id").forGetter(ResearchItem::researchId),
                GTRecipeSerializer.GT_RECIPE_TYPE_CODEC.fieldOf("research_type").forGetter(ResearchItem::recipeType)
        ).apply(instance, ResearchItem::new));
        // spotless:on
        public static final StreamCodec<ByteBuf, ResearchItem> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ResearchItem::researchId,
                GTRecipeSerializer.GT_RECIPE_TYPE_STREAM_CODEC, ResearchItem::recipeType,
                ResearchItem::new);

        static {
            FieldCodecs.register(ResearchItem.class, CODEC);
            FieldCodecs.registerStream(ResearchItem.class, STREAM_CODEC);
        }

        @Override
        public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder,
                                 TooltipFlag tooltipFlag) {
            Collection<GTRecipeDefinition> recipes = recipeType().getClientDataStickEntry(researchId());
            if (recipes == null || recipes.isEmpty()) {
                return;
            }
            tooltipAdder.accept(Component.translatable("behavior.data_item.title", recipeType().getName()));

            addResearchOutputNames(recipes, tooltipAdder);
        }
    }

    public static class DataStickCopyScannerLogic implements GTRecipeType.ICustomRecipeLogic {

        private static final int EUT = 2;
        private static final int DURATION = 100;

        @Override
        public @Nullable GTRecipe createCustomRecipe(IRecipeCapabilityHolder holder) {
            var itemInputs = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP).stream()
                    .filter(IItemHandlerModifiable.class::isInstance)
                    .map(IItemHandlerModifiable.class::cast)
                    .toArray(IItemHandlerModifiable[]::new);
            var inputs = new CombinedInvWrapper(itemInputs);
            if (inputs.getSlots() > 1) {
                // try the data recipe both ways, prioritizing overwriting the first
                GTRecipe recipe = createDataRecipe(inputs.getStackInSlot(0), inputs.getStackInSlot(1));
                if (recipe != null) return recipe;

                return createDataRecipe(inputs.getStackInSlot(1), inputs.getStackInSlot(0));
            }
            return null;
        }

        private @Nullable GTRecipe createDataRecipe(ItemStack first, ItemStack second) {
            ResearchItem researchItem = second.get(GTDataComponents.RESEARCH_ITEM);
            if (researchItem == null) return null;

            // Both must be data items
            if (!isStackDataItem(first, true)) return null;
            if (!isStackDataItem(second, true)) return null;

            ItemStack output = first.copy();
            output.set(GTDataComponents.RESEARCH_ITEM, researchItem);
            return GTRecipeTypes.SCANNER_RECIPES.recipeBuilder(GTStringUtils.itemStackToString(output))
                    .inputItems(first)
                    .notConsumable(second)
                    .outputItems(output)
                    .duration(DURATION).EUt(EUT)
                    .build();
        }

        @Override
        public void buildRepresentativeRecipes() {
            ItemStack copiedStick = GTItems.TOOL_DATA_STICK.asStack();
            copiedStick.set(DataComponents.CUSTOM_NAME, Component.translatable("gtpm.scanner.copy_stick_from"));
            ItemStack emptyStick = GTItems.TOOL_DATA_STICK.asStack();
            emptyStick.set(DataComponents.CUSTOM_NAME, Component.translatable("gtpm.scanner.copy_stick_empty"));
            ItemStack resultStick = GTItems.TOOL_DATA_STICK.asStack();
            resultStick.set(DataComponents.CUSTOM_NAME, Component.translatable("gtpm.scanner.copy_stick_to"));

            GTRecipeDefinition recipe = GTRecipeTypes.SCANNER_RECIPES
                    .recipeBuilder("copy_" + GTStringUtils.itemStackToString(copiedStick))
                    .inputItems(emptyStick)
                    .notConsumable(copiedStick)
                    .outputItems(resultStick)
                    .duration(DURATION).EUt(EUT)
                    .buildDefinition();
            // for EMI to detect it's a synthetic recipe (not ever in JSON)
            recipe.setId(recipe.getId().withPrefix("/"));
            GTRecipeTypes.SCANNER_RECIPES.addToMainCategory(recipe);
        }
    }

    @Trans(key = "behavior.data_item.data",
           source = "- §a%s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "- §a%s"))
    private static final class BehaviorTranslations {}
}

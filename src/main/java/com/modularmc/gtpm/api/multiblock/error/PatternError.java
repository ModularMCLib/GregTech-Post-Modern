package com.modularmc.gtpm.api.multiblock.error;

import com.modularmc.gtpm.api.multiblock.MultiblockState;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PatternPredicate;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PredicateRule;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Trans(key = "gtpm.multiblock.pattern.error",
       source = "Expected components (%s) at (%s).",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "在%2$s处需要%1$s。"))
@TransVariants(keyPrefix = "gtpm.multiblock.pattern",
               value = {
                       @Variant(id = "clear_amount_1",
                                source = "§6Must have a clear 1x1x1 space in front§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§6前方必须有1x1x1大小的空间§r")),
                       @Variant(id = "clear_amount_3",
                                source = "§6Must have a clear 3x3x1 space in front§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§6前方必须有3x3x1大小的空间§r")),
                       @Variant(id = "error.ambiguous_choice",
                                source = "§cMultiple structure variants match simultaneously§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c多个结构变体同时匹配§r")),
                       @Variant(id = "error.batteries",
                                source = "§cAll batteries must be the same§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c必须使用同种电池§r")),
                       @Variant(id = "error.coils",
                                source = "§cAll heating coils must be the same§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c必须使用同种线圈方块§r")),
                       @Variant(id = "error.filters",
                                source = "§cAll filters must be the same§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c必须使用同种过滤器§r")),
                       @Variant(id = "error.limited.0",
                                source = "§cMaximum: %d§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最多：%d个§r")),
                       @Variant(id = "error.limited.1",
                                source = "§cMinimum: %d§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最少：%d个§r")),
                       @Variant(id = "error.limited.2",
                                source = "§cMaximum: %d per layer§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最多：每层%d个§r")),
                       @Variant(id = "error.limited.3",
                                source = "§cMinimum: %d per layer§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最少：每层%d个§r")),
                       @Variant(id = "error.limited_exact",
                                source = "§cExactly: %d§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c精确：%d§r")),
                       @Variant(id = "error.limited_within",
                                source = "§cBetween %d and %d§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最少%d个，最多%d个§r")),
                       @Variant(id = "location_end",
                                source = "§cVery End§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最末端§r")),
                       @Variant(id = "replaceable_air",
                                source = "Replaceable by Air",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可为空气")),
                       @Variant(id = "single",
                                source = "§6Only this block can be used§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6仅可使用该种方块§r")),
               })
public class PatternError {

    @Setter
    protected MultiblockState worldState;

    public Level getWorld() {
        return worldState.getWorld();
    }

    public BlockPos getPos() {
        return worldState.getPos();
    }

    public List<List<ItemStack>> getCandidates() {
        PatternPredicate predicate = worldState.predicate;
        List<List<ItemStack>> candidates = new ArrayList<>();
        for (PredicateRule common : predicate.common) {
            candidates.add(common.getCandidates());
        }
        for (PredicateRule limited : predicate.limited) {
            candidates.add(limited.getCandidates());
        }
        return candidates;
    }

    public Component getErrorInfo() {
        List<List<ItemStack>> candidates = getCandidates();
        StringBuilder builder = new StringBuilder();
        for (List<ItemStack> candidate : candidates) {
            if (!candidate.isEmpty()) {
                builder.append(candidate.get(0).getDisplayName());
                builder.append(", ");
            }
        }
        builder.append("...");
        return Component.translatable("gtpm.multiblock.pattern.error", builder.toString(),
                worldState.getPos().toShortString());
    }
}

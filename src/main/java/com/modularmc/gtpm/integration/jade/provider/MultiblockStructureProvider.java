package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.multiblock.MultiblockState;
import com.modularmc.gtpm.api.multiblock.error.StructureIssue;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

/** Reads confirmed diagnostics only; looking at a controller never starts a structure scan. */
@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "structure.issue.at_least",
                                source = "At least %s, found %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "至少 %s 个，当前 %s 个")),
                       @Variant(id = "structure.issue.at_most",
                                source = "At most %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "至多 %s 个")),
                       @Variant(id = "structure.issue.coils",
                                source = "Heating coils",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "线圈")),
                       @Variant(id = "structure.issue.expected",
                                source = "Expected: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要：%s")),
                       @Variant(id = "structure.issue.filters",
                                source = "Filters",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过滤器")),
                       @Variant(id = "structure.issue.forbidden",
                                source = "Forbidden block inside",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存在不允许的方块")),
                       @Variant(id = "structure.issue.found",
                                source = "Found: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "实际：%s")),
                       @Variant(id = "structure.issue.matching",
                                source = "matching blocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "匹配方块")),
                       @Variant(id = "structure.issue.mismatch",
                                source = "Wrong block",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "方块不符")),
                       @Variant(id = "structure.issue.mixed",
                                source = "Mixed %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s不一致")),
                       @Variant(id = "structure.issue.more",
                                source = "%s (%s kinds)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s 等 %s 种")),
                       @Variant(id = "structure.issue.orientation",
                                source = "Block facing does not match the structure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "方块朝向不符合结构要求")),
                       @Variant(id = "structure.issue.position",
                                source = "(%s, %s, %s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "(%s, %s, %s)")),
                       @Variant(id = "structure.issue.shared",
                                source = "Part used by another multiblock",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "部件已被其他多方块占用")),
                       @Variant(id = "structure.issue.size",
                                source = "Invalid size",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "尺寸不符")),
                       @Variant(id = "structure.issue.size.combination",
                                source = "Size combination not allowed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "尺寸组合不满足要求")),
                       @Variant(id = "structure.issue.size.range",
                                source = "%s: %s~%s allowed, measured %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s：允许 %s~%s，实测 %s")),
                       @Variant(id = "structure.issue.size.unmeasured",
                                source = "%s: no boundary within %s blocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s：%s 格内未找到边界")),
                       @Variant(id = "structure.issue.too_few",
                                source = "Not enough %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s数量不足")),
                       @Variant(id = "structure.issue.too_few_layer",
                                source = "Not enough %s in a layer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s单层数量不足")),
                       @Variant(id = "structure.issue.too_many",
                                source = "Too many %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s数量过多")),
                       @Variant(id = "structure.issue.too_many_layer",
                                source = "Too many %s in a layer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s单层数量过多")),
                       @Variant(id = "structure.issue.unloaded",
                                source = "Chunk not loaded",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "区块未加载")),
                       @Variant(id = "structure.issue.variants",
                                source = "%s (%s variants)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s 等 %s 种")),
                       @Variant(id = "structure.issue.condition",
                                source = "Requirement not met",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "条件不满足")),
                       @Variant(id = "structure.issue.unchecked",
                                source = "Waiting for structure check",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "等待结构检查")),
                       @Variant(id = "top.invalid_structure",
                                source = "Structure Incomplete",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "结构不完整")),
                       @Variant(id = "top.valid_structure",
                                source = "Structure Formed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "结构已成型")),
               })
public class MultiblockStructureProvider extends MachineInfoProvider<MultiblockControllerMachine, CompoundTag> {

    public MultiblockStructureProvider() {
        super(GTPM.id("multiblock_structure"), MultiblockControllerMachine.class);
    }

    @Override
    protected CompoundTag write(MultiblockControllerMachine machine) {
        CompoundTag data = new CompoundTag();
        ListTag structures = new ListTag();
        for (String name : machine.getDefinition().getStructureOrder()) {
            structures.add(StringTag.valueOf(name));
            String key = name + "/";
            boolean formed = machine.isStructureFormed(name);
            data.putBoolean(key + "formed", formed);
            if (formed) continue;
            ListTag issue = new ListTag();
            for (Component line : issueLines(machine.getMultiblockState(name)))
                issue.add(StringTag.valueOf(Component.Serializer.toJson(line, machine.getLevel().registryAccess())));
            data.put(key + "issue", issue);
        }
        data.put("structures", structures);
        return data;
    }

    private static ObjectList<Component> issueLines(MultiblockState state) {
        if (state.error == MultiblockState.UNINIT_ERROR)
            return ObjectLists.singleton(Component.translatable("gtpm.structure.issue.unchecked"));
        var result = state.getMatchResult();
        if (result != null && !result.getDiagnostics().isEmpty()) {
            var issue = result.getDiagnostics().getFirst().getIssue();
            if (issue != null) return issue.getLines();
        }
        if (state.error instanceof StructureIssue issue) return issue.getLines();
        Component message = state.error == null ? Component.translatable("gtpm.structure.issue.condition") :
                state.error.getErrorInfo();
        return ObjectLists.singleton(message);
    }

    @Override
    protected void addTooltip(CompoundTag data, ITooltip tooltip, Player player, BlockAccessor accessor,
                              BlockEntity blockEntity, IPluginConfig config) {
        ListTag structures = data.getList("structures", Tag.TAG_STRING);
        for (int index = 0; index < structures.size(); index++) {
            String name = structures.getString(index);
            String key = name + "/";
            boolean formed = data.getBoolean(key + "formed");
            Component status = Component
                    .translatable(formed ? "gtpm.top.valid_structure" : "gtpm.top.invalid_structure")
                    .withStyle(formed ? ChatFormatting.GREEN : ChatFormatting.RED);
            tooltip.add(structures.size() == 1 ? status : Component.literal(name + ": ").append(status));
            if (formed) continue;
            for (Tag serialized : data.getList(key + "issue", Tag.TAG_STRING)) {
                Component line = Component.Serializer.fromJson(serialized.getAsString(),
                        accessor.getLevel().registryAccess());
                if (line != null) tooltip.add(Component.empty().withStyle(ChatFormatting.GRAY).append(line));
            }
        }
    }
}

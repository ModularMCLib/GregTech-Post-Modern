package com.modularmc.gtpm.api.capability;

import com.modularmc.gtpm.api.machine.feature.IRecipeLogicMachine;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "machine.miner.startx",
                                source = "sX: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "sX：%d")),
                       @Variant(id = "machine.miner.starty",
                                source = "sY: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "sY：%d")),
                       @Variant(id = "machine.miner.startz",
                                source = "sZ: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "sZ：%d")),
                       @Variant(id = "machine.miner.minex",
                                source = "mX: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "mX：%d")),
                       @Variant(id = "machine.miner.miney",
                                source = "mY: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "mY：%d")),
                       @Variant(id = "machine.miner.minez",
                                source = "mZ: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "mZ：%d")),
                       @Variant(id = "machine.miner.radius",
                                source = "Radius: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "半径：%d")),
                       @Variant(id = "machine.miner.chunkradius",
                                source = "Chunk Radius: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "区块半径：%d")),
                       @Variant(id = "multiblock.large_miner.done",
                                source = "Done!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "完成！")),
                       @Variant(id = "multiblock.large_miner.working",
                                source = "Working...",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "工作中……")),
                       @Variant(id = "multiblock.large_miner.invfull",
                                source = "Inventory Full!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存储空间已满！")),
                       @Variant(id = "multiblock.large_miner.radius",
                                source = "Radius: §a%d§r Blocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "半径：§a%d§r格")),
                       @Variant(id = "multiblock.large_miner.errorradius",
                                source = "§cCannot change radius while working!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c工作中不能调整半径！")),
                       @Variant(id = "multiblock.large_miner.needsfluid",
                                source = "Needs Drilling Fluid",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要钻井液")),
               })
public interface IMiner extends IRecipeLogicMachine {

    boolean drainInput(boolean simulate);

    static int getWorkingArea(int maximumRadius) {
        return maximumRadius * 2 + 1;
    }
}

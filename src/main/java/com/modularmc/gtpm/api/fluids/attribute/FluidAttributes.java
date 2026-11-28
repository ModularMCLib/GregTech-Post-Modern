package com.modularmc.gtpm.api.fluids.attribute;

import com.modularmc.gtpm.GTPM;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.fluid.type_acid.tooltip",
       source = "§6Acidic! Handle with care!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6酸性流体！轻拿轻放！"))
public final class FluidAttributes {

    /**
     * Attribute for acidic fluids.
     */
    public static final FluidAttribute ACID = new FluidAttribute(GTPM.id("acid"),
            list -> list.accept(Component.translatable("gtpm.fluid.type_acid.tooltip")),
            list -> list.accept(Component.translatable("gtpm.fluid_pipe.acid_proof")));

    private FluidAttributes() {}
}

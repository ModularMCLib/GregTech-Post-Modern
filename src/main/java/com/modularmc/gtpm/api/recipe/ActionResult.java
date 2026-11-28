package com.modularmc.gtpm.api.recipe;

import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.capability.recipe.RecipeCapability;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

/**
 * @param isSuccess is action success
 * @param reason    if fail, fail reason
 */
@Trans(key = "gtpm.recipe_logic.no_contents",
       source = "Recipe has no Contents",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "配方无内容"))
@TransVariants(keyPrefix = "gtpm.recipe_logic",
               value = {
                       @Variant(id = "no_capabilities",
                                source = "Machine has no Capabilities",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器无功能")),
               })
public record ActionResult(boolean isSuccess, @Nullable Component reason, @Nullable RecipeCapability<?> capability,
                           @Nullable IO io) {

    public final static ActionResult SUCCESS = new ActionResult(true, null, null, null);
    public final static ActionResult FAIL_NO_REASON = new ActionResult(false, null, null, null);
    public final static ActionResult PASS_NO_CONTENTS = new ActionResult(true,
            Component.translatable("gtpm.recipe_logic.no_contents"), null, null);
    public final static ActionResult FAIL_NO_CAPABILITIES = new ActionResult(false,
            Component.translatable("gtpm.recipe_logic.no_capabilities"), null, null);

    public static ActionResult fail(@Nullable Component component, @Nullable RecipeCapability<?> capability, IO io) {
        return new ActionResult(false, component, capability, io);
    }

    public Component reason() {
        if (reason == null) return Component.empty();
        return reason;
    }
}

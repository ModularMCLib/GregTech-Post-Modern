package com.modularmc.gtpm.common.data;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.enchantment.providers.SingleEnchantment;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "enchantment",
               value = {
                       @Variant(id = "damage.disjunction",
                                source = "Disjunction",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "末影离析")),
                       @Variant(id = "disjunction",
                                source = "Disjunction",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "末影离析")),
                       @Variant(id = "gtpm.disjunction.description",
                                source = "Applies Weakness and Slowness to Ender-related mobs.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "对末影生物施加虚弱和缓慢效果。")),
                       @Variant(id = "gtpm.hard_hammer.description",
                                source = "Breaks blocks as if they were mined with a GregTech Hammer.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "像格雷科技的锤一样粉碎所采掘的方块。")),
                       @Variant(id = "hard_hammer",
                                source = "Hammering",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锤碎")),
               })
public class GTEnchantmentProviders {

    // spotless:off
    public static final ResourceKey<EnchantmentProvider> SILK_TOUCH = REGISTRATE
            .datapackKey(Registries.ENCHANTMENT_PROVIDER, "silk_touch").register();

    public static void bootstrap(BootstrapContext<EnchantmentProvider> context) {
        HolderGetter<Enchantment> holdergetter = context.lookup(Registries.ENCHANTMENT);
        context.register(SILK_TOUCH, new SingleEnchantment(holdergetter.getOrThrow(Enchantments.SILK_TOUCH), ConstantInt.of(1)));
    }
    // spotless:on
}

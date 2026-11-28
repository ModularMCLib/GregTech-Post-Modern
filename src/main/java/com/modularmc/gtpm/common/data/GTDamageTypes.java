package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.data.medicalcondition.MedicalCondition;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "death.attack.gtpm",
               value = {
                       @Variant(id = "axe",
                                source = "%s has been chopped by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用斧子砍死了")),
                       @Variant(id = "butchery_knife",
                                source = "%s was butchered by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s死在了%s的屠刀之下")),
                       @Variant(id = "chainsaw_lv",
                                source = "%s was massacred by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%2$s的链锯切碎了%1$s")),
                       @Variant(id = "crowbar",
                                source = "%s lost half a life to %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用撬棍撬走了半条命")),
                       @Variant(id = "drill_ev",
                                source = "%s was drilled with 2048V by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%2$s的超高压电钻洞穿了%1$s")),
                       @Variant(id = "drill_hv",
                                source = "%s was drilled with 512V by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%2$s的高压电钻洞穿了%1$s")),
                       @Variant(id = "drill_iv",
                                source = "%s was drilled with 8192V by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%2$s的强导压电钻洞穿了%1$s")),
                       @Variant(id = "drill_lv",
                                source = "%s was drilled with 32V by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%2$s的低压电钻洞穿了%1$s")),
                       @Variant(id = "drill_mv",
                                source = "%s was drilled with 128V by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%2$s的中压电钻洞穿了%1$s")),
                       @Variant(id = "explosion",
                                source = "%s exploded",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s爆炸了")),
                       @Variant(id = "explosion.player",
                                source = "%s exploded with help of %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "拜%2$s所赐，%1$s爆炸了")),
                       @Variant(id = "file",
                                source = "%s has been filed D for 'Dead' by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s历尽了%s的“锉”折")),
                       @Variant(id = "frost",
                                source = "%s explored cryogenics",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s探索到了低温的奥秘")),
                       @Variant(id = "hammer",
                                source = "%s was squashed by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s锤死了")),
                       @Variant(id = "hoe",
                                source = "%s had their head tilled by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s锄死了")),
                       @Variant(id = "knife",
                                source = "%s was gently poked by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%2$s绅士地捅了%1$s几刀")),
                       @Variant(id = "lv_buzzsaw",
                                source = "%s got buzzed by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%2$s手中圆锯的嗞嗞声成为了%1$s生命中最后听到的声音")),
                       @Variant(id = "mallet",
                                source = "%s got hammered to death by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用锤头活活敲死了")),
                       @Variant(id = "mining_hammer",
                                source = "%s was mistaken for Ore by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s当成矿石砸死了")),
                       @Variant(id = "mortar",
                                source = "%s was ground to dust by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用研钵磨成了粉")),
                       @Variant(id = "pickaxe",
                                source = "%s got mined by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用镐子凿死了")),
                       @Variant(id = "screwdriver",
                                source = "%s has screwed with %s for the last time!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用螺丝刀钻死了")),
                       @Variant(id = "screwdriver_lv",
                                source = "%s had their screws removed by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s的螺丝被%s扭掉了")),
                       @Variant(id = "scythe",
                                source = "%s had their soul taken by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s一镰刀下去，%s的灵魂灰飞烟灭")),
                       @Variant(id = "shovel",
                                source = "%s got dug up by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用锹掏死了")),
                       @Variant(id = "spade",
                                source = "%s got excavated by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用铲子挖死了")),
                       @Variant(id = "wire_cutter",
                                source = "%s has cut the cable for the Life Support Machine of %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s用剪线钳剪断了%s的血管")),
                       @Variant(id = "wrench",
                                source = "%s gave %s a whack with the Wrench!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s被%s用扳手夯死了！")),
                       @Variant(id = "wrench_hv",
                                source = "%s's pipes were loosened by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s的血管被%s狠狠拧松了")),
                       @Variant(id = "wrench_iv",
                                source = "%s had a Monkey Wrench thrown into their plans by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%2$s一扳手下去，扳倒了%1$s的美好人生")),
                       @Variant(id = "wrench_lv",
                                source = "%s's pipes were loosened by %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s的血管被%s狠狠拧松了")),
               })
public class GTDamageTypes {

    public static final ResourceKey<DamageType> HEAT = REGISTRATE.damageType("heat")
            .localization(text -> text.source("%s was boiled alive").translation(MinecraftLocale.ZH_CN, "%s被活活煮熟了"))
            .localizationVariant("player",
                    text -> text.source("%s was boiled alive by %s").translation(MinecraftLocale.ZH_CN, "%s被%s活活煮熟了"))
            .register();
    public static final ResourceKey<DamageType> CHEMICAL = REGISTRATE.damageType("chemical")
            .localization(text -> text.source("%s had a chemical accident").translation(MinecraftLocale.ZH_CN, "%s历经了一场化学事故"))
            .register();
    public static final ResourceKey<DamageType> ELECTRIC = REGISTRATE.damageType("electric")
            .localization(text -> text.source("%s was electrocuted").translation(MinecraftLocale.ZH_CN, "%s触电身亡"))
            .register();
    public static final ResourceKey<DamageType> RADIATION = REGISTRATE.damageType("radiation")
            .localization(text -> text.source("%s glows with joy now").translation(MinecraftLocale.ZH_CN, "%s放射出了喜悦的光芒"))
            .register();
    public static final ResourceKey<DamageType> TURBINE = REGISTRATE.damageType("turbine")
            .localization(text -> text.source("%s put their head into a turbine").translation(MinecraftLocale.ZH_CN, "%s一头扎进了涡轮里"))
            .register();

    public static void init() {}

    public static void bootstrap(BootstrapContext<DamageType> ctx) {
        ctx.register(HEAT, new DamageType("gtpm.heat", 0, DamageEffects.BURNING));
        ctx.register(CHEMICAL, new DamageType("gtpm.chemical", 0));
        ctx.register(ELECTRIC, new DamageType("gtpm.electric", 0));
        ctx.register(RADIATION, new DamageType("gtpm.radiation", 0));
        ctx.register(TURBINE, new DamageType("gtpm.turbine", 0));

        for (var entry : MedicalCondition.CONDITIONS.entrySet()) {
            String name = entry.getKey();
            MedicalCondition condition = entry.getValue();
            ctx.register(condition.getDamageType(),
                    new DamageType("gtpm.medical_condition." + name, DamageScaling.NEVER, 0));
        }
    }
}

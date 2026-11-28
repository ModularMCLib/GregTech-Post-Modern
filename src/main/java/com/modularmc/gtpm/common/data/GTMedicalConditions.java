package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.data.medicalcondition.MedicalCondition;
import com.modularmc.gtpm.api.data.medicalcondition.Symptom;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import static com.modularmc.gtpm.api.data.tag.TagPrefix.dust;
import static com.modularmc.gtpm.common.data.GTMaterials.*;

@TransVariants(keyPrefix = "death.attack.gtpm.medical_condition",
               value = {
                       @Variant(id = "arsenicosis",
                                source = "%s got arsenic poisoning",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s死于砷中毒")),
                       @Variant(id = "asbestosis",
                                source = "%s got mesothelioma",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s得了间皮瘤")),
                       @Variant(id = "berylliosis",
                                source = "%s mined emeralds a bit too greedily",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s贪婪地挖掘绿宝石，结果有点过火了")),
                       @Variant(id = "carbon_monoxide_poisoning",
                                source = "%s left the stove on",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s离开时炉子还开着")),
                       @Variant(id = "carcinogen",
                                source = "%s got leukemia",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s身患白血病")),
                       @Variant(id = "chemical_burns",
                                source = "%s had a chemical accident",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s历经了一场化学事故")),
                       @Variant(id = "irritant",
                                source = "%s got a §n§lREALLY§r bad rash",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%s身上长了§n§l极其严重§r的皮疹")),
                       @Variant(id = "methanol_poisoning",
                                source = "%s tried to drink moonshine during the prohibition",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s在禁酒令下尝试饮用私酿烈酒")),
                       @Variant(id = "nausea",
                                source = "%s died of nausea",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s死于恶心")),
                       @Variant(id = "none",
                                source = "%s died of... nothing?",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s死于...什么都没有？")),
                       @Variant(id = "poison",
                                source = "%s forgot that poisonous materials are, in fact, poisonous",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s忘了毒物的确有毒")),
                       @Variant(id = "silicosis",
                                source = "%s didn't die of tuberculosis. it was silicosis.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s不是死于肺结核，而是矽肺病")),
                       @Variant(id = "weak_poison",
                                source = "%s ate lead (or mercury!)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s吞下了铅（或水银！）")),
               })
public class GTMedicalConditions {

    // General Conditions
    @Trans(key = "gtpm.medical_condition.none",
           source = "§2Not Dangerous",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§2无害"))
    public static final MedicalCondition NONE = new MedicalCondition("none", 0xffffff, 0);
    @Trans(key = "gtpm.medical_condition.chemical_burns",
           source = "§5Chemical burns",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§5化学烧伤"))
    public static final MedicalCondition CHEMICAL_BURNS = new MedicalCondition("chemical_burns", 0xbc305a, 200,
            MedicalCondition.IdleProgressionType.HEAL, 2, false,
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS))
            .setRecipeModifier(builder -> builder
                    .outputFluids(DilutedHydrochloricAcid.getFluid(500))
                    .outputFluids(DilutedSulfuricAcid.getFluid(750)));
    @Trans(key = "gtpm.medical_condition.poison",
           source = "§2Poisonous",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§2中毒"))
    public static final MedicalCondition POISON = new MedicalCondition("poison", 0xA36300, 600,
            MedicalCondition.IdleProgressionType.HEAL, 2, true,
            new Symptom.ConfiguredSymptom(Symptom.WEAK_POISONING),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, .5f))
            .setRecipeModifier(builder -> builder
                    .outputFluids(SulfurTrioxide.getFluid(1000)));
    @Trans(key = "gtpm.medical_condition.weak_poison",
           source = "§aWeakly poisonous",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a轻度中毒"))
    public static final MedicalCondition WEAK_POISON = new MedicalCondition("weak_poison", 0x6D7917, 3000,
            MedicalCondition.IdleProgressionType.NONE, 0, false,
            new Symptom.ConfiguredSymptom(Symptom.WEAK_POISONING, 6, .3f))
            .setRecipeModifier(builder -> builder
                    .outputFluids(NitricOxide.getFluid(1000)));
    @Trans(key = "gtpm.medical_condition.irritant",
           source = "§6Irritant",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6刺激性"))
    public static final MedicalCondition IRRITANT = new MedicalCondition("irritant", 0x02512f, 600,
            MedicalCondition.IdleProgressionType.HEAL, 5, false,
            new Symptom.ConfiguredSymptom(Symptom.RANDOM_DAMAGE),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, 0.5f))
            .setRecipeModifier(builder -> builder
                    .outputItems(dust, DarkAsh, 4));
    @Trans(key = "gtpm.medical_condition.nausea",
           source = "§3Nauseating",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§3恶心"))
    public static final MedicalCondition NAUSEA = new MedicalCondition("nausea", 0x1D4A00, 600,
            MedicalCondition.IdleProgressionType.HEAL, 5, false,
            new Symptom.ConfiguredSymptom(Symptom.NAUSEA))
            .setRecipeModifier(builder -> builder
                    .outputFluids(CarbonMonoxide.getFluid(50)));
    @Trans(key = "gtpm.medical_condition.carcinogen",
           source = "§eCarcinogenic",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e致癌"))
    public static final MedicalCondition CARCINOGEN = new MedicalCondition("carcinogen", 0x0f570f, 20000,
            MedicalCondition.IdleProgressionType.NONE, 0, true,
            new Symptom.ConfiguredSymptom(Symptom.DEATH),
            new Symptom.ConfiguredSymptom(Symptom.HEALTH_DEBUFF, .75f),
            new Symptom.ConfiguredSymptom(Symptom.AIR_SUPPLY_DEBUFF, .5f),
            new Symptom.ConfiguredSymptom(Symptom.MINING_FATIGUE, .4f),
            new Symptom.ConfiguredSymptom(Symptom.SLOWNESS, .4f),
            new Symptom.ConfiguredSymptom(Symptom.HUNGER, .3f),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, .2f));

    // Material specific Conditions
    @Trans(key = "gtpm.medical_condition.asbestosis",
           source = "§dAsbestosis",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§d石棉肺"))
    public static final MedicalCondition ASBESTOSIS = new MedicalCondition("asbestosis", 0xe3e3e3, 5000,
            MedicalCondition.IdleProgressionType.HEAL, 1, true,
            new Symptom.ConfiguredSymptom(Symptom.HEALTH_DEBUFF, .6f),
            new Symptom.ConfiguredSymptom(Symptom.AIR_SUPPLY_DEBUFF, .3f),
            new Symptom.ConfiguredSymptom(Symptom.HUNGER, .2f),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, .2f))
            .setRecipeModifier(builder -> builder
                    .outputItems(dust, Asbestos, 4));
    @Trans(key = "gtpm.medical_condition.arsenicosis",
           source = "§bArsenicosis",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b砷中毒"))
    public static final MedicalCondition ARSENICOSIS = new MedicalCondition("arsenicosis", 0xbd4b15, 1000,
            MedicalCondition.IdleProgressionType.HEAL, 1, true,
            new Symptom.ConfiguredSymptom(Symptom.WITHER),
            new Symptom.ConfiguredSymptom(Symptom.HEALTH_DEBUFF, .6f),
            new Symptom.ConfiguredSymptom(Symptom.SLOWNESS, 2, .5f),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, 2, .3f),
            new Symptom.ConfiguredSymptom(Symptom.HUNGER, 2, .2f),
            new Symptom.ConfiguredSymptom(Symptom.NAUSEA, .1f))
            .setRecipeModifier(builder -> builder
                    .outputItems(dust, Arsenic, 4));
    @Trans(key = "gtpm.medical_condition.silicosis",
           source = "§1Silicosis",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§1矽肺病"))
    public static final MedicalCondition SILICOSIS = new MedicalCondition("silicosis", 0x5d6c91, 15000,
            MedicalCondition.IdleProgressionType.HEAL, .5f, true,
            new Symptom.ConfiguredSymptom(Symptom.HEALTH_DEBUFF, 4, .75f),
            new Symptom.ConfiguredSymptom(Symptom.AIR_SUPPLY_DEBUFF, .6f))
            .setRecipeModifier(builder -> builder
                    .outputItems(dust, SiliconDioxide, 4));
    @Trans(key = "gtpm.medical_condition.berylliosis",
           source = "§5Berylliosis",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§5铍中毒"))
    public static final MedicalCondition BERYLLIOSIS = new MedicalCondition("berylliosis", 0x0c6539, 10000,
            MedicalCondition.IdleProgressionType.HEAL, .5f, true,
            new Symptom.ConfiguredSymptom(Symptom.WITHER),
            new Symptom.ConfiguredSymptom(Symptom.RANDOM_DAMAGE, 1, .7f),
            new Symptom.ConfiguredSymptom(Symptom.SLOWNESS, 2, .5f),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, 2, .4f))
            .setRecipeModifier(builder -> builder
                    .outputItems(dust, Beryllium, 4));
    @Trans(key = "gtpm.medical_condition.methanol_poisoning",
           source = "§6Methanol Poisoning",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6甲醇中毒"))
    public static final MedicalCondition METHANOL_POISONING = new MedicalCondition("methanol_poisoning", 0xaa8800, 500,
            MedicalCondition.IdleProgressionType.HEAL, .5f, true,
            new Symptom.ConfiguredSymptom(Symptom.POISONING),
            new Symptom.ConfiguredSymptom(Symptom.SLOWNESS, 1, .75f),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, 2, .5f),
            new Symptom.ConfiguredSymptom(Symptom.BLINDNESS, 2, .25f))
            .setRecipeModifier(builder -> builder
                    .outputFluids(Methanol.getFluid(1000)));
    @Trans(key = "gtpm.medical_condition.carbon_monoxide_poisoning",
           source = "§7Carbon Monoxide Poisoning",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一氧化碳污染"))
    public static final MedicalCondition CARBON_MONOXIDE_POISONING = new MedicalCondition("carbon_monoxide_poisoning",
            0x041525, 2000, MedicalCondition.IdleProgressionType.HEAL, 1, true,
            new Symptom.ConfiguredSymptom(Symptom.DEATH),
            new Symptom.ConfiguredSymptom(Symptom.SLOWNESS, 2, .75f),
            new Symptom.ConfiguredSymptom(Symptom.NAUSEA, 2, .5f),
            new Symptom.ConfiguredSymptom(Symptom.WEAKNESS, 2, .25f))
            .setRecipeModifier(builder -> builder
                    .outputFluids(CarbonMonoxide.getFluid(1000)));
}

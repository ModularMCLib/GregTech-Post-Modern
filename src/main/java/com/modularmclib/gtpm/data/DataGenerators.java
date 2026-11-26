package com.modularmclib.gtpm.data;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.registry.GTRegistries;
import com.modularmclib.gtpm.api.registry.registrate.SoundEntryBuilder;
import com.modularmclib.gtpm.common.data.*;
import com.modularmclib.gtpm.data.tags.BiomeTagsLoader;
import com.modularmclib.gtpm.data.tags.DamageTypeTagsLoader;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;

@EventBusSubscriber(modid = GTPM.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        var registries = event.getLookupProvider();
        if (event.includeClient()) {
            generator.addProvider(true, new SoundEntryBuilder.SoundEntryProvider(packOutput, GTPM.MOD_ID));
        }
        if (event.includeServer()) {
            var set = Set.of(GTPM.MOD_ID);
            generator.addProvider(true, new BiomeTagsLoader(packOutput, registries, existingFileHelper));
            DatapackBuiltinEntriesProvider provider = generator.addProvider(true, new DatapackBuiltinEntriesProvider(
                    packOutput, registries, new RegistrySetBuilder()
                            .add(Registries.DAMAGE_TYPE, GTDamageTypes::bootstrap)
                            .add(Registries.CONFIGURED_FEATURE, GTConfiguredFeatures::bootstrap)
                            .add(Registries.PLACED_FEATURE, GTPlacements::bootstrap)
                            .add(Registries.DENSITY_FUNCTION, GTWorldgen::bootstrap)
                            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GTBiomeModifiers::bootstrap)
                            .add(Registries.JUKEBOX_SONG, GTJukeboxSongs::bootstrap)
                            .add(Registries.ENCHANTMENT_PROVIDER, GTEnchantmentProviders::bootstrap)
                            .add(GTRegistries.BEDROCK_FLUID_REGISTRY, GTBedrockFluids::bootstrap)
                            .add(GTRegistries.ORE_VEIN_REGISTRY, GTOreVeins::bootstrap),
                    set));
            generator.addProvider(true,
                    new DamageTypeTagsLoader(packOutput, provider.getRegistryProvider(), existingFileHelper));
        }
    }
}

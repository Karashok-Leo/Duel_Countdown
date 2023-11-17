package net.karashokleo.duelcountdown;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.karashokleo.duelcountdown.data.RegistryProvider;
import net.karashokleo.duelcountdown.data.LangProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class DuelCountdownData implements DataGeneratorEntrypoint
{

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
    {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(RegistryProvider::new);
        pack.addProvider(LangProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder)
    {
        registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, Constants::bootstrapDimensionType);
        registryBuilder.addRegistry(RegistryKeys.BIOME, Constants::bootstrapBiome);
    }

}

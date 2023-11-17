package net.karashokleo.duelcountdown.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.karashokleo.duelcountdown.Constants;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class RegistryProvider extends FabricDynamicRegistryProvider
{
    public RegistryProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries)
    {
        entries.add(Constants.DIMENSION_TYPE_KEY, Constants.DUEL_DIM_TYPE);
        entries.add(Constants.BIOME_KEY, Constants.DUEL_BIOME);
    }

    @Override
    public String getName()
    {
        return "Duel Field Data";
    }
}

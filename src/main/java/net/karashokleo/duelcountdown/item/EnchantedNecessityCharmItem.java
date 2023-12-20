package net.karashokleo.duelcountdown.item;

import net.karashokleo.duelcountdown.PlayersManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class EnchantedNecessityCharmItem extends NecessityCharmItem
{
    public EnchantedNecessityCharmItem(Settings settings)
    {
        super(settings);
    }

    @Override
    protected void afterActivated(ServerPlayerEntity serverPlayerEntity)
    {
        PlayersManager.backToSpawnPoint(serverPlayerEntity);
    }

    @Override
    public boolean hasGlint(ItemStack stack)
    {
        return true;
    }
}

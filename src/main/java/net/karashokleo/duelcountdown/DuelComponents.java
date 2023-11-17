package net.karashokleo.duelcountdown;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.karashokleo.duelcountdown.component.WinnerComponent;

public class DuelComponents implements EntityComponentInitializer
{
    public static final ComponentKey<WinnerComponent> IS_WINNER = ComponentRegistryV3.INSTANCE.getOrCreate(Constants.IS_WINNER_ID, WinnerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
    {
        registry.registerForPlayers(IS_WINNER, WinnerComponent::new, RespawnCopyStrategy.INVENTORY);
    }
}

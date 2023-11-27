package net.karashokleo.duelcountdown;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.karashokleo.duelcountdown.config.DuelConfig;
import net.minecraft.item.ItemGroups;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class DuelCountdown implements ModInitializer
{
    private static MinecraftServer runningServer;
    private static DuelManager duelManager;

    static
    {
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            runningServer = server;
            duelManager = new DuelManager();
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> duelManager.tick());

        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) ->
        {
            if (killedEntity instanceof ServerPlayerEntity && entity instanceof ServerPlayerEntity killer)
                ServerPlayNetworking.send(killer, DuelInfos.PLAYER_KILL_ID, PacketByteBufs.create());
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((player, origin, destination) ->
        {
            if (isDuelField(origin) && !player.hasStatusEffect(DuelRegistry.WINNER) && !player.hasStatusEffect(DuelRegistry.NECESSITY))
                player.kill();
        }));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries ->
        {
            entries.add(DuelRegistry.NECESSITY_CHARM);
            entries.add(DuelRegistry.ENCHANTED_NECESSITY_CHARM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(DuelRegistry.BACKER_ITEM));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> DuelCommands.register(dispatcher));
    }

    public static MinecraftServer getServer()
    {
        return runningServer;
    }

    public static DuelManager getDuelManager()
    {
        return duelManager;
    }

    public static ServerWorld getDuelField()
    {
        return getServer().getWorld(DuelInfos.WORLD_KEY);
    }

    public static boolean isDuelField(World world)
    {
        return world.getRegistryKey().equals(DuelInfos.WORLD_KEY);
    }

    @Override
    public void onInitialize()
    {
        AutoConfig.register(DuelConfig.class, GsonConfigSerializer::new);
        DuelInfos.CONFIG = AutoConfig.getConfigHolder(DuelConfig.class).getConfig();
        DuelRegistry.register();
    }
}
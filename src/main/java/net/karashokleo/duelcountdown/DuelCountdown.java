package net.karashokleo.duelcountdown;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;

public class DuelCountdown implements ModInitializer
{
    private static final int DUEL_GAP = Constants.DUEL_GAP * 20;
    private static final int COUNTDOWN = (Constants.COUNTDOWN + 1) * 20;

    private static MinecraftServer runningServer;

    public static int countdown;

    static
    {
        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            runningServer = server;
            countdown = DUEL_GAP;
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> countdown = 0);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
        {
            dispatcher.register(CommandManager.literal("duel")
                    .executes(context ->
                    {
                        if (context.getSource().isExecutedByPlayer())
                        {
                            context.getSource().getPlayerOrThrow().teleport(getDuelField(), 0, 1.2, 0, 0, 0);
                        }
                        return 0;
                    })
            );
        });

        ServerTickEvents.START_SERVER_TICK.register(server ->
        {
            countdown--;
            if (countdown % 20 == 0 && countdown <= COUNTDOWN && countdown > 0)
            {
                DuelEvent.updatePlayerList();
                DuelEvent.countdownTitle(countdown / 20 - 1);
            }
            if (countdown == 0)
            {
                countdown = DUEL_GAP;
                DuelEvent.updatePlayerList();
                DuelEvent.startTitle();
                DuelEvent.placePlatform(0, 0, 16, 6);
                DuelEvent.playerReady();
            }
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((player, origin, destination) ->
        {
            if (isDuelField(origin) && player != DuelEvent.winner)
                player.kill();
        }));


    }

    @Override
    public void onInitialize()
    {

    }

    public static MinecraftServer getServer()
    {
        return runningServer;
    }

    public static List<ServerPlayerEntity> getPlayers()
    {
        return getServer().getPlayerManager().getPlayerList();
    }

    public static ServerWorld getDuelField()
    {
        return getServer().getWorld(Constants.WORLD_KEY);
    }

    public static boolean isDuelField(World world)
    {
        return world.getRegistryKey().equals(Constants.WORLD_KEY);
    }
}
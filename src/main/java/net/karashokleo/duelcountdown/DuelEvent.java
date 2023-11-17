package net.karashokleo.duelcountdown;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class DuelEvent
{
    public static List<ServerPlayerEntity> playerToFight = DuelCountdown.getPlayers();
    public static ServerPlayerEntity winner;

    public static void placePlatform(int x0, int z0, int radius, int height)
    {
        ServerWorld world = DuelCountdown.getDuelField();
        for (int x = x0 - radius; x <= x0 + radius; x++)
        {
            for (int z = z0 - radius; z <= z0 + radius; z++)
            {
                if ((x - x0) * (x - x0) + (z - z0) * (z - z0) <= radius * radius)
                {
                    BlockPos pos = new BlockPos(x, 0, z);
                    for (int h = 0; h < height; h++)
                    {
//                        BlockPos pos1 = pos.up(h);
//                        if (!world.getBlockState(pos1).isAir())
//                            world.setBlockState(pos1, Blocks.AIR.getDefaultState());
                        world.setBlockState(pos.up(h), Blocks.AIR.getDefaultState());
                    }
                    world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                }
            }
        }
    }

    public static void playerReady()
    {
        if (playerToFight.isEmpty()) return;
        int x0 = 0;
        int z0 = 0;
        int r = 8;
        int gap = 360 / playerToFight.size();
        for (int i = 0; i < playerToFight.size(); i++)
        {
            double radians = Math.toRadians(i * gap);
            double x = x0 + r * Math.cos(radians);
            double z = z0 + r * Math.sin(radians);
            playerToFight.get(i).teleport(DuelCountdown.getDuelField(), x, 1.2, z, i * gap + 90, 0);
        }
    }

    public static void countdownTitle(int countdown)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(countdown);
        playerToFight.forEach(player ->
        {
            ServerPlayNetworking.send(player, Constants.COUNTDOWN_TITLE_ID, buf);
        });
    }

    public static void startTitle()
    {
        PacketByteBuf buf = PacketByteBufs.create();
        playerToFight.forEach(player ->
        {
            ServerPlayNetworking.send(player, Constants.START_TITLE_ID, buf);
        });
    }

    public static void updatePlayerList()
    {
        playerToFight = survivorsList(playerToFight);
    }

    public static boolean checkDuelEnded()
    {
        List<ServerPlayerEntity> playersInDuel = DuelCountdown.getDuelField().getPlayers();
        playersInDuel = survivorsList(playersInDuel);
        if (playersInDuel.size() == 1)
        {
            winner = playersInDuel.get(0);
            winner.getComponent(DuelComponents.IS_WINNER).setValue(true);
            return true;
        } else return false;
    }

    public static void winnerNotify()
    {
        for (ServerPlayerEntity player : DuelCountdown.getPlayers())
        {
            if (player.getComponent(DuelComponents.IS_WINNER).getValue())
                ServerPlayNetworking.send(player, Constants.PLAYER_WIN_ID, PacketByteBufs.create());
            else ServerPlayNetworking.send(player, Constants.WINNER_NOTIFY_ID, PacketByteBufs.create());
        }
    }

    private static List<ServerPlayerEntity> survivorsList(List<ServerPlayerEntity> playerList)
    {
        return playerList.stream().filter(player -> /*player.interactionManager.isSurvivalLike() &&*/ player.isAlive() && !player.isDisconnected()).toList();
    }
}

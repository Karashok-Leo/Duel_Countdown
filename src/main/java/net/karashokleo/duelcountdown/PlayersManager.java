package net.karashokleo.duelcountdown;

import com.google.common.collect.Sets;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;

public class PlayersManager
{
    private List<ServerPlayerEntity> list;

    public PlayersManager(List<ServerPlayerEntity> playerList)
    {
        this.list = playerList;
    }

    public List<ServerPlayerEntity> getList()
    {
        return list;
    }

    public boolean singlePlayer()
    {
        return getPlayersAmount() == 1;
    }

    public boolean noPlayers()
    {
        return list.isEmpty();
    }

    public int getPlayersAmount()
    {
        return list.size();
    }

    public ServerPlayerEntity getPlayer(int index)
    {
        return list.get(index);
    }

    public static List<ServerPlayerEntity> getPlayers()
    {
        return DuelCountdown.getServer().getPlayerManager().getPlayerList();
    }

    public static PlayersManager withAllPlayers()
    {
        return new PlayersManager(getPlayers());
    }

    public static PlayersManager withAllSurvivors()
    {
        return PlayersManager.withAllPlayers().keepSurvivors();
    }

    public static PlayersManager withAllNonDuelists()
    {
        return PlayersManager.withAllPlayers().keepNonDuelists();
    }

    public static PlayersManager withAllNonNecessitySurvivors()
    {
        return PlayersManager.withAllSurvivors().keepNonNecessity();
    }

    public PlayersManager keepSurvivors()
    {
        this.list = list.stream().filter(player -> player.interactionManager.isSurvivalLike() && player.isAlive() && !player.isDisconnected()).toList();
        return this;
    }

    public PlayersManager keepNonNecessity()
    {
        this.list = this.list.stream().filter(player -> !isNecessity(player)).toList();
        return this;
    }

    public PlayersManager keepDuelists()
    {
        this.list = this.list.stream().filter(PlayersManager::isInDuelField).toList();
        return this;
    }

    public PlayersManager keepNonDuelists()
    {
        this.list = this.list.stream().filter(player -> !isInDuelField(player)).toList();
        return this;
    }

    //Util
    public static boolean isNecessity(PlayerEntity player)
    {
        return player.hasStatusEffect(DuelRegistry.NECESSITY);
    }

    public static boolean isInDuelField(PlayerEntity player)
    {
        return DuelCountdown.isDuelField(player.getWorld());
    }

    public static void updateBarToPlayers(ServerBossBar bar, List<ServerPlayerEntity> list)
    {
        HashSet<ServerPlayerEntity> set = Sets.newHashSet(bar.getPlayers());
        for (ServerPlayerEntity serverPlayerEntity : list)
        {
            if (set.contains(serverPlayerEntity)) continue;
            bar.addPlayer(serverPlayerEntity);
        }
        for (ServerPlayerEntity serverPlayerEntity : set)
        {
            if (list.contains(serverPlayerEntity)) continue;
            bar.removePlayer(serverPlayerEntity);
        }
    }

    public static void backToSpawnPoint(ServerPlayerEntity player)
    {
        ServerWorld spawnWorld = DuelCountdown.getServer().getWorld(player.getSpawnPointDimension());
        BlockPos spawnPos = player.getSpawnPointPosition();
        ServerWorld overWorld = DuelCountdown.getServer().getOverworld();
        BlockPos targetPos = spawnWorld != null && spawnPos != null ? spawnPos : overWorld.getSpawnPos();
        ServerWorld targetWorld = spawnWorld != null && spawnPos != null ? spawnWorld : overWorld;
        player.teleport(targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ(), player.getYaw(), player.getPitch());
        if (spawnPos == null)
            player.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.NO_RESPAWN_BLOCK, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
    }
}

package net.karashokleo.duelcountdown;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.karashokleo.duelcountdown.effect.DuelEffect;
import net.minecraft.block.Blocks;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class Duel
{
    public final List<ServerPlayerEntity> candidates;
    public PlayersManager duelists;
    private final float totalHeath;
    public ServerPlayerEntity winner;
    public Status status;
    private final ServerBossBar bar = new ServerBossBar(Text.translatable("duel.bar.title"), BossBar.Color.PINK, BossBar.Style.NOTCHED_20);
    public int countdown = 20 * (DuelInfos.CONFIG.countdownTitle + 1);
    public int duelTime = 0;
    public int stayTime = 0;
    public int x0 = 0;
    public int z0 = 0;
    public int radius = 16;
    public int radiusReady = 12;
    public int height = 8;

    public Duel(PlayersManager candidates)
    {
        status = Status.COUNTING;
        this.duelists = candidates;
        this.candidates = candidates.getList();
        totalHeath = calculateTotalHealth(this.candidates);
        bar.setVisible(false);
    }

    public void tick()
    {
        if (isCounting())
        {
            if (countdown < 0) return;
            if (countdown % 20 == 0)
            {
                updateCandidates();
                if (countdown == 0)
                {
                    status = Status.ONGOING;
                    startTitle();
                    placePlatform();
                    playerReady();
                } else countdownTitle(countdown / 20 - 1);
            }
            countdown--;
        } else if (isOnGoing())
        {
            if (duelTime % 20 == 0)
            {
                updateDuelists();
                updateBar();
                checkDuelFinished();
            }
            duelTime++;
        } else if (isFinished())
        {
            if (stayTime == DuelInfos.CONFIG.countdownStay * 20)
                bar.setVisible(false);
            stayTime++;
        }
    }

    private float calculateTotalHealth(List<ServerPlayerEntity> list)
    {
        float totalHealth = 0;
        for (ServerPlayerEntity player : list)
            totalHealth += player.getHealth();
        return totalHealth;
    }

    public boolean isCounting()
    {
        return status == Status.COUNTING;
    }

    public boolean isOnGoing()
    {
        return status == Status.ONGOING;
    }

    public boolean isFinished()
    {
        return status == Status.FINISHED;
    }

    public boolean isFinishedWithBarInvisible()
    {
        return isFinished() && !bar.isVisible();
    }

    private void placeBacker()
    {
        ServerWorld world = DuelCountdown.getDuelField();
        BlockPos pos = new BlockPos(0, 1, 0);
        world.setBlockState(pos, DuelRegistry.BACKER.getDefaultState());
    }

    public void placePlatform()
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

    public void playerReady()
    {
        if (duelists.noPlayers()) return;
        int gap = 360 / duelists.getPlayersAmount();
        for (int i = 0; i < duelists.getPlayersAmount(); i++)
        {
            double radians = Math.toRadians(i * gap);
            double x = x0 + radiusReady * Math.cos(radians);
            double z = z0 + radiusReady * Math.sin(radians);
            duelists.getPlayer(i).teleport(DuelCountdown.getDuelField(), x, 1.2, z, i * gap + 90, 0);
        }
    }

    public void countdownTitle(int countdown)
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(countdown);
        duelists.getList().forEach(player -> ServerPlayNetworking.send(player, DuelInfos.COUNTDOWN_TITLE_ID, buf));
    }

    public void startTitle()
    {
        PacketByteBuf buf = PacketByteBufs.create();
        duelists.getList().forEach(player -> ServerPlayNetworking.send(player, DuelInfos.START_TITLE_ID, buf));
    }

    private void updateBar()
    {
        PlayersManager.updateBarToPlayers(bar, candidates);
        bar.setName(DuelInfos.titleText("duel.bar.remain", duelists.getPlayersAmount()));
        bar.setPercent(calculateTotalHealth(duelists.getList()) / totalHeath);
        bar.setVisible(true);
    }

    public void updateCandidates()
    {
        duelists.keepSurvivors();
    }

    public void updateDuelists()
    {
        duelists.keepSurvivors().keepDuelists();
    }

    public void checkDuelFinished()
    {
        if (duelists.noPlayers()) return;
        if (duelists.singlePlayer())
            finishDuel();
    }

    private void finishDuel()
    {
        status = Status.FINISHED;
        winner = duelists.getPlayer(0);
        winner.addStatusEffect(DuelEffect.getWinnerInstance());
//            ServerPlayNetworking.send(winner, DuelInfos.PLAYER_WIN_ID, PacketByteBufs.create());
        bar.setPercent(1F);
        bar.setStyle(BossBar.Style.PROGRESS);
        bar.setColor(BossBar.Color.GREEN);
        bar.setName(DuelInfos.titleText("duel.bar.winner", winner.getEntityName()));
        winnerNotify();
        placeBacker();
    }

    public void winnerNotify()
    {
        if (winner == null) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(winner.getEntityName());
        for (ServerPlayerEntity player : PlayersManager.withAllNonDuelists().getList())
            ServerPlayNetworking.send(player, DuelInfos.WINNER_NOTIFY_ID, buf);
    }

    enum Status
    {
        COUNTING,
        ONGOING,
        FINISHED
    }
}

package net.karashokleo.duelcountdown;

import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class DuelManager
{
    private Duel regularDuel;

    private boolean counting;

    private int countdown = -DuelInfos.CONFIG.countdownGap * 20;

    private int stayTime;

    private final ServerBossBar bar = new ServerBossBar(Text.translatable("duel.bar.title"), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);

    public DuelManager()
    {
        counting = DuelInfos.CONFIG.enableDefault;
    }

    public void startCountdown()
    {
        counting = true;
    }

    public void pauseCountdown()
    {
        counting = false;
        if (countdown > DuelInfos.CONFIG.countdownBar * 20)
            bar.setVisible(false);
    }

    public void stopCountdown()
    {
        counting = false;
        countdown = 0;
        stayTime = DuelInfos.CONFIG.countdownStay * 20;
        bar.setPercent(0F);
        bar.setName(DuelInfos.titleText("duel.bar.stop"));
    }

    public int getCountdown()
    {
        return countdown;
    }

    public void setCountdown(int countdown)
    {
        this.countdown = countdown;
    }

    public void tick()
    {
        if (regularDuel == null)
        {
            if (stayTime >= 0) stayTime--;
            if (stayTime == 0) bar.setVisible(false);
            if (!counting) return;
            countdown++;
            if (countdown >= 0 && countdown <= DuelInfos.CONFIG.countdownBar * 20)
            {
                if (countdown % 20 == 0)
                {
                    PlayersManager.updateBarToPlayers(bar, PlayersManager.getPlayers());
                    bar.setName(DuelInfos.titleText("duel.bar.countdown", DuelInfos.CONFIG.countdownBar - countdown / 20));
                    bar.setVisible(true);
                }
                bar.setPercent(MathHelper.clamp((float) countdown / 20 / DuelInfos.CONFIG.countdownBar, 0F, 1F));
            }
            if (countdown == DuelInfos.CONFIG.countdownBar * 20)
                startRegularDuel();
        } else
        {
            regularDuel.tick();
            if (regularDuel.isFinishedWithBarInvisible()) regularDuel = null;
        }
    }

    public boolean startRegularDuel()
    {
        regularDuel = allPlayersToDuel();
        countdown = -DuelInfos.CONFIG.countdownGap * 20;
        if (regularDuel == null)
        {
            stayTime = DuelInfos.CONFIG.countdownStay * 20;
            bar.setVisible(true);
            bar.setName(DuelInfos.titleText("duel.bar.sufficient"));
            bar.setPercent(0F);
            return false;
        } else
        {
            bar.setVisible(false);
            return true;
        }
    }

    public Duel allPlayersToDuel()
    {
        PlayersManager playersManager = PlayersManager.withAllNonNecessitySurvivors();
        if (playersManager.getPlayersAmount() >= DuelInfos.CONFIG.duelistMin)
            return new Duel(playersManager);
        return null;
    }
}

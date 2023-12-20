package net.karashokleo.duelcountdown.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.karashokleo.duelcountdown.DuelInfos;

@Config(name = DuelInfos.MOD_ID)
public class DuelConfig implements ConfigData
{
    @ConfigEntry.Gui.Tooltip
    public boolean enableDefault = false;

    @ConfigEntry.Gui.Tooltip
    public boolean keepInventory = true;

    @ConfigEntry.Gui.Tooltip
    public boolean keepXp = false;

    @ConfigEntry.Gui.Tooltip
    public int countdownGap = 200;

    @ConfigEntry.Gui.Tooltip
    public int countdownBar = 600;

    @ConfigEntry.Gui.Tooltip
    public int countdownStay = 5;

    @ConfigEntry.Gui.Tooltip
    public int countdownTitle = 10;

    @ConfigEntry.Gui.Tooltip
    public int duelistMin = 2;
}

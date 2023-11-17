package net.karashokleo.duelcountdown.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.karashokleo.duelcountdown.DuelComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class WinnerComponent implements BooleanComponent, AutoSyncedComponent
{
    private boolean isWinner;
    private final PlayerEntity provider;

    public WinnerComponent(PlayerEntity player)
    {
        this.provider = player;
    }

    @Override
    public boolean getValue()
    {
        return this.isWinner;
    }

    public void setValue(boolean value) {
        this.isWinner = value;
        DuelComponents.IS_WINNER.sync(this.provider);
    }

    @Override
    public void readFromNbt(NbtCompound tag)
    {
        this.isWinner = tag.getBoolean("isWinner");
    }

    @Override
    public void writeToNbt(NbtCompound tag)
    {
        tag.putBoolean("isWinner", this.isWinner);
    }
}

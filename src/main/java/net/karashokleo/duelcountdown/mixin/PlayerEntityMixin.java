package net.karashokleo.duelcountdown.mixin;

import net.karashokleo.duelcountdown.DuelInfos;
import net.karashokleo.duelcountdown.PlayersManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
    @Redirect(
            method = "dropInventory",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"
            )
    )
    private boolean dropInventory(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule)
    {
        return (DuelInfos.CONFIG.keepInventory && PlayersManager.isInDuelField((PlayerEntity) (Object) this)) || gameRules.getBoolean(rule);
    }

    @Redirect(
            method = "getXpToDrop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"
            )
    )
    private boolean getXpToDrop(GameRules gameRules, GameRules.Key<GameRules.BooleanRule> rule)
    {
        return (DuelInfos.CONFIG.keepXp && PlayersManager.isInDuelField((PlayerEntity) (Object) this)) || gameRules.getBoolean(rule);
    }
}

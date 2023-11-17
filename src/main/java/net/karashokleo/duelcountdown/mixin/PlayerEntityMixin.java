package net.karashokleo.duelcountdown.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.karashokleo.duelcountdown.Constants;
import net.karashokleo.duelcountdown.DuelCountdown;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
    @WrapOperation(
            method = "dropInventory",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z")
    )
    private boolean dropInventory(GameRules rules, GameRules.Key<GameRules.BooleanRule> rule, Operation<Boolean> original)
    {
        if (DuelCountdown.isDuelField(((PlayerEntity) (Object) this).getWorld()))
            return true;
        else return original.call(rules, rule);
    }
}
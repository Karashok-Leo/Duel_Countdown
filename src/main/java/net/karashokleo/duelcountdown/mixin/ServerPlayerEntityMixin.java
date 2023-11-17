package net.karashokleo.duelcountdown.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.karashokleo.duelcountdown.Constants;
import net.karashokleo.duelcountdown.DuelCountdown;
import net.karashokleo.duelcountdown.DuelEvent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
    @WrapOperation(
            method = "copyFrom",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z")
    )
    private boolean copyFrom(GameRules rules, GameRules.Key<GameRules.BooleanRule> rule, Operation<Boolean> original)
    {
        if (DuelCountdown.isDuelField(((ServerPlayerEntity) (Object) this).getWorld()))
            return true;
        else return original.call(rules, rule);
    }

    @Inject(
            method = "onDeath",
            at = @At(value = "HEAD")
    )
    private void onDeath(DamageSource damageSource, CallbackInfo ci)
    {
        if (DuelCountdown.isDuelField(((ServerPlayerEntity) (Object) this).getWorld()))
            if (DuelEvent.checkDuelEnded())
                DuelEvent.winnerNotify();

    }
}

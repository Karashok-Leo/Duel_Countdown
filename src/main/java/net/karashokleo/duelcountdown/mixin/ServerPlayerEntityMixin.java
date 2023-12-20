package net.karashokleo.duelcountdown.mixin;

import net.karashokleo.duelcountdown.DuelInfos;
import net.karashokleo.duelcountdown.PlayersManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Key;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin
{
    private ServerPlayerEntity oldPlayerCopied;

    @Inject(
            method = "copyFrom",
            at = @At(value = "HEAD")
    )
    private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci)
    {
        oldPlayerCopied = oldPlayer;
    }

    @Redirect(
            method = "copyFrom",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"
            )
    )
    private boolean copyFrom(GameRules gameRules, Key<BooleanRule> rule)
    {
        return (DuelInfos.CONFIG.keepInventory && PlayersManager.isInDuelField(oldPlayerCopied)) || gameRules.getBoolean(rule);
    }

//    @Redirect(
//            method = "copyFrom",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;getWorld()Lnet/minecraft/world/World;"
//            )
//    )
//    private World redirectedGetWorld(ServerPlayerEntity instance) {
//        return oldPlayerCopied.getWorld();
//    }
}

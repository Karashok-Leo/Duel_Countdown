package net.karashokleo.duelcountdown.item;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.karashokleo.duelcountdown.DuelInfos;
import net.karashokleo.duelcountdown.effect.DuelEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NecessityCharmItem extends Item
{
    public NecessityCharmItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        if (world.isClient) return super.use(world, user, hand);
        ItemStack stack = user.getStackInHand(hand);
        user.addStatusEffect(DuelEffect.getNecessityInstance());
        if (user instanceof ServerPlayerEntity serverPlayerEntity)
        {
            ServerPlayNetworking.send(serverPlayerEntity, DuelInfos.NECESSITY_ID, PacketByteBufs.create().writeItemStack(this.getDefaultStack()));
            afterActivated(serverPlayerEntity);
        }
        stack.decrement(1);
        return TypedActionResult.success(stack);
    }

    protected void afterActivated(ServerPlayerEntity serverPlayerEntity)
    {
    }
}

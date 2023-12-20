package net.karashokleo.duelcountdown.block;

import net.karashokleo.duelcountdown.DuelRegistry;
import net.karashokleo.duelcountdown.PlayersManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BackerBlock extends Block
{
    public BackerBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        if (player instanceof ServerPlayerEntity serverPlayer && player.hasStatusEffect(DuelRegistry.WINNER))
            PlayersManager.backToSpawnPoint(serverPlayer);
        return ActionResult.success(world.isClient());
    }
}

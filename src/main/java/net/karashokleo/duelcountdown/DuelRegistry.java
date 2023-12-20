package net.karashokleo.duelcountdown;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.karashokleo.duelcountdown.block.BackerBlock;
import net.karashokleo.duelcountdown.effect.DuelEffect;
import net.karashokleo.duelcountdown.item.EnchantedNecessityCharmItem;
import net.karashokleo.duelcountdown.item.NecessityCharmItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DuelRegistry
{
    public static final NecessityCharmItem NECESSITY_CHARM = new NecessityCharmItem(new FabricItemSettings());
    public static final EnchantedNecessityCharmItem ENCHANTED_NECESSITY_CHARM = new EnchantedNecessityCharmItem(new FabricItemSettings());

    public static final BackerBlock BACKER = new BackerBlock(FabricBlockSettings.create().mapColor(MapColor.PURPLE).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(Blocks::never));
    public static final BlockItem BACKER_ITEM = new BlockItem(BACKER, new FabricItemSettings());

    public static final StatusEffect WINNER = new DuelEffect(0xFFFF00);
    public static final StatusEffect NECESSITY = new DuelEffect(0xFF4500);

    public static void register()
    {
        Registry.register(Registries.ITEM, DuelInfos.NECESSITY_ITEM, NECESSITY_CHARM);
        Registry.register(Registries.ITEM, DuelInfos.ENCHANTED_NECESSITY_ITEM, ENCHANTED_NECESSITY_CHARM);

        Registry.register(Registries.BLOCK, DuelInfos.BACKER_BLOCK, BACKER);
        Registry.register(Registries.ITEM, DuelInfos.BACKER_BLOCK, BACKER_ITEM);

        Registry.register(Registries.STATUS_EFFECT, DuelInfos.WINNER_EFFECT, WINNER);
        Registry.register(Registries.STATUS_EFFECT, DuelInfos.NECESSITY_EFFECT, NECESSITY);
    }
}

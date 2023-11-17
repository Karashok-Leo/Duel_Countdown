package net.karashokleo.duelcountdown;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class DuelCountdownClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientPlayNetworking.registerGlobalReceiver(Constants.COUNTDOWN_TITLE_ID, (client, handler, buf, responseSender)->{
            int countdown = buf.readInt();
            client.execute(() -> {
                client.inGameHud.setTitle(Text.of(String.valueOf(countdown)));
                client.inGameHud.setSubtitle(Text.of("Countdown"));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(Constants.START_TITLE_ID, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.inGameHud.setTitle(Text.of("Duel Start!"));
                client.inGameHud.setSubtitle(Text.of("Fight Now"));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(Constants.PLAYER_WIN_ID, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.gameRenderer.showFloatingItem(new ItemStack(Items.DIAMOND_SWORD));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(Constants.WINNER_NOTIFY_ID, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                client.inGameHud.setTitle(Text.of(DuelEvent.winner.getEntityName()));
                client.inGameHud.setSubtitle(Text.of("Winner!"));
            });
        });
    }
}

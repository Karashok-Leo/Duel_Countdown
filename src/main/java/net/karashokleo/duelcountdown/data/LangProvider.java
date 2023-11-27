package net.karashokleo.duelcountdown.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.karashokleo.duelcountdown.DuelRegistry;

public class LangProvider extends FabricLanguageProvider
{
    public LangProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(DuelRegistry.NECESSITY_CHARM, "Necessity Charm");
        translationBuilder.add(DuelRegistry.ENCHANTED_NECESSITY_CHARM, "Enchanted Necessity Charm");

        translationBuilder.add(DuelRegistry.BACKER, "Backer");

        translationBuilder.add(DuelRegistry.WINNER, "Winner");
        translationBuilder.add(DuelRegistry.NECESSITY, "Necessity");

        translationBuilder.add("biome.duel-countdown.duel_field_biome", "Duel Field");

        translationBuilder.add("duel.bar.title", "Duel");
        translationBuilder.add("duel.bar.stop", "Stopped");
        translationBuilder.add("duel.bar.sufficient", "Sufficient number of players");
        translationBuilder.add("duel.bar.countdown", "Countdown: %1$s");
        translationBuilder.add("duel.bar.remain", "Remain: %1$s");
        translationBuilder.add("duel.bar.winner", "Winner: %1$s");

        translationBuilder.add("duel.commands.auto.start", "Countdown started!");
        translationBuilder.add("duel.commands.auto.pause", "Countdown paused!");
        translationBuilder.add("duel.commands.auto.stop", "Countdown stopped!");
        translationBuilder.add("duel.commands.auto.gap.get", "Current duel gap is: %1$s seconds");
        translationBuilder.add("duel.commands.auto.gap.set", "Duel gap has been set to: %1$s seconds");
        translationBuilder.add("duel.commands.auto.rest.get", "Current countdown is: %1$s seconds");
        translationBuilder.add("duel.commands.auto.rest.set", "Countdown has been set to: %1$s seconds");
    }
}

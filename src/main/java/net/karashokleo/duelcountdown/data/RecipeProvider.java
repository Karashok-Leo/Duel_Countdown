package net.karashokleo.duelcountdown.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.karashokleo.duelcountdown.DuelRegistry;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider
{
    public RecipeProvider(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter)
    {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, DuelRegistry.NECESSITY_CHARM)
                .input(Items.PLAYER_HEAD)
                .criterion(FabricRecipeProvider.hasItem(DuelRegistry.NECESSITY_CHARM), FabricRecipeProvider.conditionsFromItem(DuelRegistry.NECESSITY_CHARM))
                .criterion(FabricRecipeProvider.hasItem(Items.PLAYER_HEAD), FabricRecipeProvider.conditionsFromItem(Items.PLAYER_HEAD))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder
                .create(RecipeCategory.MISC, DuelRegistry.ENCHANTED_NECESSITY_CHARM)
                .pattern("ooo")
                .pattern("oco")
                .pattern("ooo")
                .input('o', Items.CRYING_OBSIDIAN)
                .input('c', DuelRegistry.NECESSITY_CHARM)
                .criterion(FabricRecipeProvider.hasItem(DuelRegistry.ENCHANTED_NECESSITY_CHARM), FabricRecipeProvider.conditionsFromItem(DuelRegistry.ENCHANTED_NECESSITY_CHARM))
                .criterion(FabricRecipeProvider.hasItem(Items.CRYING_OBSIDIAN), FabricRecipeProvider.conditionsFromItem(Items.CRYING_OBSIDIAN))
                .criterion(FabricRecipeProvider.hasItem(DuelRegistry.NECESSITY_CHARM), FabricRecipeProvider.conditionsFromItem(DuelRegistry.NECESSITY_CHARM))
                .offerTo(exporter);

    }
}

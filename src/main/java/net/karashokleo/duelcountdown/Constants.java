package net.karashokleo.duelcountdown;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class Constants
{
    public static final String MOD_ID = "duel-countdown";

    public static final int DUEL_GAP = 30;
    public static final int COUNTDOWN = 10;

    public static final Identifier COUNTDOWN_TITLE_ID = new Identifier(MOD_ID, "countdown_title");
    public static final Identifier START_TITLE_ID = new Identifier(MOD_ID, "start_title");
    public static final Identifier PLAYER_WIN_ID = new Identifier(MOD_ID, "player_win");
    public static final Identifier WINNER_NOTIFY_ID = new Identifier(MOD_ID, "winner_notify");

    public static final Identifier DIM_ID = new Identifier(MOD_ID, "duel_field_dim");
    public static final Identifier DIM_TYPE_ID = new Identifier(MOD_ID, "duel_field_dim_type");
    public static final Identifier BIOME_ID = new Identifier(MOD_ID, "duel_field_biome");

    public static final Identifier IS_WINNER_ID = new Identifier(MOD_ID, "is_winner");

    public static final RegistryKey<World> WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, DIM_ID);
    public static final RegistryKey<DimensionOptions> DIMENSION_KEY = RegistryKey.of(RegistryKeys.DIMENSION, DIM_ID);
    public static final RegistryKey<DimensionType> DIMENSION_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, DIM_TYPE_ID);
    public static final RegistryKey<Biome> BIOME_KEY = RegistryKey.of(RegistryKeys.BIOME, BIOME_ID);

    public static DimensionType DUEL_DIM_TYPE = new DimensionType(
            OptionalLong.of(6000),
            true,
            false,
            false,
            false,
            1.0D,
            true,
            true,
            0,
            384,
            384,
            BlockTags.INFINIBURN_OVERWORLD,
            DimensionTypes.THE_END_ID,
            1.0F,
            new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 7), 0)
    );

    public static Biome DUEL_BIOME = new Biome.Builder()
            .temperature(1F)
            .downfall(0.4F)
            .precipitation(false)
            .temperatureModifier(Biome.TemperatureModifier.NONE)
            .spawnSettings(new SpawnSettings.Builder().build())
            .generationSettings(new GenerationSettings.Builder().build())
            .effects(new BiomeEffects.Builder()
                    .skyColor(8103167)
                    .fogColor(12638463)
                    .waterColor(4445678)
                    .waterFogColor(270131)
                    .build()
            )
            .build();

    public static void bootstrapDimensionType(Registerable<DimensionType> registerable)
    {
        registerable.register(DIMENSION_TYPE_KEY, DUEL_DIM_TYPE);
    }

    public static void bootstrapBiome(Registerable<Biome> registerable)
    {
        registerable.register(BIOME_KEY, DUEL_BIOME);
    }
}

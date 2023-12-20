package net.karashokleo.duelcountdown.effect;

import net.karashokleo.duelcountdown.DuelRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;

public class DuelEffect extends StatusEffect
{
    public DuelEffect(int color)
    {
        super(StatusEffectCategory.BENEFICIAL, color);
    }

    public static StatusEffectInstance getWinnerInstance()
    {
        return new StatusEffectInstance(DuelRegistry.WINNER, 10 * 60 * 20, 0, false, false);
    }

    public static StatusEffectInstance getNecessityInstance()
    {
        return new StatusEffectInstance(DuelRegistry.NECESSITY, 5 * 60 * 20, 0, false, false);
    }
}

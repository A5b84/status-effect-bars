package io.github.a5b84.statuseffectbars.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.MobEffectInstanceDuck;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents effects' max duration from being overwritten.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "forceAddEffect", at = @At(value = "TAIL"))
    private void onSetStatusEffect(MobEffectInstance effect, Entity source, CallbackInfo ci, @Local(ordinal = 1) MobEffectInstance oldEffect) {
        if (oldEffect != null && oldEffect.getAmplifier() == effect.getAmplifier()) {
            MobEffectInstanceDuck duck = (MobEffectInstanceDuck) effect;
            MobEffectInstanceDuck oldDuck = (MobEffectInstanceDuck) oldEffect;
            if (duck.statusEffectBars_getMaxDuration() < oldDuck.statusEffectBars_getMaxDuration()) {
                duck.statusEffectBars_setMaxDuration(
                        oldDuck.statusEffectBars_getMaxDuration()
                );
            }
        }
    }

}

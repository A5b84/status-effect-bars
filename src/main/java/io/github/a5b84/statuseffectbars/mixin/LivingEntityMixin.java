package io.github.a5b84.statuseffectbars.mixin;

import io.github.a5b84.statuseffectbars.StatusEffectInstanceDuck;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Prevents effects' max duration from being overwritten.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "setStatusEffect",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onSetStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfo ci, StatusEffectInstance oldEffect) {
        if (oldEffect != null && oldEffect.getAmplifier() == effect.getAmplifier()) {
            StatusEffectInstanceDuck duck = (StatusEffectInstanceDuck) effect;
            StatusEffectInstanceDuck oldDuck = (StatusEffectInstanceDuck) oldEffect;
            if (duck.statusEffectBars_getMaxDuration() < oldDuck.statusEffectBars_getMaxDuration()) {
                duck.statusEffectBars_setMaxDuration(
                        oldDuck.statusEffectBars_getMaxDuration()
                );
            }
        }
    }

}

package io.github.a5b84.statuseffectbars.mixin;

import io.github.a5b84.statuseffectbars.StatusEffectInstanceDuck;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Adds the {@link #maxDuration} attribute used for rendering.
 */
@Mixin(StatusEffectInstance.class)
public abstract class StatusEffectInstanceMixin implements StatusEffectInstanceDuck {

    @Unique private int maxDuration;
    @Shadow private int duration;

    @Inject(method = "<init>(Lnet/minecraft/entity/effect/StatusEffect;IIZZZLnet/minecraft/entity/effect/StatusEffectInstance;Ljava/util/Optional;)V",
            at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        maxDuration = duration;
    }

    @Inject(method = "copyFrom", at = @At("RETURN"))
    private void onCopyFrom(StatusEffectInstance that, CallbackInfo ci) {
        maxDuration = ((StatusEffectInstanceMixin) (Object) that).maxDuration;
    }

    @Inject(method = "upgrade", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;duration:I", opcode = Opcodes.PUTFIELD))
    private void onUpgrade(StatusEffectInstance that, CallbackInfoReturnable<Boolean> cir) {
        maxDuration = ((StatusEffectInstanceMixin) (Object) that).maxDuration;
    }

    @Override
    public int statusEffectBars_getMaxDuration() {
        return maxDuration;
    }

    @Override
    public void statusEffectBars_setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }
}

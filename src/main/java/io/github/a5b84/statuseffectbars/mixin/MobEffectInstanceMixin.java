package io.github.a5b84.statuseffectbars.mixin;

import io.github.a5b84.statuseffectbars.MobEffectInstanceDuck;
import net.minecraft.world.effect.MobEffectInstance;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Adds the {@link #maxDuration} attribute used for rendering. */
@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements MobEffectInstanceDuck {

  @Unique private int maxDuration;
  @Shadow private int duration;

  @Inject(
      method =
          "<init>(Lnet/minecraft/core/Holder;IIZZZLnet/minecraft/world/effect/MobEffectInstance;)V",
      at = @At("RETURN"))
  private void onInit(CallbackInfo ci) {
    maxDuration = duration;
  }

  @Inject(method = "setDetailsFrom", at = @At("RETURN"))
  private void onCopyFrom(MobEffectInstance copy, CallbackInfo ci) {
    maxDuration = ((MobEffectInstanceMixin) (Object) copy).maxDuration;
  }

  @Inject(
      method = "update",
      at =
          @At(
              value = "FIELD",
              target = "Lnet/minecraft/world/effect/MobEffectInstance;duration:I",
              opcode = Opcodes.PUTFIELD))
  private void onUpgrade(MobEffectInstance takeOver, CallbackInfoReturnable<Boolean> cir) {
    maxDuration = ((MobEffectInstanceMixin) (Object) takeOver).maxDuration;
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

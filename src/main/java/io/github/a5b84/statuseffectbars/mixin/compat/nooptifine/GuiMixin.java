package io.github.a5b84.statuseffectbars.mixin.compat.nooptifine;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Renders the duration bars under status effects.
 *
 * @see io.github.a5b84.statuseffectbars.mixin.compat.optifine.GuiMixin
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

  @Unique private static final int ICON_SIZE = 24;

  @Inject(
      method = "renderEffects",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/Gui;getMobEffectSprite(Lnet/minecraft/core/Holder;)Lnet/minecraft/resources/Identifier;",
              ordinal = 0))
  private void onRenderEffects(
      GuiGraphics guiGraphics,
      DeltaTracker deltaTracker,
      CallbackInfo ci,
      @Local MobEffectInstance effect,
      @Local(ordinal = 2) int x,
      @Local(ordinal = 3) int y) {
    StatusEffectBarRenderer.render(
        guiGraphics, deltaTracker, effect, x, y, ICON_SIZE, ICON_SIZE, config.hudLayout);
  }
}

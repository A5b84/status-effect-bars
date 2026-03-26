package io.github.a5b84.statuseffectbars.mixin.compat.optifine;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Renders the duration bars under status effects. (Same as {@link
 * io.github.a5b84.statuseffectbars.mixin.compat.nooptifine.GuiMixin nooptifine.InGameHudMixin} but
 * without locals capture.)
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

  @Unique private static final int ICON_SIZE = 24;

  @Unique private int beneficialColumn;
  @Unique private int othersColumn;

  @Shadow @Final private Minecraft minecraft;

  @Inject(method = "extractEffects", at = @At("HEAD"))
  private void onBeforeRenderEffects(CallbackInfo ci) {
    beneficialColumn = othersColumn = 0;
  }

  @Inject(
      method = "extractEffects",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/Gui;getMobEffectSprite(Lnet/minecraft/core/Holder;)Lnet/minecraft/resources/Identifier;",
              ordinal = 0))
  private void onExtractEffects(
      GuiGraphicsExtractor graphics,
      DeltaTracker deltaTracker,
      CallbackInfo ci,
      @Local(name = "instance") MobEffectInstance effect) {
    // Mirroring vanilla behavior (easier than capturing locals because of OptiFine)
    int x = graphics.guiWidth();
    int y = 1;
    if (minecraft.isDemo()) y += 15;

    if (effect.getEffect().value().isBeneficial()) {
      beneficialColumn++;
      x -= (ICON_SIZE + 1) * beneficialColumn;
    } else {
      othersColumn++;
      x -= (ICON_SIZE + 1) * othersColumn;
      y += ICON_SIZE + 2;
    }

    StatusEffectBarRenderer.render(
        graphics, deltaTracker, effect, x, y, ICON_SIZE, ICON_SIZE, config.hudLayout);
  }
}

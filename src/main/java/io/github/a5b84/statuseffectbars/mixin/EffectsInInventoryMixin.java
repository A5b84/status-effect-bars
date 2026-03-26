package io.github.a5b84.statuseffectbars.mixin;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import java.util.Collection;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Renders the duration bars under status effects. */
@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {

  @Inject(
      method = "extractEffects",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/screens/inventory/EffectsInInventory;extractText(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;IIIIII)V"))
  private void onDrawStatusEffectBackground(
      GuiGraphicsExtractor graphics,
      Collection<MobEffectInstance> effects,
      int x0,
      int yStep,
      int mouseX,
      int mouseY,
      int maxWidth,
      CallbackInfo ci,
      @Local(name = "y0") int y0,
      @Local(name = "textureWidth") int textureWidth,
      @Local(name = "effect") MobEffectInstance effect) {
    StatusEffectBarRenderer.render(
        graphics,
        null,
        effect,
        x0,
        y0,
        textureWidth,
        EffectsInInventory.SPRITE_SQUARE_SIZE,
        config.inventoryLayout);
  }
}

package io.github.a5b84.statuseffectbars.mixin;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import java.util.Collection;
import net.minecraft.client.gui.GuiGraphics;
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
      method = "renderEffects",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/client/gui/screens/inventory/EffectsInInventory;renderText(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;IIIIII)V"))
  private void onDrawStatusEffectBackground(
      GuiGraphics guiGraphics,
      Collection<MobEffectInstance> effects,
      int x,
      int rowSpacing,
      int k,
      int l,
      int maxWidth,
      CallbackInfo ci,
      @Local(ordinal = 5) int y,
      @Local(ordinal = 6) int actualWidth,
      @Local MobEffectInstance effect) {
    StatusEffectBarRenderer.render(
        guiGraphics,
        null,
        effect,
        x,
        y,
        actualWidth,
        EffectsInInventory.SPRITE_SQUARE_SIZE,
        config.inventoryLayout);
  }
}

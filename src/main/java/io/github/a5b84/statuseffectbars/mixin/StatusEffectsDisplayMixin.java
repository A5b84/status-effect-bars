package io.github.a5b84.statuseffectbars.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 */
@Mixin(StatusEffectsDisplay.class)
public abstract class StatusEffectsDisplayMixin {

    @Inject(method = "drawStatusEffectBackgrounds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIII)V", shift = At.Shift.AFTER))
    private void onDrawStatusEffectBackground(DrawContext context, int x, int height, Iterable<StatusEffectInstance> effects, boolean wide, CallbackInfo ci, @Local(ordinal = 2) int y, @Local StatusEffectInstance effect) {
        StatusEffectBarRenderer.render(context, null, effect, x, y, wide ? 120 : 32, 32, config.inventoryLayout);
    }

}

package io.github.a5b84.statuseffectbars.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 */
@Mixin(EffectsInInventory.class)
public abstract class EffectsInInventoryMixin {

    @Inject(method = "renderBackgrounds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V", shift = At.Shift.AFTER))
    private void onDrawStatusEffectBackground(GuiGraphics guiGraphics, int x, int height, Iterable<MobEffectInstance> effects, boolean wide, CallbackInfo ci, @Local(ordinal = 2) int y, @Local MobEffectInstance effect) {
        StatusEffectBarRenderer.render(guiGraphics, null, effect, x, y, wide ? 120 : 32, 32, config.inventoryLayout);
    }
}

package io.github.a5b84.statuseffectbars.mixin;

import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 */
@Mixin(AbstractInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin {

    @Inject(method = "drawStatusEffectBackgrounds",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/AbstractInventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onDrawStatusEffectBackground(
            MatrixStack matrices, int x, int verticalSpacing,
            Iterable<StatusEffectInstance> effects, boolean wide,
            CallbackInfo ci, int y, Iterator<StatusEffectInstance> it,
            StatusEffectInstance effect) {
        StatusEffectBarRenderer.render(matrices, effect, x, y, wide ? 120 : 32, 32, config.inventoryLayout);
    }

}

package io.github.a5b84.statuseffectbars.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Inject(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderStatusEffectOverlay(
            MatrixStack matrices, CallbackInfo ci,
            Collection<StatusEffectInstance> effects, int beneficialColumn,
            int othersColumn, StatusEffectSpriteManager spriteManager,
            List<Runnable> spriteRunnables, Iterator<StatusEffectInstance> it,
            StatusEffectInstance effect, StatusEffect type, int x, int y) {
        StatusEffectBarRenderer.render(matrices, effect, x, y, 24, 24, config.hudLayout);
        RenderSystem.enableBlend(); // disabled by DrawableHelper#fill
    }

}

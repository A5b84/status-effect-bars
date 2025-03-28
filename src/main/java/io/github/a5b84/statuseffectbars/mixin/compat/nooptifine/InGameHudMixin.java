package io.github.a5b84.statuseffectbars.mixin.compat.nooptifine;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 * @see io.github.a5b84.statuseffectbars.mixin.compat.optifine.InGameHudMixin
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique private static final int ICON_SIZE = 24;

    @Inject(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0))
    private void onRenderStatusEffectOverlay(
            DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci,
            @Local StatusEffectInstance effect, @Local(ordinal = 2) int x, @Local(ordinal = 3) int y) {
        StatusEffectBarRenderer.render(context, tickCounter, effect, x, y, ICON_SIZE, ICON_SIZE, config.hudLayout);
    }

}

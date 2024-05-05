package io.github.a5b84.statuseffectbars.mixin.compat.nooptifine;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
 * @see io.github.a5b84.statuseffectbars.mixin.compat.optifine.InGameHudMixin
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique private static final int ICON_SIZE = 24;

    @Inject(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderStatusEffectOverlay(
            DrawContext context, float tickDelta, CallbackInfo ci,
            Collection<StatusEffectInstance> effects, int beneficialColumn,
            int othersColumn, StatusEffectSpriteManager spriteManager,
            List<Runnable> spriteRunnables, Iterator<StatusEffectInstance> it,
            StatusEffectInstance effect, RegistryEntry<StatusEffect> effectType, int x, int y) {
        StatusEffectBarRenderer.render(context, tickDelta, effect, x, y, ICON_SIZE, ICON_SIZE, config.hudLayout);
        RenderSystem.enableBlend(); // disabled by DrawableHelper#fill
    }

}

package io.github.a5b84.statuseffectbars.mixin.compat.optifine;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 * (Same as {@link io.github.a5b84.statuseffectbars.mixin.compat.nooptifine.InGameHudMixin nooptifine.InGameHudMixin}
 * but without locals capture.)
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique private static final int ICON_SIZE = 24;

    @Unique private int beneficialColumn;
    @Unique private int othersColumn;

    @Shadow @Final private MinecraftClient client;


    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"))
    private void onBeforeRenderStatusEffectOverlay(CallbackInfo ci) {
        beneficialColumn = othersColumn = 0;
    }

    @Inject(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0))
    private void onRenderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci, @Local StatusEffectInstance effect) {
        // Mirroring vanilla behavior (easier than capturing locals because of OptiFine)
        int x = context.getScaledWindowWidth();
        int y = 1;
        if (client.isDemo()) y += 15;

        if (effect.getEffectType().value().isBeneficial()) {
            beneficialColumn++;
            x -= (ICON_SIZE + 1) * beneficialColumn;
        } else {
            othersColumn++;
            x -= (ICON_SIZE + 1) * othersColumn;
            y += ICON_SIZE + 2;
        }

        StatusEffectBarRenderer.render(context, tickCounter, effect, x, y, ICON_SIZE, ICON_SIZE, config.hudLayout);
        RenderSystem.enableBlend(); // disabled by DrawableHelper#fill
    }

}

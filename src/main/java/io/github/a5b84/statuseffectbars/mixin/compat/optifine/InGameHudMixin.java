package io.github.a5b84.statuseffectbars.mixin.compat.optifine;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.a5b84.statuseffectbars.StatusEffectBarRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 * (Same as {@link io.github.a5b84.statuseffectbars.mixin.compat.nooptifine.InGameHudMixin nooptifine.InGameHudMixin}
 * but without locals capture.)
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @Unique private static final int ICON_SIZE = 24;

    @Unique private int beneficialColumn;
    @Unique private int othersColumn;
    @Unique private StatusEffectInstance effect;

    @Shadow private int scaledWidth;
    @Shadow @Final private MinecraftClient client;


    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"))
    private void onBeforeRenderStatusEffectOverlay(CallbackInfo ci) {
        beneficialColumn = othersColumn = 0;
    }

    @ModifyVariable(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;getEffectType()Lnet/minecraft/entity/effect/StatusEffect;"))
    private StatusEffectInstance storeEffect(StatusEffectInstance effect) {
        this.effect = effect;
        return effect;
    }

    @Inject(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0))
    private void onRenderStatusEffectOverlay(MatrixStack matrices, CallbackInfo ci) {
        // Mirroring vanilla behavior (easier than capturing locals because of OptiFine)
        int x = scaledWidth;
        int y = 1;
        if (client.isDemo()) y += 15;

        if (effect.getEffectType().isBeneficial()) {
            beneficialColumn++;
            x -= (ICON_SIZE + 1) * beneficialColumn;
        } else {
            othersColumn++;
            x -= (ICON_SIZE + 1) * othersColumn;
            y += ICON_SIZE + 2;
        }

        StatusEffectBarRenderer.render(matrices, effect, x, y, ICON_SIZE, ICON_SIZE, config.hudLayout);
        RenderSystem.enableBlend(); // disabled by DrawableHelper#fill
    }

}

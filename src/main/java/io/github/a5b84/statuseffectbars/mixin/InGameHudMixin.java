package io.github.a5b84.statuseffectbars.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.a5b84.statuseffectbars.StatusEffectInstanceDuck;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.PLATE_SIZE;
import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

/**
 * Renders the duration bars under status effects.
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    @SuppressWarnings("SuspiciousNameCombination")
    @Inject(method = "renderStatusEffectOverlay",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/StatusEffectSpriteManager;getSprite(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRenderStatusEffectOverlay(
            MatrixStack matrices, CallbackInfo ci,
            Collection<StatusEffectInstance> effects, int beneficialColumn,
            int othersColumn, StatusEffectSpriteManager spriteManager,
            List<Runnable> spriteRunnables, Iterator<StatusEffectInstance> it,
            StatusEffectInstance effect, StatusEffect type, int x, int y) {
        // Special cases where the bar is hidden

        if (effect.getDuration() > config.maxRemainingDuration) return; // Too much time remaining

        StatusEffectInstanceDuck duck = (StatusEffectInstanceDuck) effect;
        int age = duck.statusEffectBars_getMaxDuration() - effect.getDuration();
        if (effect.isAmbient() && age < config.minAmbientAge) return; // Beacon effect too recent

        // start--------+-----end
        // |            |       |
        // +---------middle-----+
        // or
        // start----+
        // |        |
        // |        |
        // +---middle
        // |        |
        // end------+
        int startX, middleX, endX;
        int startY, middleY, endY;

        startX = config.collinearOffset + config.collinearPadding;
        endX = config.collinearOffset + PLATE_SIZE - config.collinearPadding;
        if (config.direction.reverseAxis) {
            int tmp = startX;
            startX = endX;
            endX = tmp;
        }

        float progress = (float) effect.getDuration() / ((StatusEffectInstanceDuck) effect).statusEffectBars_getMaxDuration();
        middleX = Math.round(MathHelper.lerp(progress, startX, endX));

        startY = endY = config.orthogonalOffset;
        middleY = startY + config.thickness;

        if (config.direction.swapXY) {
            // Swapping X and Y to make the bar vertical instead of horizontal
            int tmp;

            tmp = startX;
            startX = startY;
            startY = tmp;

            tmp = middleX;
            middleX = middleY;
            middleY = tmp;

            tmp = endX;
            endX = endY;
            endY = tmp;
        }

        startX += x;
        middleX += x;
        endX += x;
        startY += y;
        middleY += y;
        endY += y;

        fill(matrices, startX, startY, middleX, middleY, config.getColor(effect));
        fill(matrices, middleX, middleY, endX, endY, config.backgroundColor);
        RenderSystem.enableBlend(); // disabled by fill
    }

}

package io.github.a5b84.statuseffectbars;

import io.github.a5b84.statuseffectbars.config.StatusEffectBarsConfig.LayoutConfig;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

public class StatusEffectBarRenderer {

    @SuppressWarnings("SuspiciousNameCombination")
    public static void render(MatrixStack matrices, StatusEffectInstance effect, int x, int y, int width, int height, LayoutConfig layoutConfig) {
        // Special cases where the bar is hidden

        if (!layoutConfig.enabled) return;

        if (effect.getDuration() > config.maxRemainingDuration || effect.isPermanent()) {
            return; // Too much time remaining
        }

        StatusEffectInstanceDuck duck = (StatusEffectInstanceDuck) effect;
        int age = duck.statusEffectBars_getMaxDuration() - effect.getDuration();
        if (effect.isAmbient() && age < config.minAmbientAge) {
            return; // Beacon effect too recent (will probably be refreshed soon)
        }

        if (layoutConfig.direction.swapXY) {
            int tmp = width;
            width = height;
            height = tmp;
        }

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

        startX = layoutConfig.collinearOffset + layoutConfig.collinearPadding;
        endX = layoutConfig.collinearOffset + width - layoutConfig.collinearPadding;
        if (layoutConfig.direction.reverseAxis) {
            int tmp = startX;
            startX = endX;
            endX = tmp;
        }

        float progress = (float) effect.getDuration() / ((StatusEffectInstanceDuck) effect).statusEffectBars_getMaxDuration();
        middleX = Math.round(MathHelper.lerp(progress, startX, endX));

        startY = layoutConfig.orthogonalOffset;
        if (layoutConfig.relativeToEnd) {
            startY = height - (startY + layoutConfig.thickness);
        }
        middleY = startY + layoutConfig.thickness;
        endY = startY;

        if (layoutConfig.direction.swapXY) {
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

        DrawableHelper.fill(matrices, startX, startY, middleX, middleY, config.getColor(effect));
        DrawableHelper.fill(matrices, middleX, middleY, endX, endY, config.backgroundColor);
    }

}

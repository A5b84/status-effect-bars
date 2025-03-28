package io.github.a5b84.statuseffectbars;

import io.github.a5b84.statuseffectbars.config.StatusEffectBarsConfig.LayoutConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import static io.github.a5b84.statuseffectbars.StatusEffectBars.config;

public class StatusEffectBarRenderer {

    @SuppressWarnings("SuspiciousNameCombination")
    public static void render(DrawContext context, @Nullable RenderTickCounter tickCounter, StatusEffectInstance effect, int x, int y, int width, int height, LayoutConfig layoutConfig) {
        // Special cases where the bar is hidden

        if (!layoutConfig.enabled) return;

        if (effect.getDuration() > config.maxRemainingDuration || effect.isInfinite()) {
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

        float tickDelta = tickCounter != null
                ? tickCounter.getTickProgress(false)
                : 0;
        float progress = (effect.getDuration() - tickDelta) / ((StatusEffectInstanceDuck) effect).statusEffectBars_getMaxDuration();
        middleX = MathHelper.lerp(progress, startX, endX);

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

        context.fill(startX, startY, middleX, middleY, config.getColor(effect));
        context.fill(middleX, middleY, endX, endY, config.backgroundColor);
    }

}

package io.github.a5b84.statuseffectbars.config;

import io.github.a5b84.statuseffectbars.StatusEffectBars;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.PrefixText;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

@Config(name = StatusEffectBars.MOD_ID)
public class StatusEffectBarsConfig implements ConfigData {

    // Color

    @PrefixText
    @EnumHandler(option = EnumDisplayOption.BUTTON)
    @Tooltip(count = 4)
    public ColorMode colorMode = ColorMode.EFFECT_COLOR;

    @ColorPicker(allowAlpha = true)
    public int backgroundColor = 0x80000000;

    @PrefixText
    @ColorPicker(allowAlpha = true)
    public int beneficialForegroundColor = 0x80ffffff;
    @ColorPicker(allowAlpha = true)
    public int harmfulForegroundColor = beneficialForegroundColor;
    @ColorPicker(allowAlpha = true)
    public int neutralForegroundColor = beneficialForegroundColor;

    public int getColor(StatusEffectInstance effect) {
        return colorMode.getColor(this, effect);
    }

    // Behavior

    /**
     * Remaining duration in ticks above which the bar is hidden
     */
    @PrefixText
    @Tooltip(count = 2)
    public int maxRemainingDuration = Integer.MAX_VALUE;

    /**
     * Age in ticks under which the bar of ambient (i.e. beacon) effects is hidden
     * @see BeaconBlockEntity#tick
     */
    @Tooltip(count = 2)
    public int minAmbientAge = 80 + 10;

    // Layout

    @Category("hud")
    @TransitiveObject
    public LayoutConfig hudLayout = new LayoutConfig(3, 2);

    @Category("inventory")
    @TransitiveObject
    public LayoutConfig inventoryLayout = new LayoutConfig(4, 3);


    public static class LayoutConfig {

        public boolean enabled = true;

        // Position

        @PrefixText
        @EnumHandler(option = EnumDisplayOption.BUTTON)
        public Direction direction = Direction.LEFT_TO_RIGHT;
        /** Whether the bar is placed relative to the end (bottom/right) or
         * the start (top/left) of the effect rectangle */
        @Tooltip(count = 2) public boolean relativeToEnd = true;

        // Shape

        @PrefixText
        public int thickness = 1;
        @Tooltip public int collinearPadding;

        // Fine-tuning

        /** Offset along the bar's main axis */
        @PrefixText
        @Tooltip public int collinearOffset = 0;
        /** Offset perpendicular to the bar */
        @Tooltip public int orthogonalOffset;

        public LayoutConfig(int collinearPadding, int orthogonalOffset) {
            this.collinearPadding = collinearPadding;
            this.orthogonalOffset = orthogonalOffset;
        }
    }

}

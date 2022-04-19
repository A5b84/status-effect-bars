package io.github.a5b84.statuseffectbars.config;

import io.github.a5b84.statuseffectbars.StatusEffectBars;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.PrefixText;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;

import java.util.function.ToIntBiFunction;

@Config(name = StatusEffectBars.ID)
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

    // Layout

    @PrefixText
    @EnumHandler(option = EnumDisplayOption.BUTTON)
    public Direction direction = Direction.LEFT_TO_RIGHT;

    public int thickness = 1;
    @Tooltip public int collinearPadding = 3;
    /** Offset along the bar's main axis */
    @Tooltip public int collinearOffset = 0;
    /** Offset perpendicular to the bar */
    @Tooltip public int orthogonalOffset = StatusEffectBars.PLATE_SIZE - 3;

    // Behavior

    /**
     * Remaining duration in ticks above which the bar is hidden
     * @see EntityStatusEffectS2CPacket#isPermanent()
     */
    @PrefixText
    @Tooltip(count = 2)
    public int maxRemainingDuration = Short.MAX_VALUE - 1;

    /**
     * Age in ticks under which the bar of ambient (i.e. beacon) effects is hidden
     * @see BeaconBlockEntity#tick
     */
    @Tooltip(count = 2)
    public int minAmbientAge = 80 + 5;


    public enum ColorMode {
        EFFECT_COLOR((config, effect) -> effect.getEffectType().getColor() | 0xff000000),
        @SuppressWarnings("ConstantConditions")
        CATEGORY_COLOR((config, effect) -> effect.getEffectType().getCategory().getFormatting().getColorValue() | 0xff000000),
        CUSTOM((config, effect) -> switch (effect.getEffectType().getCategory()) {
            case BENEFICIAL -> config.beneficialForegroundColor;
            case HARMFUL -> config.harmfulForegroundColor;
            default -> config.neutralForegroundColor;
        });

        private final ToIntBiFunction<StatusEffectBarsConfig, StatusEffectInstance> provider;

        ColorMode(ToIntBiFunction<StatusEffectBarsConfig, StatusEffectInstance> provider) {
            this.provider = provider;
        }

        public int getColor(StatusEffectBarsConfig config, StatusEffectInstance effect) {
            return provider.applyAsInt(config, effect);
        }

        @Override
        public String toString() {
            return I18n.translate("text.autoconfig.status-effect-bars.option.colorMode." + name());
        }
    }


    @SuppressWarnings("unused")
    public enum Direction {
        LEFT_TO_RIGHT(false, false),
        RIGHT_TO_LEFT(false, true),
        BOTTOM_TO_TOP(true, true),
        TOP_TO_BOTTOM(true, false);

        public final boolean swapXY;
        public final boolean reverseAxis;

        Direction(boolean swapXY, boolean reverseAxis) {
            this.swapXY = swapXY;
            this.reverseAxis = reverseAxis;
        }

        @Override
        public String toString() {
            return I18n.translate("text.autoconfig.status-effect-bars.option.direction." + name());
        }
    }

}

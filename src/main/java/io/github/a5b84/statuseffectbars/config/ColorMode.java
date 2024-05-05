package io.github.a5b84.statuseffectbars.config;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.function.ToIntBiFunction;

public enum ColorMode {
    EFFECT_COLOR(
            (config, effect) -> effect.getEffectType().value().getColor() | 0xff000000
    ),
    @SuppressWarnings({"ConstantConditions", "unused"})
    CATEGORY_COLOR(
            (config, effect) -> effect.getEffectType().value().getCategory().getFormatting().getColorValue() | 0xff000000
    ),
    @SuppressWarnings("unused") CUSTOM(
            (config, effect) -> switch (effect.getEffectType().value().getCategory()) {
                case BENEFICIAL -> config.beneficialForegroundColor;
                case HARMFUL -> config.harmfulForegroundColor;
                default -> config.neutralForegroundColor;
            }
    );

    private final ToIntBiFunction<StatusEffectBarsConfig, StatusEffectInstance> colorProvider;

    ColorMode(ToIntBiFunction<StatusEffectBarsConfig, StatusEffectInstance> colorProvider) {
        this.colorProvider = colorProvider;
    }

    public int getColor(StatusEffectBarsConfig config, StatusEffectInstance effect) {
        return colorProvider.applyAsInt(config, effect);
    }

    @Override
    public String toString() {
        return I18n.translate("text.autoconfig.status-effect-bars.option.colorMode." + name());
    }

}

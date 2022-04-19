package io.github.a5b84.statuseffectbars.config;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.function.ToIntBiFunction;

public enum ColorMode {
    EFFECT_COLOR(
            (config, effect) -> effect.getEffectType().getColor() | 0xff000000
    ),
    @SuppressWarnings("ConstantConditions")
    CATEGORY_COLOR(
            (config, effect) -> effect.getEffectType().getCategory().getFormatting().getColorValue() | 0xff000000
    ),
    CUSTOM(
            (config, effect) -> switch (effect.getEffectType().getCategory()) {
                case BENEFICIAL -> config.beneficialForegroundColor;
                case HARMFUL -> config.harmfulForegroundColor;
                default -> config.neutralForegroundColor;
            }
    );

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

package io.github.a5b84.statuseffectbars.config;

import java.util.function.ToIntBiFunction;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.effect.MobEffectInstance;

public enum ColorMode {
  EFFECT_COLOR((config, effect) -> effect.getEffect().value().getColor() | 0xff000000),
  @SuppressWarnings({"ConstantConditions", "unused"})
  CATEGORY_COLOR(
      (config, effect) ->
          effect.getEffect().value().getCategory().getTooltipFormatting().getColor() | 0xff000000),
  @SuppressWarnings("unused")
  CUSTOM(
      (config, effect) ->
          switch (effect.getEffect().value().getCategory()) {
            case BENEFICIAL -> config.beneficialForegroundColor;
            case HARMFUL -> config.harmfulForegroundColor;
            default -> config.neutralForegroundColor;
          });

  private final ToIntBiFunction<StatusEffectBarsConfig, MobEffectInstance> colorProvider;

  ColorMode(ToIntBiFunction<StatusEffectBarsConfig, MobEffectInstance> colorProvider) {
    this.colorProvider = colorProvider;
  }

  public int getColor(StatusEffectBarsConfig config, MobEffectInstance effect) {
    return colorProvider.applyAsInt(config, effect);
  }

  @Override
  public String toString() {
    return I18n.get("text.autoconfig.status-effect-bars.option.colorMode." + name());
  }
}

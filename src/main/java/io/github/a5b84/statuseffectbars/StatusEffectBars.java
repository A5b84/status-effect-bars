package io.github.a5b84.statuseffectbars;

import io.github.a5b84.statuseffectbars.config.StatusEffectBarsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class StatusEffectBars implements ClientModInitializer {

    public static final String ID = "status-effect-bars";

    public static StatusEffectBarsConfig config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(StatusEffectBarsConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(StatusEffectBarsConfig.class).getConfig();
    }

}

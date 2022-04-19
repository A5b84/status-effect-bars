package io.github.a5b84.example;

import io.github.a5b84.example.config.ExampleModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class ExampleMod implements ClientModInitializer {

    public static final String ID = "modid";

    public static ExampleModConfig config;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ExampleModConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ExampleModConfig.class).getConfig();
    }

}

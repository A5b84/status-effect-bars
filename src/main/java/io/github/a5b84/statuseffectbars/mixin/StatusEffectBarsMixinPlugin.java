package io.github.a5b84.statuseffectbars.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class StatusEffectBarsMixinPlugin implements IMixinConfigPlugin {

    private final int mixinPackageLength = "io.github.a5b84.statuseffectbars.mixin.".length();
    private final boolean isOptiFabricLoaded = FabricLoader.getInstance().isModLoaded("optifabric");

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith("compat.optifine.", mixinPackageLength)) {
            return isOptiFabricLoaded;
        } else if (mixinClassName.startsWith("compat.nooptifine.", mixinPackageLength)) {
            return !isOptiFabricLoaded;
        } else {
            return true;
        }
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

}

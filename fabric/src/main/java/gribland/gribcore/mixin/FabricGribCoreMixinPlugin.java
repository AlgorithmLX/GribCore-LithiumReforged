package gribland.gribcore.mixin;

import gribcore.Constantum;
import gribland.gribcore.GribCoreFabric;
import gribland.gribcore.config.LithiumConfig;
import gribland.gribcore.config.Option;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class FabricGribCoreMixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE_ROOT = "gribland.gribcore.mixin.";

    private LithiumConfig config;

    @Override
    public void onLoad(String mixinPackage) {
        try {
            this.config = LithiumConfig.load(new File("./config/lithium.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Could not load configuration file for Lithium", e);
        }

        Constantum.LOGGER.info("Loaded configuration file for Lithium: {} options available, {} override(s) found",
                this.config.getOptionCount(), this.config.getOptionOverrideCount());

        GribCoreFabric.CONFIG = this.config;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!mixinClassName.startsWith(MIXIN_PACKAGE_ROOT)) {
            Constantum.LOGGER.error("Expected mixin '{}' to start with package root '{}', treating as foreign and " +
                    "disabling!", mixinClassName, MIXIN_PACKAGE_ROOT);

            return false;
        }

        String mixin = mixinClassName.substring(MIXIN_PACKAGE_ROOT.length());
        Option option = this.config.getEffectiveOptionForMixin(mixin);

        if (option == null) {
            Constantum.LOGGER.error("No rules matched mixin '{}', treating as foreign and disabling!", mixin);

            return false;
        }

        if (option.isOverridden()) {
            String source = "[unknown]";

            if (option.isUserDefined()) {
                source = "user configuration";
            } else if (option.isModDefined()) {
                source = "mods [" + String.join(", ", option.getDefiningMods()) + "]";
            }

            if (option.isEnabled()) {
                Constantum.LOGGER.warn("Force-enabling mixin '{}' as rule '{}' (added by {}) enables it", mixin,
                        option.getName(), source);
            } else {
                Constantum.LOGGER.warn("Force-disabling mixin '{}' as rule '{}' (added by {}) disables it and children", mixin,
                        option.getName(), source);
            }
        }

        return option.isEnabled();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}

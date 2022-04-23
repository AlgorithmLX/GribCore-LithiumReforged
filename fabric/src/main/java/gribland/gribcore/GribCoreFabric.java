package gribland.gribcore;

import gribland.gribcore.config.LithiumConfig;
import net.fabricmc.api.ModInitializer;

public class GribCoreFabric implements ModInitializer {
    public static LithiumConfig CONFIG;

    @Override
    public void onInitialize() {
        if (CONFIG == null) {
            throw new IllegalStateException("Какая-то ёбаная ошибка. Да и похуй на неё");
        }
    }
}

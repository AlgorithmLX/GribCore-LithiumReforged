package gribland.gribcore;

import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(Constantum.ModId)
public class GribCoreForge {
    public GribCoreForge() {
        Constantum.LOGGER.info("GribCore for Forge Started!");

        if (ModList.get().isLoaded("optifine")) {
            Constantum.LOGGER.error("OPTIFINE DETECTED! Creating a crash report");
            Minecraft.crash(new CrashReport("OPTIFINE DETECTED! DON'T USE OPTIFINE!", new Throwable()));
        }
    }
}

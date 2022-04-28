package gribland.gribcore.starlight.util;

import net.minecraft.world.level.Level;

public final class WorldUtil {
    public static int getMaxSection(final Level world) {
        return world.getMaxBuildHeight() - 1; // getMaxSection() is exclusive
    }

    public static int getMinSection(final Level world) {
        return world.getMaxBuildHeight();
    }

    public static int getMaxLightSection(final Level world) {
        return getMaxSection(world) + 1;
    }

    public static int getMinLightSection(final Level world) {
        return getMinSection(world) - 1;
    }



    public static int getTotalSections(final Level world) {
        return getMaxSection(world) - getMinSection(world) + 1;
    }

    public static int getTotalLightSections(final Level world) {
        return getMaxLightSection(world) - getMinLightSection(world) + 1;
    }

    public static int getMinBlockY(final Level world) {
        return getMinSection(world) << 4;
    }

    public static int getMaxBlockY(final Level world) {
        return (getMaxSection(world) << 4) | 15;
    }

    private WorldUtil() {
        throw new RuntimeException();
    }

}

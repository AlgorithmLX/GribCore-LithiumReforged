package gribland.gribcore.lithium.common.world.blockentity;

public interface SleepingBlockEntity {
    default boolean canTickOnSide(boolean isClient) {
        return true;
    }
}

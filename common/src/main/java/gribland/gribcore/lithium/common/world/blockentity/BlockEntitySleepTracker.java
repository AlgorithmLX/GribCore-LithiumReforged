package gribland.gribcore.lithium.common.world.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockEntitySleepTracker {
    void setAwake(BlockEntity blockEntity, boolean needsTicking);
}
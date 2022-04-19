package gribcore.mixin.world.block_entity_ticking.sleeping;

import gribland.gribcore.lithium.common.world.blockentity.SleepingBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin implements SleepingBlockEntity {
    @Override
    public boolean canTickOnSide(boolean isClient) {
        return !isClient;
    }
}

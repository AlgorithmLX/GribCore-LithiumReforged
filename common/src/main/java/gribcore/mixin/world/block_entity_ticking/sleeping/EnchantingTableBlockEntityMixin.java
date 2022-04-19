package gribcore.mixin.world.block_entity_ticking.sleeping;

import gribland.gribcore.lithium.common.world.blockentity.SleepingBlockEntity;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantingTableBlockEntity.class)
public class EnchantingTableBlockEntityMixin implements SleepingBlockEntity {
    @Override
    public boolean canTickOnSide(boolean isClient) {
        return isClient;
    }
}

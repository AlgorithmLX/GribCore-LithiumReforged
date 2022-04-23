package gribland.gribcore.mixin.lithium.world.block_entity_ticking.sleeping;

import gribland.gribcore.lithium.common.world.blockentity.SleepingBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements SleepingBlockEntity {
}

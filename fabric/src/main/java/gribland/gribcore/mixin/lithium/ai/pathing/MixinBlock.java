package gribland.gribcore.mixin.lithium.ai.pathing;

import gribland.gribcore.lithium.api.BlockPathingBehavior;
import gribland.gribcore.lithium.common.ai.pathing.PathNodeDefaults;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class MixinBlock implements BlockPathingBehavior {
    @Override
    public BlockPathTypes getPathNodeType(BlockState state) {
        return PathNodeDefaults.getNodeType(state);
    }

    @Override
    public BlockPathTypes getPathNodeTypeAsNeighbor(BlockState state) {
        return PathNodeDefaults.getNeighborNodeType(state);
    }
}

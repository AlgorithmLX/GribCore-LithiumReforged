package gribland.gribcore.lithium.api;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public interface BlockPathingBehavior {

    BlockPathTypes getPathNodeType(BlockState state);

    BlockPathTypes getPathNodeTypeAsNeighbor(BlockState state);
}
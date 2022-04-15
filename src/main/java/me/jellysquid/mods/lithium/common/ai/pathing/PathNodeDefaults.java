package me.jellysquid.mods.lithium.common.ai.pathing;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;

public class PathNodeDefaults {
    public static PathNodeType getNeighborNodeType(BlockState state) {
        if (state.isAir()) {
            return PathNodeType.OPEN;
        }

        // [VanillaCopy] LandPathNodeMaker#getNodeTypeFromNeighbors
        // Determine what kind of obstacle type this neighbor is
        if (state.is(Blocks.CACTUS)) {
            return PathNodeType.DANGER_CACTUS;
        } else if (state.is(Blocks.SWEET_BERRY_BUSH)) {
            return PathNodeType.DANGER_OTHER;
        } else if (isFireDangerSource(state)) {
            return PathNodeType.DANGER_FIRE;
        } else if (state.getFluidState().is(FluidTags.WATER)) {
            return PathNodeType.WATER_BORDER;
        } else {
            return PathNodeType.OPEN;
        }
    }

    public static PathNodeType getNodeType(BlockState state) {
        if (state.isAir()) {
            return PathNodeType.OPEN;
        }

        Block block = state.getBlock();
        Material material = state.getMaterial();

        if (state.is(BlockTags.TRAPDOORS) || state.is(Blocks.LILY_PAD)) {
            return PathNodeType.TRAPDOOR;
        }

        if (state.is(Blocks.CACTUS)) {
            return PathNodeType.DAMAGE_CACTUS;
        }

        if (state.is(Blocks.SWEET_BERRY_BUSH)) {
            return PathNodeType.DAMAGE_OTHER;
        }

        if (state.is(Blocks.HONEY_BLOCK)) {
            return PathNodeType.STICKY_HONEY;
        }

        if (state.is(Blocks.COCOA)) {
            return PathNodeType.COCOA;
        }

        // Retrieve the fluid state from the block state to avoid a second lookup
        FluidState fluidState = state.getFluidState();
        if (fluidState.is(FluidTags.WATER)) {
            return PathNodeType.WATER;
        } else if (fluidState.is(FluidTags.LAVA)) {
            return PathNodeType.LAVA;
        }

        if (isFireDangerSource(state)) {
            return PathNodeType.DAMAGE_FIRE;
        }

        if (DoorBlock.isWoodenDoor(state) && !state.hasProperty(DoorBlock.OPEN)) {
            return PathNodeType.DOOR_WOOD_CLOSED;
        }

        if ((block instanceof DoorBlock) && (material == Material.METAL) && !state.hasProperty(DoorBlock.OPEN)) {
            return PathNodeType.DOOR_IRON_CLOSED;
        }

        if ((block instanceof DoorBlock) && state.hasProperty(DoorBlock.OPEN)) {
            return PathNodeType.DOOR_OPEN;
        }

        if (block instanceof AbstractRailBlock) {
            return PathNodeType.RAIL;
        }

        if (block instanceof LeavesBlock) {
            return PathNodeType.LEAVES;
        }

        if (block.is(BlockTags.FENCES) || block.is(BlockTags.WALLS) || ((block instanceof FenceGateBlock) && !state.hasProperty(FenceGateBlock.OPEN))) {
            return PathNodeType.FENCE;
        }

        return PathNodeType.OPEN;
    }

    private static boolean isFireDangerSource(BlockState blockState) {
        return blockState.is(BlockTags.FIRE) || blockState.is(Blocks.LAVA) || blockState.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(blockState);
    }
}

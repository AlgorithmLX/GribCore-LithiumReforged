package gribland.gribcore.lithium.common.shapes;

import net.minecraft.world.phys.AABB;

public interface VoxelShapeCaster {
    /**
     * Checks whether an entity's bounding box collides with this shape translated to the given coordinates.
     *
     * @param box The entity's bounding box
     * @param x   The x-coordinate of this shape
     * @param y   The y-coordinate of this shape
     * @param z   The z-coordinate of this shape
     * @return True if the box intersects with this shape, otherwise false
     */
    boolean intersects(AABB box, double x, double y, double z);
}

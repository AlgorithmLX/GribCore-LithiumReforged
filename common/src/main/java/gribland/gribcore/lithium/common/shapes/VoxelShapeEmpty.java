package gribland.gribcore.lithium.common.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeEmpty extends VoxelShape implements VoxelShapeCaster {
    private static final DoubleList EMPTY_LIST = DoubleArrayList.wrap(new double[]{0.0D});

    public VoxelShapeEmpty(DiscreteVoxelShape voxels) {
        super(voxels);
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return EMPTY_LIST;
    }

    @Override
    protected boolean isFullWide(double x, double y, double z) {
        return false;
    }

    @Override
    public double min(Direction.Axis axis) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double max(Direction.Axis axis) {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean intersects(AABB box, double x, double y, double z) {
        return false;
    }
}

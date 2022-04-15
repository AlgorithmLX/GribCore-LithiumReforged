package me.jellysquid.mods.lithium.common.shapes;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShapePart;

public class CuboidVoxelSet extends VoxelShapePart {
    private final int minX, minY, minZ, maxX, maxY, maxZ;

    protected CuboidVoxelSet(int xSize, int ySize, int zSize, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        super(xSize, ySize, zSize);

        this.minX = (int) Math.round(minX * xSize);
        this.maxX = (int) Math.round(maxX * xSize);
        this.minY = (int) Math.round(minY * ySize);
        this.maxY = (int) Math.round(maxY * ySize);
        this.minZ = (int) Math.round(minZ * zSize);
        this.maxZ = (int) Math.round(maxZ * zSize);
    }

    @Override
    public boolean contains(int x, int y, int z) {
        return x >= this.minX && x < this.maxX &&
                y >= this.minY && y < this.maxY &&
                z >= this.minZ && z < this.maxZ;
    }

    @Override
    public void set(int x, int y, int z, boolean resize, boolean included) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMax(Direction.Axis axis) {
        return axis.choose(this.maxX, this.maxY, this.maxZ);
    }


    @Override
    public boolean isFull(int p_197835_1_, int p_197835_2_, int p_197835_3_) {
        return false;
    }

    @Override
    public void setFull(int p_199625_1_, int p_199625_2_, int p_199625_3_, boolean p_199625_4_, boolean p_199625_5_) {

    }

    @Override
    public boolean isEmpty() {
        return this.minX >= this.maxX || this.minY >= this.maxY || this.minZ >= this.maxZ;
    }

    @Override
    public int firstFull(Direction.Axis axis) {
        return axis.choose(this.minX, this.minY, this.minZ);
    }

    @Override
    public int lastFull(Direction.Axis axis) {
        return 0;
    }

    @Override
    protected boolean isColumnFull(int minZ, int maxZ, int x, int y) {
        return x >= this.minX && x < this.maxX &&
                y >= this.minY && y < this.maxY &&
                minZ >= this.minZ && maxZ <= this.maxZ; // arg maxZ is exclusive
    }

    @Override
    protected void setColumn(int minZ, int maxZ, int x, int y, boolean included) {
        throw new UnsupportedOperationException();
    }
}

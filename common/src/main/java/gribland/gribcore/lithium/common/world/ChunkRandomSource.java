package gribland.gribcore.lithium.common.world;

import net.minecraft.core.BlockPos;
public interface ChunkRandomSource {

    void getRandomPosInChunk(int x, int y, int z, int mask, BlockPos.MutableBlockPos out);
}

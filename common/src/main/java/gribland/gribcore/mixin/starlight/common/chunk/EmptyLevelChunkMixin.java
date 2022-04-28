package gribland.gribcore.mixin.starlight.common.chunk;

import gribland.gribcore.starlight.chunk.ExtendedChunk;
import gribland.gribcore.starlight.light.SWMRNibbleArray;
import gribland.gribcore.starlight.light.StarLightEngine;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyLevelChunk.class)
public abstract class EmptyLevelChunkMixin extends LevelChunk implements ExtendedChunk {

    public EmptyLevelChunkMixin(Level level, ProtoChunk protoChunk) {
        super(level, protoChunk);
    }

    @Override
    public SWMRNibbleArray[] getBlockNibbles() {
        return StarLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void setBlockNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public SWMRNibbleArray[] getSkyNibbles() {
        return StarLightEngine.getFilledEmptyLight(this.getLevel());
    }

    @Override
    public void setSkyNibbles(final SWMRNibbleArray[] nibbles) {}

    @Override
    public boolean[] getSkyEmptinessMap() {
        return null;
    }

    @Override
    public void setSkyEmptinessMap(final boolean[] emptinessMap) {}

    @Override
    public boolean[] getBlockEmptinessMap() {
        return null;
    }

    @Override
    public void setBlockEmptinessMap(final boolean[] emptinessMap) {}
}

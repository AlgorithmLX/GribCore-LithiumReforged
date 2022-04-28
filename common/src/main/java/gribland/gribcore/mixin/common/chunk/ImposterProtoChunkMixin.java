package gribland.gribcore.mixin.common.chunk;

import gribland.gribcore.starlight.chunk.ExtendedChunk;
import gribland.gribcore.starlight.light.SWMRNibbleArray;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ImposterProtoChunk.class)
public abstract class ImposterProtoChunkMixin extends ProtoChunk implements ExtendedChunk {

    @Final
    @Shadow
    private LevelChunk wrapped;

    public ImposterProtoChunkMixin(final LevelChunk levelChunk, final ProtoTickList<Block> biome, final ProtoTickList<Fluid> biome1, final boolean bl) {
        super(levelChunk.getPos(), UpgradeData.EMPTY, levelChunk.getSections(), biome, biome1);
    }

    @Override
    public SWMRNibbleArray[] getBlockNibbles() {
        return ((ExtendedChunk)this.wrapped).getBlockNibbles();
    }

    @Override
    public void setBlockNibbles(final SWMRNibbleArray[] nibbles) {
        ((ExtendedChunk)this.wrapped).setBlockNibbles(nibbles);
    }

    @Override
    public SWMRNibbleArray[] getSkyNibbles() {
        return ((ExtendedChunk)this.wrapped).getSkyNibbles();
    }

    @Override
    public void setSkyNibbles(final SWMRNibbleArray[] nibbles) {
        ((ExtendedChunk)this.wrapped).setSkyNibbles(nibbles);
    }

    @Override
    public boolean[] getSkyEmptinessMap() {
        return ((ExtendedChunk)this.wrapped).getSkyEmptinessMap();
    }

    @Override
    public void setSkyEmptinessMap(final boolean[] emptinessMap) {
        ((ExtendedChunk)this.wrapped).setSkyEmptinessMap(emptinessMap);
    }

    @Override
    public boolean[] getBlockEmptinessMap() {
        return ((ExtendedChunk)this.wrapped).getBlockEmptinessMap();
    }

    @Override
    public void setBlockEmptinessMap(final boolean[] emptinessMap) {
        ((ExtendedChunk)this.wrapped).setBlockEmptinessMap(emptinessMap);
    }
}

package gribland.gribcore.mixin.starlight.common.chunk;

import gribland.gribcore.starlight.chunk.ExtendedChunk;
import gribland.gribcore.starlight.light.StarLightEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ALL")
@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements ExtendedChunk {

    /**
     * Copies the nibble data from the protochunk.
     * TODO since this is a constructor inject, check for new constructors on update.
     */
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/chunk/ProtoChunk;)V",
            at = @At("TAIL")
    )
    public void onTransitionToFull(ServerLevel serverLevel, ProtoChunk protoChunk, LevelChunk.EntityCreationType postLoadProcessor, CallbackInfo ci) {
        this.setBlockNibbles(((ExtendedChunk)protoChunk).getBlockNibbles());
        this.setSkyNibbles(((ExtendedChunk)protoChunk).getSkyNibbles());
        this.setSkyEmptinessMap(((ExtendedChunk)protoChunk).getSkyEmptinessMap());
        this.setBlockEmptinessMap(((ExtendedChunk)protoChunk).getBlockEmptinessMap());
    }

    /**
     * Initialises the nibble arrays to default values.
     * TODO since this is a constructor inject, check for new constructors on update.
     */
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/level/TickList;Lnet/minecraft/world/level/TickList;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;)V",
            at = @At("TAIL")
    )
    public void onConstruct(Level level, ChunkPos chunkPos, UpgradeData upgradeData, TickList levelChunkTicks, TickList levelChunkTicks2, long l, LevelChunkSection[] levelChunkSections, LevelChunk.EntityCreationType postLoadProcessor, LevelChunkSection blendingData, CallbackInfo ci) {
        this.setBlockNibbles(StarLightEngine.getFilledEmptyLight(level));
        this.setSkyNibbles(StarLightEngine.getFilledEmptyLight(level));
    }
}

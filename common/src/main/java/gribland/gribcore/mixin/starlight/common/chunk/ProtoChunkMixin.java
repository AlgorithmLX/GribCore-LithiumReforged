package gribland.gribcore.mixin.starlight.common.chunk;

import gribland.gribcore.starlight.chunk.ExtendedChunk;
import gribland.gribcore.starlight.light.StarLightEngine;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ALL")
@Mixin(ProtoChunk.class)
public abstract class ProtoChunkMixin implements ExtendedChunk {

    /**
     * Initialises the nibble arrays to default values.
     * TODO since this is a constructor inject, check for new constructors on update.
     */
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/ProtoTickList;Lnet/minecraft/world/level/chunk/ProtoTickList;)V",
            at = @At("TAIL")
    )
    public void onConstruct(ChunkPos chunkPos, UpgradeData upgradeData, LevelChunkSection[] levelChunkSections, ProtoTickList protoChunkTicks, ProtoTickList protoChunkTicks2, Level levelHeightAccessor, Registry registry, CallbackInfo ci) {
        if ((Object)this instanceof ImposterProtoChunk) {
            return;
        }
        this.setBlockNibbles(StarLightEngine.getFilledEmptyLight(levelHeightAccessor));
        this.setSkyNibbles(StarLightEngine.getFilledEmptyLight(levelHeightAccessor));
    }
}

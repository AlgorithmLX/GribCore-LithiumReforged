package gribland.gribcore.mixin.lithium.world.block_entity_ticking.collections;

import gribland.gribcore.lithium.common.util.collections.BlockEntityList;
import gribland.gribcore.lithium.common.util.collections.HashedReferenceList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@Mixin(Level.class)
public abstract class WorldMixin implements LevelAccessor {
    @Mutable
    @Shadow
    protected List<BlockEntity> updatingBlockEntities;
    @Shadow
    @Final
    public boolean isClientSide;
    @Mutable
    @Shadow
    @Final
    public List<BlockEntity> blockEntityList;

    @Shadow
    @Final
    public List<BlockEntity> tickableBlockEntities;

    @Mutable
    @Shadow
    @Final
    protected List<BlockEntity> pendingBlockEntities;

    @Shadow
    @Final
    private Supplier<ProfilerFiller> profiler;

    private BlockEntityList blockEntities$lithium;
    private BlockEntityList pendingBlockEntities$lithium;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void reinit(WritableLevelData properties, ResourceKey<Level> registryKey, DimensionType dimensionType,
                        Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, CallbackInfo ci) {
        // Replace the fallback collections with our types as well
        // This won't guarantee mod compatibility, but at least it should fail loudly when it does
        this.blockEntities$lithium = new BlockEntityList(this.blockEntityList, false);
        this.blockEntityList = this.blockEntities$lithium;

        this.updatingBlockEntities = new HashedReferenceList<>(this.updatingBlockEntities);

        this.pendingBlockEntities$lithium = new BlockEntityList(this.pendingBlockEntities, true);
        this.pendingBlockEntities = this.pendingBlockEntities$lithium;
    }

    /**
     * @author JellySquid
     * @reason Replace with direct lookup
     */
    @SuppressWarnings("OverwriteModifiers")
    @Overwrite
    private BlockEntity getPendingBlockEntityAt(BlockPos pos) {
        return this.pendingBlockEntities$lithium.getFirstNonRemovedBlockEntityAtPosition(pos.asLong());
    }

    // We do not want the vanilla code for adding pending block entities to be ran. We'll inject later in
    // postBlockEntityTick to use our optimized implementation.
    @Redirect(
            method = "tickBlockEntities",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/level/Level;updatingBlockEntities:Z",
                    ordinal = 0
            )
    )
    private List<?> nullifyPendingBlockEntityListDuringTick(Level world) {
        return Collections.emptyList();
    }

    // Add any pending block entities to the world.
    @Inject(method = "tickBlockEntities", at = @At("RETURN"))
    private void postBlockEntityTick(CallbackInfo ci) {
        ProfilerFiller profiler = this.profiler.get();
        profiler.push("pendingBlockEntities$lithium");

        // The usage of a for-index loop is invalid with our optimized implementation, so use an iterator here
        // The overhead of this is essentially non-zero and doesn't matter in this code.
        for (BlockEntity entity : this.pendingBlockEntities) {
            if (entity.isRemoved()) {
                continue;
            }

            // Try-add directly to avoid the double map lookup, helps speed things along
            if (this.blockEntities$lithium.addIfAbsent(entity)) {
                //vanilla has an extra updateListeners(...) call on the client here, but the one below should be enough
                if (entity instanceof TickableBlockEntity) {
                    this.tickableBlockEntities.add(entity);
                }

                BlockPos pos = entity.getBlockPos();

                // Avoid the double chunk lookup (isLoaded followed by getChunk) by simply inlining getChunk call
                // pass this.isClient instead of false, so the updateListeners call is always executed on the client (like vanilla)
                ChunkAccess chunk = this.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, this.isClientSide);

                if (chunk != null) {
                    BlockState state = chunk.getBlockState(pos);
                    chunk.setBlockEntity(pos, entity);

                    this.sendBlockUpdated(pos, state, state, 3);
                }
            }
        }

        this.pendingBlockEntities.clear();

        profiler.pop();
    }

    // We don't want this code wasting a ton of CPU time trying to scan through our optimized collection
    // Instead, we simply run the code on those at the same position directly
    @Redirect(
            method = "setBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/BlockEntity;setLevelAndPosition(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            )
    )
    private void setLocationAndRemoveAllAtPosition(BlockEntity blockEntity, Level world, BlockPos pos) {
        blockEntity.setLevelAndPosition(world, pos);
        this.pendingBlockEntities$lithium.markRemovedAndRemoveAllAtPosition(pos);
    }

    @Redirect(
            method = "setBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
            )
    )
    private <E> Iterator<E> nullifyBlockEntityScanDuringSetBlockEntity(List<E> list) {
        return Collections.emptyIterator();
    }

    @Shadow
    public abstract void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags);
}

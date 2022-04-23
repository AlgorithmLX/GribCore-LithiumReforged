package gribland.gribcore.mixin.lithium.ai.nearby_entity_tracking;

import gribland.gribcore.entity.tracker.EntityTrackerEngine;
import gribland.gribcore.entity.tracker.EntityTrackerEngineProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Level.class)
public class WorldMixin implements EntityTrackerEngineProvider {
    private EntityTrackerEngine tracker;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(WritableLevelData properties, ResourceKey<Level> registryKey, final DimensionType dimensionType, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, CallbackInfo ci) {
        this.tracker = new EntityTrackerEngine();
    }

    @Override
    public EntityTrackerEngine gribcore$getEntityTracker() {
        return this.tracker;
    }
}

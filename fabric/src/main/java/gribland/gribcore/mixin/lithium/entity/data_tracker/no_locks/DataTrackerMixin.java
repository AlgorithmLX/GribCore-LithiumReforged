package gribland.gribcore.mixin.lithium.entity.data_tracker.no_locks;

import gribland.gribcore.lithium.common.util.lock.NullReadWriteLock;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.locks.ReadWriteLock;

@Mixin(value = SynchedEntityData.class, priority = 1001)
public abstract class DataTrackerMixin {
    @Mutable
    @Shadow
    @Final
    private ReadWriteLock lock;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Entity entity, CallbackInfo ci) {
        this.lock = new NullReadWriteLock();
    }
}

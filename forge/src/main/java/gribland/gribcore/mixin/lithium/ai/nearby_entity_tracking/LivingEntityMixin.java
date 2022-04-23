package gribland.gribcore.mixin.lithium.ai.nearby_entity_tracking;

import gribland.gribcore.entity.tracker.nearby.NearbyEntityListenerMulti;
import gribland.gribcore.entity.tracker.nearby.NearbyEntityListenerProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Extends the base living entity class to provide a {@link NearbyEntityListenerMulti} which will handle the
 * child {@link NearbyEntityListenerProvider}s of AI tasks attached to this entity.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin implements NearbyEntityListenerProvider {
    private NearbyEntityListenerMulti tracker;

    /**
     * Initialize the entity listener.
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityType<? extends LivingEntity> type, Level world, CallbackInfo ci) {
        this.tracker = new NearbyEntityListenerMulti();
    }

    @Override
    public NearbyEntityListenerMulti gribcore$getListener() {
        return this.tracker;
    }
}

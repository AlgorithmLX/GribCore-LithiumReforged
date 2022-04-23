package gribland.gribcore.mixin.lithium.ai.nearby_entity_tracking.goals;

import gribland.gribcore.entity.tracker.nearby.NearbyEntityListenerProvider;
import gribland.gribcore.entity.tracker.nearby.NearbyEntityTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(AvoidEntityGoal.class)
public class FleeEntityGoalMixin<T extends LivingEntity> {
    private NearbyEntityTracker<T> tracker;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/PathfinderMob;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V",
            at = @At("RETURN"))
    private void init(PathfinderMob mob, Class<T> fleeFromType, Predicate<LivingEntity> predicate, float distance, double slowSpeed, double fastSpeed, Predicate<LivingEntity> predicate2, CallbackInfo ci) {
        this.tracker = new NearbyEntityTracker<>(fleeFromType, mob, distance);

        ((NearbyEntityListenerProvider) mob).gribcore$getListener().addListener(this.tracker);
    }
    @Redirect(method = "canUse", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getNearestLoadedEntity(Ljava/lang/Class;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;DDDLnet/minecraft/world/phys/AABB;)Lnet/minecraft/world/entity/LivingEntity;"
    ))
    private T canUseRedirect(Level level, Class<? extends T> entityClass, TargetingConditions targetingConditions, LivingEntity entity, double a, double b, double c, AABB aabb) {
        return this.tracker.getClosestEntity(aabb, targetingConditions);
    }
}

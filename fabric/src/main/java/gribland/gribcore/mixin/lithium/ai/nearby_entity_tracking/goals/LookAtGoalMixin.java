package gribland.gribcore.mixin.lithium.ai.nearby_entity_tracking.goals;

import gribland.gribcore.entity.tracker.nearby.NearbyEntityListenerProvider;
import gribland.gribcore.entity.tracker.nearby.NearbyEntityTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookAtPlayerGoal.class)
public class LookAtGoalMixin {
    private NearbyEntityTracker<? extends LivingEntity> tracker;

    @Inject(method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;FF)V", at = @At("RETURN"))
    private void init(Mob mob, Class<? extends LivingEntity> targetType, float range, float chance, CallbackInfo ci) {
        this.tracker = new NearbyEntityTracker<>(targetType, mob, range);

        ((NearbyEntityListenerProvider) mob).gribcore$getListener().addListener(this.tracker);
    }

    @Redirect(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getNearestLoadedEntity(Ljava/lang/Class;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;DDDLnet/minecraft/world/phys/AABB;)Lnet/minecraft/world/entity/LivingEntity;"
            )
    )
    private <T extends LivingEntity> LivingEntity canUseRedirect(Level instance, Class<? extends T> aClass, TargetingConditions targetingConditions, LivingEntity entity, double x, double y, double z, AABB aabb) {
        return this.tracker.getClosestEntity(aabb, targetingConditions);
    }

    @Redirect(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getNearestPlayer(Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;DDD)Lnet/minecraft/world/entity/player/Player;"
            )
    )
    private Player canUseRedirect(Level world, TargetingConditions targetPredicate, LivingEntity entity, double x, double y, double z) {
        return (Player) this.tracker.getClosestEntity(null, targetPredicate);
    }
}

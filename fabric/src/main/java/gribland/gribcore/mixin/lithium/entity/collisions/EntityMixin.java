package gribland.gribcore.mixin.lithium.entity.collisions;

import gribland.gribcore.entity.LithiumEntityCollisions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Stream;

/**
 * Replaces collision testing methods against the world border with faster checks.
 */
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract AABB getBoundingBox();

    @Shadow public Level level;

    @Redirect(
            method = "collide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/shapes/Shapes;joinIsNotEmpty(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/phys/shapes/BooleanOp;)Z"
            )
    )
    private boolean skipWorldBorderMatchesAnywhere(VoxelShape borderShape, VoxelShape entityShape, BooleanOp func, Vec3 motion) {
        return false;
    }

    /**
     * Skip creation of unused cuboid shape
     */
    @Redirect(
            method = "collide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;",
                    ordinal = 0
            )
    )
    private AABB skipCuboid(Entity instance) {
        return null;
    }

    /**
     * Skip creation of unused stream
     */
    @Redirect(
            method = "collide",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;empty()Ljava/util/stream/Stream;",
                    ordinal = 0
            )
    )
    private Stream<VoxelShape> skipStream() {
        return null;
    }

    /**
     * Uses a very quick check to determine if the player is outside the world border (which would disable collisions
     * against it). We also perform an additional check to see if the player can even collide with the world border in
     * this physics step, allowing us to remove it from collision testing in later code.
     *
     * @return The combined entity shapes and worldborder stream, or if the worldborder cannot be collided with the entity stream will be returned.
     */
    @Redirect(
            method = "collide",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;concat(Ljava/util/stream/Stream;Ljava/util/stream/Stream;)Ljava/util/stream/Stream;"
            )
    )
    private Stream<VoxelShape> fastWorldBorderTest(Stream<VoxelShape> entityShapes, Stream<VoxelShape> emptyStream, Vec3 motion) {
        if (LithiumEntityCollisions.isWithinWorldBorder(level.getWorldBorder(), getBoundingBox().expandTowards(motion)) ||
                !LithiumEntityCollisions.isWithinWorldBorder(level.getWorldBorder(), getBoundingBox().inflate(1.0E-7D))) {
            return entityShapes;
        }
        //the world border shape will only be collided with, if the entity is colliding with the world border after movement but is not
        //colliding with the world border already
        return Stream.concat(entityShapes, Stream.of(this.level.getWorldBorder().getCollisionShape()));
    }
}

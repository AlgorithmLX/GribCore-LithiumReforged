package gribland.gribcore.mixin.lithium.entity.replace_entitytype_predicates;

import gribland.gribcore.world.WorldHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(LlamaFollowCaravanGoal.class)
public class FormCaravanGoalMixin {
    @Redirect(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private List<Entity> getLlamasForCaravan(Level world, Entity excluded, AABB box, Predicate<? super Entity> predicate) {
        return WorldHelper.getEntitiesOfClass(world, excluded, Llama.class, box);
    }
}

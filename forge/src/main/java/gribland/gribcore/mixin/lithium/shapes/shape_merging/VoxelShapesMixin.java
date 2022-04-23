package gribland.gribcore.mixin.lithium.shapes.shape_merging;

import gribland.gribcore.lithium.common.shapes.pairs.LithiumDoublePairList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.world.phys.shapes.IndexMerger;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Shapes.class)
public class VoxelShapesMixin {
    /**
     * Replaces the returned list pair with our own optimized type.
     */
    @Inject(
            method = "createIndexMerger",
            at = @At(
                    shift = At.Shift.BEFORE,
                    value = "NEW",
                    target = "Lnet/minecraft/world/phys/shapes/IdenticalMerger;<init>(Lit/unimi/dsi/fastutil/doubles/DoubleList;)V"
            ),
            cancellable = true
    )
    private static void injectCustomListPair(int size, DoubleList a, DoubleList b, boolean flag1, boolean flag2, CallbackInfoReturnable<IndexMerger> cir) {
        cir.setReturnValue(new LithiumDoublePairList(a, b, flag1, flag2));
    }
}

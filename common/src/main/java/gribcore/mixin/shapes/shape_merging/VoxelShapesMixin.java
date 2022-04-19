package gribcore.mixin.shapes.shape_merging;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import gribland.gribcore.lithium.common.shapes.pairs.LithiumDoublePairList;
import net.minecraft.util.math.shapes.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(VoxelShapes.class)
public class VoxelShapesMixin {
    /**
     * Replaces the returned list pair with our own optimized type.
     */
    @Inject(
            method = "createIndexMerger",
            at = @At(
                    shift = At.Shift.BEFORE,
                    value = "NEW",
                    target = "net/minecraft/util/shape/IndexMerger"
            ),
            cancellable = true
    )
    private static void injectCustomListPair(int size, DoubleList a, DoubleList b, boolean flag1, boolean flag2, CallbackInfoReturnable<PairList> cir) {
        cir.setReturnValue(new LithiumDoublePairList(a, b, flag1, flag2));
    }
}

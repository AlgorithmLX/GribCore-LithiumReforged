package gribland.gribcore.mixin.lithium.gen.biome_noise_cache;

import gribland.gribcore.lithium.common.world.layer.CachedLocalLayerFactory;
import gribland.gribcore.lithium.common.world.layer.CloneableContext;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Memoize the LayerFactory and make it produce thread-local copies for thread-safety purposes
 */
@Mixin(AreaTransformer2.class)
public interface MergingLayerMixin extends AreaTransformer2 {
    /**
     * @reason Replace with a memoized and thread-local layer factory
     * @author gegy1000
     */
    @Overwrite
    @SuppressWarnings("unchecked")
    default <R extends Area> AreaFactory<R> run(BigContext<R> bigContext, AreaFactory<R> areaFactory, AreaFactory<R> areaFactory2) {
        return CachedLocalLayerFactory.createMerging(this, (CloneableContext<R>) bigContext, areaFactory, areaFactory2);
    }
}
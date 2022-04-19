package gribcore.mixin.gen.biome_noise_cache;

import gribland.gribcore.lithium.common.world.layer.CachedLocalLayerFactory;
import gribland.gribcore.lithium.common.world.layer.CloneableContext;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Memoize the LayerFactory and make it produce thread-local copies for thread-safety purposes
 */
@Mixin(ParentedLayer.class)
public interface ParentedLayerMixin extends ParentedLayer {
    /**
     * @reason Replace with a memoized and thread-local layer factory
     * @author gegy1000
     */
    @Overwrite
    @SuppressWarnings("unchecked")
    default <R extends LayerSampler> LayerFactory<R> create(LayerSampleContext<R> context, LayerFactory<R> parent) {
        return CachedLocalLayerFactory.createParented(this, (CloneableContext<R>) context, parent);
    }
}
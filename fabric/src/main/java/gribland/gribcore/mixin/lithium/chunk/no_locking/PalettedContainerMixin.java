package gribland.gribcore.mixin.lithium.chunk.no_locking;

import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;


@Mixin(PalettedContainer.class)
public class PalettedContainerMixin {
    /**
     * @reason Do not check the container's lock
     * @author JellySquid
     */
    @Overwrite
    public void acquire() {

    }

    /**
     * @reason Do not check the container's lock
     * @author JellySquid
     */
    @Overwrite
    public void release() {

    }
}

package gribland.gribcore.mixin.common.world;

import gribland.gribcore.starlight.world.ExtendedWorld;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelAccessor, AutoCloseable, ExtendedWorld {
    //Здесь должен быть код, но его съел Зейт
}

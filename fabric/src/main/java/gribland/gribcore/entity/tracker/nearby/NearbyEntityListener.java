package gribland.gribcore.entity.tracker.nearby;

import gribland.gribcore.entity.tracker.EntityTrackerEngine;
import net.minecraft.world.entity.LivingEntity;

/**
 * The main interface used to receive events from the
 * {@link EntityTrackerEngine} of a world.
 */
public interface NearbyEntityListener {
    /**
     * Returns the range (in chunks) of this listener. This must never change during the lifetime of the listener.
     * TODO: Allow entity listeners to change the radius they receive updates within
     */
    int getChunkRange();

    /**
     * Called by the entity tracker when an entity enters the range of this listener.
     */
    void onEntityEnteredRange(LivingEntity entity);

    /**
     * Called by the entity tracker when an entity leaves the range of this listener or is removed from the world.
     */
    void onEntityLeftRange(LivingEntity entity);
}

package gribland.gribcore.entity.tracker;

public interface EntityTrackerEngineProvider {
    EntityTrackerEngine gribcore$getEntityTracker();

    static EntityTrackerEngine gribcore$getEntityTracker(Object world) {
        return world instanceof EntityTrackerEngineProvider ? ((EntityTrackerEngineProvider) world).gribcore$getEntityTracker() : null;
    }
}

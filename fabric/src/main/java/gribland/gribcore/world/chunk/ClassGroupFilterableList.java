package gribland.gribcore.world.chunk;

import gribland.gribcore.entity.EntityClassGroup;

import java.util.Collection;

public interface ClassGroupFilterableList<T> {
    Collection<T> getAllOfGroupType(EntityClassGroup type);

}
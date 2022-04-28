package gribland.gribcore.starlight.blockstate;

public interface ExtendedAbstractBlockState {
    public boolean isConditionallyFullOpaque();
    public int getOpacityIfCached();
}

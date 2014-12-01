package fr.mcnanotech.privatizer.common;

public interface IDoorTile extends IDirectionalTile
{
    public boolean isDoubleDoor();
    
    public boolean isOpen();

    public void setDoubleDoor(boolean doubleDoor);
}
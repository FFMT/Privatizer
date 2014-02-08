package fr.mcnanotech.privatizer.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrivate extends TileEntity
{
	private String owner;
	
    public void readFromNBT(NBTTagCompound nbtTag)
    {
    	this.owner = nbtTag.getString("owner");
    }

    public void writeToNBT(NBTTagCompound nbtTag)
    {
    	nbtTag.setString("owner", this.owner);
    }
	
	public void setOwner(String str)
	{
		this.owner = str;
	}
	
	public String getOwner()
	{
		return this.owner;
	}
}
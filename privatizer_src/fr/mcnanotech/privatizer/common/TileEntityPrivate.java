package fr.mcnanotech.privatizer.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPrivate extends TileEntity
{
	private String owner;

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		this.owner = nbtTag.getString("owner");
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);
		if(this.owner != null && !this.owner.isEmpty())
		{
			nbtTag.setString("owner", this.owner);
		}
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
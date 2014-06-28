package fr.mcnanotech.privatizer.common;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityPrivate extends TileEntity
{
	private UUID owner;

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		if(nbtTag.hasKey("UUIDMost", Constants.NBT.TAG_LONG) && nbtTag.hasKey("UUIDLeast", Constants.NBT.TAG_LONG))
		{
			this.owner = new UUID(nbtTag.getLong("UUIDMost"), nbtTag.getLong("UUIDLeast"));
		}
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);
		if(this.owner != null)
		{
			nbtTag.setLong("UUIDMost", this.owner.getMostSignificantBits());
			nbtTag.setLong("UUIDLeast", this.owner.getLeastSignificantBits());
		}
	}

	public void setOwner(UUID id)
	{
		this.owner = id;
	}

	public UUID getOwner()
	{
		return this.owner;
	}
}
package fr.mcnanotech.privatizer.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPassword extends TileEntity
{
	private String password;

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		this.password = nbtTag.getString("password");
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);
		nbtTag.setString("password", this.password);
	}

	public void setPassword(String str)
	{
		this.password = str;
	}

	public String getPassword()
	{
		return this.password;
	}
}
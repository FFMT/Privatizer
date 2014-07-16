package fr.mcnanotech.privatizer.common;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityPrivate extends TileEntity
{
	private GameProfile owner;

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		this.owner = NBTUtil.func_152459_a(nbtTag);
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);
		NBTUtil.func_152460_a(nbtTag, this.owner);
	}

	public void setOwner(GameProfile profile)
	{
		this.owner = profile;
	}

	public GameProfile getOwner()
	{
		return this.owner;
	}
}
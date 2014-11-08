package fr.mcnanotech.privatizer.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;

import com.mojang.authlib.GameProfile;

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
    
    public boolean canUpdate()
    {
        return false;
    }
}
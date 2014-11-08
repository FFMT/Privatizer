package fr.mcnanotech.privatizer.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFriend extends TileEntity
{
    private String friend;

    public void readFromNBT(NBTTagCompound nbtTag)
    {
        super.readFromNBT(nbtTag);
        this.friend = nbtTag.getString("friend");
    }

    public void writeToNBT(NBTTagCompound nbtTag)
    {
        super.writeToNBT(nbtTag);
        nbtTag.setString("friend", this.friend);
    }

    public void setOwners(String... str)
    {
        for(String friend : str)
        {
            if(friend == null || friend.isEmpty())
            {
                this.friend = friend;
            }
            else
            {
                this.friend = this.friend + "|" + friend;
            }
        }
    }

    public String[] getOwners()
    {
        return this.friend.split("|");
    }
    
    public boolean canUpdate()
    {
        return false;
    }
}
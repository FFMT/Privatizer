package fr.mcnanotech.privatizer.common;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityPrivateDoor extends TileEntityPrivate
{
	private int direction;
	private boolean open, doubleDoor;

	public void updateEntity()
	{
		final IEntitySelector livingFilter = new PlayerFilter();
		List entityTagetList = worldObj.selectEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord, this.zCoord - 1, this.xCoord + 2, this.yCoord + 1, this.zCoord + 2), livingFilter);
		if(this.isOpen() && entityTagetList.size() == 0)
		{
			this.setOpen(false);
		}
		for(int i = 0; i < entityTagetList.size(); i++)
		{
			EntityPlayer player = (EntityPlayer)entityTagetList.get(i);
			if(!this.isOpen() && player.getGameProfile().equals(this.getOwner()))
			{
				this.setOpen(true);
			}
			else if(this.isOpen() && !player.getGameProfile().equals(this.getOwner()))
			{
				this.setOpen(false);
			}
		}
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.direction = compound.getInteger("direction");
		this.open = compound.getBoolean("open");
		this.doubleDoor = compound.getBoolean("double");
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("direction", this.direction);
		compound.setBoolean("open", this.open);
		compound.setBoolean("double", this.doubleDoor);
	}

	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public boolean isOpen()
	{
		return open;
	}

	public void setOpen(boolean open)
	{
		this.open = open;
		if(!this.worldObj.isRemote)
		{
			this.worldObj.playAuxSFXAtEntity(null, 1003, xCoord, yCoord, zCoord, 0);
		}
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord + 1, this.zCoord);
	}
	
	public boolean isDoubleDoor()
	{
		return doubleDoor;
	}

	public void setDoubleDoor(boolean doubleDoor)
	{
		this.doubleDoor = doubleDoor;
	}

	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.func_148857_g());
	}
}
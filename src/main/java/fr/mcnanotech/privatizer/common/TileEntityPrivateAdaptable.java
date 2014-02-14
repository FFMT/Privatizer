package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityPrivateAdaptable extends TileEntityPrivate
{
	private ItemStack[] currentStack = new ItemStack[4];

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		NBTTagList nbttaglist = nbtTag.getTagList("Items", 10);
		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if(j >= 0 && j < this.currentStack.length)
			{
				this.currentStack[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);

		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < this.currentStack.length; ++i)
		{
			if(this.currentStack[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.currentStack[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTag.setTag("Items", nbttaglist);
	}

	public Block getBlockForTexture()
	{
		if(currentStack[0] != null && currentStack[0].getItem() != null && Block.getBlockFromItem(currentStack[0].getItem()) != null)
		{
			return Block.getBlockFromItem(currentStack[0].getItem());
		}
		return null;
	}

	public int getBlockMetadataForTexture()
	{
		if(currentStack[0] != null)
		{
			return currentStack[0].getItemDamage();
		}
		return 0;
	}

	public void setStack(ItemStack stack)
	{
		this.currentStack[0] = stack;
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
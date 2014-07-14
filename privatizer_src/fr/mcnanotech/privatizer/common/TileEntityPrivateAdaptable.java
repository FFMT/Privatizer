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
	private ItemStack stack;

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		NBTTagList nbttaglist = nbtTag.getTagList("Items", 10);
		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			this.stack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);

		NBTTagList nbttaglist = new NBTTagList();
		if(this.stack != null)
		{
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("Slot", (byte)0);
			this.stack.writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}
		nbtTag.setTag("Items", nbttaglist);
	}

	public Block getBlockForTexture()
	{
		if(stack != null && stack.getItem() != null && Block.getBlockFromItem(stack.getItem()) != null)
		{
			return Block.getBlockFromItem(stack.getItem());
		}
		return null;
	}

	public int getBlockMetadataForTexture()
	{
		if(stack != null)
		{
			return stack.getItemDamage();
		}
		return 0;
	}

	public void setStack(ItemStack stack)
	{
		this.stack = stack;
	}

	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbttagcompound);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.func_148857_g());
		this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
	}
}
package fr.mcnanotech.privatizer.common;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

public class TileEntityPrivateChest extends TileEntityPrivate implements IInventory
{
	private ItemStack[] contents = new ItemStack[54];
	public float lidAngle;
	public float prevLidAngle;
	public int numPlayersUsing;
	private int ticksSinceSync;
	private String customName;
	private byte direction;

	public byte getDirection()
	{
		return direction;
	}

	public void setDirection(byte direction)
	{
		this.direction = direction;
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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

	@Override
	public int getSizeInventory()
	{
		return contents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.contents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		if(this.contents[slot] != null)
		{
			ItemStack itemstack;

			if(this.contents[slot].stackSize <= amount)
			{
				itemstack = this.contents[slot];
				this.contents[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.contents[slot].splitStack(amount);

				if(this.contents[slot].stackSize == 0)
				{
					this.contents[slot] = null;
				}
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		if(this.contents[slot] != null)
		{
			ItemStack itemstack = this.contents[slot];
			this.contents[slot] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		this.contents[slot] = stack;

		if(stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.customName : "container.privateChest";
	}

	public void setInventoryName(String name)
	{
		this.customName = name;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return this.customName != null && this.customName.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		if(nbtTag.hasKey("CustomName", Constants.NBT.TAG_STRING))
		{
			this.customName = nbtTag.getString("CustomName");
		}
		this.direction = nbtTag.getByte("Direction");

		NBTTagList nbttaglist = nbtTag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if(j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < this.contents.length; ++i)
		{
			if(this.contents[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.contents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtTag.setTag("Items", nbttaglist);

		if(this.hasCustomInventoryName())
		{
			nbtTag.setString("CustomName", this.customName);
		}
		nbtTag.setByte("Direction", this.direction);
	}

	public void updateEntity()
	{
		super.updateEntity();
		++this.ticksSinceSync;
		float f;

		if(!this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
		{
			this.numPlayersUsing = 0;
			f = 5.0F;
			List list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB((double)((float)this.xCoord - f), (double)((float)this.yCoord - f), (double)((float)this.zCoord - f), (double)((float)(this.xCoord + 1) + f), (double)((float)(this.yCoord + 1) + f), (double)((float)(this.zCoord + 1) + f)));
			Iterator iterator = list.iterator();

			while(iterator.hasNext())
			{
				EntityPlayer entityplayer = (EntityPlayer)iterator.next();

				if(entityplayer.openContainer instanceof ContainerPrivateChest)
				{
					IInventory iinventory = ((ContainerPrivateChest)entityplayer.openContainer).getTile();

					if(iinventory == this)
					{
						++this.numPlayersUsing;
					}
				}
			}
		}

		this.prevLidAngle = this.lidAngle;
		f = 0.1F;
		double d2;

		if(this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
		{
			this.worldObj.playSoundEffect(this.xCoord + 0.5D, (double)this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if(this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
		{
			float f1 = this.lidAngle;

			if(this.numPlayersUsing > 0)
			{
				this.lidAngle += f;
			}
			else
			{
				this.lidAngle -= f;
			}

			if(this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if(this.lidAngle < f2 && f1 >= f2)
			{
				this.worldObj.playSoundEffect(this.xCoord + 0.5D, (double)this.yCoord + 0.5D, this.zCoord + 0.5D, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}

			if(this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}

	public boolean receiveClientEvent(int id, int value)
	{
		if(id == 1)
		{
			this.numPlayersUsing = value;
			return true;
		}
		else
		{
			return super.receiveClientEvent(id, value);
		}
	}

	@Override
	public void openInventory()
	{
		if(this.numPlayersUsing < 0)
		{
			this.numPlayersUsing = 0;
		}

		++this.numPlayersUsing;
		this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
	}

	@Override
	public void closeInventory()
	{
		if(this.getBlockType() instanceof BlockPrivateChest)
		{
			--this.numPlayersUsing;
			this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
		}
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return true;
	}
}
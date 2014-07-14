package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.Constants;

public class TileEntityPrivateFurnace extends TileEntityPrivate implements ISidedInventory
{
	private static final int[] slotsTop = new int[] {0};
	private static final int[] slotsBottom = new int[] {2, 1};
	private static final int[] slotsSides = new int[] {1};

	private ItemStack[] contents = new ItemStack[3];

	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;

	private String customName;
	private byte direction;
	private boolean active;

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
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbttagcompound);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.func_148857_g());
		this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
		this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public int getSizeInventory()
	{
		return this.contents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotId)
	{
		return this.contents[slotId];
	}

	@Override
	public ItemStack decrStackSize(int slotId, int amount)
	{
		if(this.contents[slotId] != null)
		{
			ItemStack itemstack;
			if(this.contents[slotId].stackSize <= amount)
			{
				itemstack = this.contents[slotId];
				this.contents[slotId] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.contents[slotId].splitStack(amount);
				if(this.contents[slotId].stackSize == 0)
				{
					this.contents[slotId] = null;
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
	public ItemStack getStackInSlotOnClosing(int slotId)
	{
		if(this.contents[slotId] != null)
		{
			ItemStack itemstack = this.contents[slotId];
			this.contents[slotId] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slotId, ItemStack stack)
	{
		this.contents[slotId] = stack;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return this.hasCustomInventoryName() ? this.customName : "container.privateFurnace";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return this.customName != null && this.customName.length() > 0;
	}

	public void setInventoryName(String name)
	{
		this.customName = name;
	}

	public void readFromNBT(NBTTagCompound nbtTag)
	{
		super.readFromNBT(nbtTag);
		NBTTagList nbttaglist = nbtTag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.contents = new ItemStack[this.getSizeInventory()];

		for(int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if(b0 >= 0 && b0 < this.contents.length)
			{
				this.contents[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.furnaceBurnTime = nbtTag.getShort("BurnTime");
		this.furnaceCookTime = nbtTag.getShort("CookTime");
		this.currentItemBurnTime = getItemBurnTime(this.contents[1]);

		if(nbtTag.hasKey("CustomName", Constants.NBT.TAG_STRING))
		{
			this.customName = nbtTag.getString("CustomName");
		}

		this.direction = nbtTag.getByte("Direction");
		this.active = nbtTag.getBoolean("Active");
	}

	public void writeToNBT(NBTTagCompound nbtTag)
	{
		super.writeToNBT(nbtTag);
		nbtTag.setShort("BurnTime", (short)this.furnaceBurnTime);
		nbtTag.setShort("CookTime", (short)this.furnaceCookTime);
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
		nbtTag.setBoolean("Active", this.active);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	public int getCookProgressScaled(int i)
	{
		return this.furnaceCookTime * i / 200;
	}

	public int getBurnTimeRemainingScaled(int i)
	{
		if(this.currentItemBurnTime == 0)
		{
			this.currentItemBurnTime = 200;
		}

		return this.furnaceBurnTime * i / this.currentItemBurnTime;
	}

	public boolean isBurning()
	{
		return this.furnaceBurnTime > 0;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public void updateEntity()
	{
		boolean active = this.furnaceBurnTime > 0;
		boolean shouldNotifyUpdate = false;

		if(this.furnaceBurnTime > 0)
		{
			--this.furnaceBurnTime;
		}

		if(!this.worldObj.isRemote)
		{
			if(this.furnaceBurnTime == 0 && this.canSmelt())
			{
				this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.contents[1]);

				if(this.furnaceBurnTime > 0)
				{
					shouldNotifyUpdate = true;

					if(this.contents[1] != null)
					{
						--this.contents[1].stackSize;

						if(this.contents[1].stackSize == 0)
						{
							this.contents[1] = contents[1].getItem().getContainerItem(contents[1]);
						}
					}
				}
			}

			if(this.isBurning() && this.canSmelt())
			{
				++this.furnaceCookTime;

				if(this.furnaceCookTime == 200)
				{
					this.furnaceCookTime = 0;
					this.smeltItem();
					shouldNotifyUpdate = true;
				}
			}
			else
			{
				this.furnaceCookTime = 0;
			}

			if(active != this.furnaceBurnTime > 0)
			{
				shouldNotifyUpdate = true;
				this.active = this.furnaceBurnTime > 0;
				if(!this.worldObj.isRemote)
				{
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				}
			}
		}

		if(shouldNotifyUpdate)
		{
			this.markDirty();
		}
	}

	private boolean canSmelt()
	{
		if(this.contents[0] == null)
		{
			return false;
		}
		else
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.contents[0]);
			if(itemstack == null)
				return false;
			if(this.contents[2] == null)
				return true;
			if(!this.contents[2].isItemEqual(itemstack))
				return false;
			int result = contents[2].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.contents[2].getMaxStackSize();
		}
	}

	public void smeltItem()
	{
		if(this.canSmelt())
		{
			ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.contents[0]);

			if(this.contents[2] == null)
			{
				this.contents[2] = itemstack.copy();
			}
			else if(this.contents[2].getItem() == itemstack.getItem())
			{
				this.contents[2].stackSize += itemstack.stackSize;
			}

			--this.contents[0].stackSize;

			if(this.contents[0].stackSize <= 0)
			{
				this.contents[0] = null;
			}
		}
	}

	public static int getItemBurnTime(ItemStack stack)
	{
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	public static boolean isItemFuel(ItemStack stack)
	{
		return getItemBurnTime(stack) > 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory()
	{

	}

	@Override
	public void closeInventory()
	{

	}

	@Override
	public boolean isItemValidForSlot(int slotId, ItemStack stack)
	{
		return slotId == 2 ? false : (slotId == 1 ? isItemFuel(stack) : true);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? slotsBottom : (side == 1 ? slotsTop : slotsSides);
	}

	@Override
	public boolean canInsertItem(int slotId, ItemStack stack, int side)
	{
		return this.isItemValidForSlot(slotId, stack);
	}

	@Override
	public boolean canExtractItem(int slotId, ItemStack stack, int side)
	{
		return side != 0 || slotId != 1 || stack.getItem() == Items.bucket;
	}
}
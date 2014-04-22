package fr.mcnanotech.privatizer.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerPrivateFurnace extends Container
{
	private TileEntityPrivateFurnace tilePrivateFurnace;
	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerPrivateFurnace(TileEntityPrivateFurnace te, InventoryPlayer inventory)
	{
		this.tilePrivateFurnace = te;
		this.addSlotToContainer(new Slot(this.tilePrivateFurnace, 0, 56, 17));
		this.addSlotToContainer(new Slot(this.tilePrivateFurnace, 1, 56, 53));
		this.addSlotToContainer(new SlotFurnace(inventory.player, this.tilePrivateFurnace, 2, 116, 35));
		int i;

		for(i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	public void addCraftingToCrafters(ICrafting crafting)
	{
		super.addCraftingToCrafters(crafting);
		crafting.sendProgressBarUpdate(this, 0, this.tilePrivateFurnace.furnaceCookTime);
		crafting.sendProgressBarUpdate(this, 1, this.tilePrivateFurnace.furnaceBurnTime);
		crafting.sendProgressBarUpdate(this, 2, this.tilePrivateFurnace.currentItemBurnTime);
	}

	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for(int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if(this.lastCookTime != this.tilePrivateFurnace.furnaceCookTime)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.tilePrivateFurnace.furnaceCookTime);
			}

			if(this.lastBurnTime != this.tilePrivateFurnace.furnaceBurnTime)
			{
				icrafting.sendProgressBarUpdate(this, 1, this.tilePrivateFurnace.furnaceBurnTime);
			}

			if(this.lastItemBurnTime != this.tilePrivateFurnace.currentItemBurnTime)
			{
				icrafting.sendProgressBarUpdate(this, 2, this.tilePrivateFurnace.currentItemBurnTime);
			}
		}

		this.lastCookTime = this.tilePrivateFurnace.furnaceCookTime;
		this.lastBurnTime = this.tilePrivateFurnace.furnaceBurnTime;
		this.lastItemBurnTime = this.tilePrivateFurnace.currentItemBurnTime;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value)
	{
		if(id == 0)
		{
			this.tilePrivateFurnace.furnaceCookTime = value;
		}

		if(id == 1)
		{
			this.tilePrivateFurnace.furnaceBurnTime = value;
		}

		if(id == 2)
		{
			this.tilePrivateFurnace.currentItemBurnTime = value;
		}
	}

	public boolean canInteractWith(EntityPlayer player)
	{
		return this.tilePrivateFurnace.isUseableByPlayer(player);
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotId);

		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(slotId == 2)
			{
				if(!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(slotId != 1 && slotId != 0)
			{
				if(FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
				{
					if(!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if(TileEntityFurnace.isItemFuel(itemstack1))
				{
					if(!this.mergeItemStack(itemstack1, 1, 2, false))
					{
						return null;
					}
				}
				else if(slotId >= 3 && slotId < 30)
				{
					if(!this.mergeItemStack(itemstack1, 30, 39, false))
					{
						return null;
					}
				}
				else if(slotId >= 30 && slotId < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
				{
					return null;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 3, 39, false))
			{
				return null;
			}

			if(itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if(itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}
}
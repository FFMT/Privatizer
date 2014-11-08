package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPasswordChest extends Container
{
    private final TileEntityPasswordChest passwordChest;

    public ContainerPasswordChest(TileEntityPasswordChest tile, InventoryPlayer inventory)
    {
        this.passwordChest = tile;
        this.passwordChest.openInventory();

        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlotToContainer(new Slot(this.passwordChest, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
        this.bindPlayerInventory(inventory);
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        int i;
        for(i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 103 + i * 18 + 36));
            }
        }

        for(i = 0; i < 9; i++)
        {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 161 + 36));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return this.passwordChest.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotId);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotId < this.passwordChest.getSizeInventory())
            {
                if(!this.mergeItemStack(itemstack1, this.passwordChest.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.passwordChest.getSizeInventory(), false))
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
        }
        return itemstack;
    }

    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        this.passwordChest.closeInventory();
    }

    public TileEntityPasswordChest getTile()
    {
        return this.passwordChest;
    }
}
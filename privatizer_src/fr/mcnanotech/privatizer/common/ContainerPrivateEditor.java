package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPrivateEditor extends Container
{
    private final TileEntityPermissionEditor privateEditor;

    public ContainerPrivateEditor(TileEntityPermissionEditor tile, InventoryPlayer inventory)
    {
        this.privateEditor = tile;

        for(int i = 0; i < 9; i++)
        {
            this.addSlotToContainer(new Slot(this.privateEditor, i, 8 + i * 18, 47));
        }
        this.bindPlayerInventory(inventory);
    }

    private void bindPlayerInventory(InventoryPlayer playerInventory)
    {
        int i;
        for(i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return this.privateEditor.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotId)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotId);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotId < this.privateEditor.getSizeInventory())
            {
                if(!this.mergeItemStack(itemstack1, this.privateEditor.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, this.privateEditor.getSizeInventory(), false))
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

    public TileEntityPermissionEditor getTile()
    {
        return this.privateEditor;
    }
}

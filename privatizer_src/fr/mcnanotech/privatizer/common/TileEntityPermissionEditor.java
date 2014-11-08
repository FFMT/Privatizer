package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityPermissionEditor extends TileEntity implements IInventory
{
    private ItemStack[] contents = new ItemStack[9];
    private String customName;

    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbttagcompound);
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
        return this.hasCustomInventoryName() ? this.customName : "container.permissionEditor";
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
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }
    
    public boolean canUpdate()
    {
        return false;
    }
}
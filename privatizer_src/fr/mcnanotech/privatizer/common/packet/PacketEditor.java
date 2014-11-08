package fr.mcnanotech.privatizer.common.packet;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import cpw.mods.fml.common.network.ByteBufUtils;
import fr.mcnanotech.privatizer.common.ContainerPrivateEditor;
import fr.mcnanotech.privatizer.common.TileEntityPermissionEditor;
import fr.minecraftforgefrance.ffmtlibs.network.FFMTPacket;

public class PacketEditor extends FFMTPacket
{
    private String str;

    public PacketEditor()
    {

    }

    public PacketEditor(String str)
    {
        this.str = str;
    }

    @Override
    public void writeData(ByteBuf buffer) throws IOException
    {
        ByteBufUtils.writeUTF8String(buffer, this.str);
    }

    @Override
    public void readData(ByteBuf buffer)
    {
        this.str = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Container c = player.openContainer;
        if(c instanceof ContainerPrivateEditor)
        {
            TileEntityPermissionEditor editor = ((ContainerPrivateEditor)c).getTile();
            for(int i = 0; i < editor.getSizeInventory(); i++)
            {
                ItemStack stack = editor.getStackInSlot(i);
                if(stack != null)
                {
                    NBTTagCompound nbtTag = stack.getTagCompound();
                    if(nbtTag == null)
                    {
                        nbtTag = new NBTTagCompound();
                    }
                    NBTTagList list = new NBTTagList();
                    list.appendTag(new NBTTagString(player.getGameProfile().getId().toString()));
                    list.appendTag(new NBTTagString(this.str));
                    nbtTag.setTag("privatizer", list);
                    stack.setTagCompound(nbtTag);
                }
            }
        }
    }

    @Override
    public int getDiscriminator()
    {
        return 0;
    }
}
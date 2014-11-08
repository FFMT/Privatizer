package fr.mcnanotech.privatizer.common;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PrivatizerEventHandler
{
    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent event)
    {
        if(event.block == PrivatizerMod.privateBlock || event.block == PrivatizerMod.privateChest)
        {
            TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
            event.setCanceled(getIsCancel(tile, event.getPlayer()));
        }
        else if(event.block == PrivatizerMod.privateDoor)
        {
            TileEntity tile = event.world.getTileEntity(event.x, event.y, event.z);
            if(!(tile instanceof TileEntityPrivateDoor))
            {
                tile = event.world.getTileEntity(event.x, event.y - 1, event.z);
            }
            event.setCanceled(getIsCancel(tile, event.getPlayer()));
        }
    }

    public boolean getIsCancel(TileEntity tile, EntityPlayer player)
    {
        if(tile instanceof TileEntityPrivate)
        {
            TileEntityPrivate tePrivate = (TileEntityPrivate)tile;
            if(!PrivatizerHelper.isOwner(player, tePrivate.getOwner()))
            {
                player.addChatMessage(new ChatComponentTranslation("message.deny.break", tePrivate.getOwner() != null ? tePrivate.getOwner().getName() : "unknown"));
                return true;
            }
        }
        else if(tile instanceof TileEntityPassword)
        {
            TileEntityPassword tePass = (TileEntityPassword)tile;
            if(!PrivatizerHelper.checkPassword(player, tePass.getPassword()))
            {
                player.addChatMessage(new ChatComponentTranslation("message.password.break"));
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event)
    {
        if(event.itemStack.hasTagCompound())
        {
            NBTTagList list = event.itemStack.getTagCompound().getTagList("privatizer", Constants.NBT.TAG_STRING);
            if(!list.getStringTagAt(1).isEmpty() && !GameSettings.isKeyDown(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak))
            {
                // TODO translate
                event.toolTip.add("privatizer : this item has a password.");
                event.toolTip.add("Sneak to see");
            }
            else
            {
                if(list.getStringTagAt(0).equals(event.entityPlayer.getGameProfile().getId().toString()))
                {
                    event.toolTip.add("The password is : " + list.getStringTagAt(1));
                }
                else
                {
                    event.toolTip.add("Sorry but you aren't the owner of this item");
                }
            }
        }
    }
}
package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
			if(!PrivatizerHelper.canBreak(player, tePrivate.getOwner()))
			{
				player.addChatMessage(new ChatComponentTranslation("message.deny.break", tePrivate.getOwner() != null ? tePrivate.getOwner().getName() : "unknown"));
				return true;
			}
		}
		return false;
	}
}
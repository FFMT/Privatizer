package fr.mcnanotech.privatizer.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PrivatizerEventHandler
{
	@SubscribeEvent
	public void onBlockDestroyed(BlockEvent.BreakEvent event)
	{
		if(event.block == PrivatizerMod.privateBlock || event.block == PrivatizerMod.privateChest || event.block == PrivatizerMod.privateDoor)
		{
			TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
			if(te instanceof TileEntityPrivate)
			{
				TileEntityPrivate tePrivate = (TileEntityPrivate)te;
				if(!PrivatizerHelper.canBreak(event.getPlayer().getCommandSenderName(), tePrivate.getOwner()))
				{
					event.getPlayer().addChatMessage(new ChatComponentTranslation("message.deny.break", tePrivate.getOwner() != null ? tePrivate.getOwner() : "null"));
					event.setCanceled(true);
				}
			}
		}
	}
}
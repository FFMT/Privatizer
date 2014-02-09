package fr.mcnanotech.privatizer.common;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PrivatizerEventHandler
{
	@SubscribeEvent
	public void onBlockDestroyed(BlockEvent.BreakEvent event)
	{
		if(event.block instanceof BlockPrivate)
		{
			TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
			if(te != null && te instanceof TileEntityPrivate)
			{
				TileEntityPrivate tePrivate = (TileEntityPrivate)te;
				if(!event.getPlayer().getCommandSenderName().equals(tePrivate.getOwner()))
				{
					event.setCanceled(true);
					event.setResult(Result.DENY);
				}
			}
		}
	}
}
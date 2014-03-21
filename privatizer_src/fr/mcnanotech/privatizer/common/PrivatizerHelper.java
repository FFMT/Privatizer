package fr.mcnanotech.privatizer.common;

import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class PrivatizerHelper
{
	public static boolean canBreak(String player, String owner)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(owner != null && !owner.isEmpty() && (player.equals(owner) || (PrivatizerMod.opCanRemoveBlock && server.getConfigurationManager().isPlayerOpped(player))))
		{
			return true;
		}
		return false;
	}
}
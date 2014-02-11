package fr.mcnanotech.privatizer.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;

public class PrivatizerHelper
{
	public static boolean canBreak(String player, String owner)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(player.equals(owner) || (PrivatizerMod.opCanRemoveBlock && server.getConfigurationManager().isPlayerOpped(player)))
		{
			return true;
		}
		return false;
	}
}
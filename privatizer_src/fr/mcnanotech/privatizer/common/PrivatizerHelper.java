package fr.mcnanotech.privatizer.common;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.google.common.base.Splitter;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;

public class PrivatizerHelper
{
	public static final Splitter newLineSplitter = Splitter.on("\\n");

	public static boolean canBreak(EntityPlayer player, GameProfile owner)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(owner != null && (player.getGameProfile().equals(owner) || (PrivatizerMod.opCanRemoveBlock && server.getConfigurationManager().func_152596_g(player.getGameProfile()))))
		{
			return true;
		}
		return false;
	}
}
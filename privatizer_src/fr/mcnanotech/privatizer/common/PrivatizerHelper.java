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

	public static boolean canBreak(EntityPlayer player, UUID owner)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if(owner != null && (player.getUniqueID().equals(owner) || (PrivatizerMod.opCanRemoveBlock && server.getConfigurationManager().func_152596_g(player.getGameProfile()))))
		{
			return true;
		}
		return false;
	}

	public static String getUsername(UUID uuid)
	{
		if(uuid == null)
		{
			return "none";
		}
		GameProfile gameprofile = MinecraftServer.getServer().func_152358_ax().func_152652_a(uuid);
		if(gameprofile != null)
		{
			return gameprofile.getName();
		}
		return "unknown";
	}
}
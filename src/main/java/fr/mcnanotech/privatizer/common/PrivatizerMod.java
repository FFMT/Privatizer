package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "Privatizer", name = "Privatizer", version = "@VERSION@")

public class PrivatizerMod
{
	public static Block privateBlock;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		privateBlock = new BlockPrivate(Material.rock).setBlockName("privateBlock");
		GameRegistry.registerBlock(privateBlock, "privateBlock");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(new PrivatizerEventHandler());
	}
}
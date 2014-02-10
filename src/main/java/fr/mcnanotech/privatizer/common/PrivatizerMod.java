package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "privatizer", name = "Privatizer", version = "@VERSION@")

public class PrivatizerMod
{
	public static Block privateBlock, privateDoor, privateChest, keyChanger;
	public static Item key, bunchOfKeys, passPaper, securityPickaxe;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		privateBlock = new BlockPrivate(Material.rock).setBlockName("private").setResistance(5000F).setHardness(5F);
		
		GameRegistry.registerBlock(privateBlock, ItemBlockPrivate.class, "privateBlock", "privatizer");
		
		GameRegistry.registerTileEntity(TileEntityPrivate.class, "privatizer:Private");
		GameRegistry.registerTileEntity(TileEntityPrivateAdaptable.class, "privatizer:PrivateAdaptable");
		GameRegistry.registerTileEntity(TileEntityFriend.class, "privatizer:Friend");
		GameRegistry.registerTileEntity(TileEntityPassword.class, "privatizer:PassWord");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PrivatizerEventHandler());
	}
}
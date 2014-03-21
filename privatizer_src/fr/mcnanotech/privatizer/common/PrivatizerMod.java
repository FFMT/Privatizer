package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = PrivatizerMod.MODID, name = "Privatizer", version = "@VERSION@")
public class PrivatizerMod
{
	public static final String MODID = "privatizer";
	
	public static Block privateBlock, privateChest, privateDoor, keyChanger;
	public static Item key, bunchOfKeys, passPaper, securityPickaxe;
	public static boolean opCanRemoveBlock;

	@Instance("privatizer")
	public static PrivatizerMod instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try
		{
			cfg.load();
			opCanRemoveBlock = cfg.get(Configuration.CATEGORY_GENERAL, "can op destroy", false, "If set to true, operator can break private block without being the owner of the block").getBoolean(false);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally 
		{
			if(cfg.hasChanged())
			{
				cfg.save();
			}
		}
		
		privateBlock = new BlockPrivate(Material.rock).setBlockName("private").setResistance(5000F).setHardness(10F);
		privateChest = new BlockPrivateChest(Material.iron).setBlockName("privateChest").setResistance(5000F).setHardness(10F);
		privateDoor = new BlockPrivateDoor(Material.iron).setBlockName("privateDoor").setResistance(5000F).setHardness(10F);
		
		GameRegistry.registerBlock(privateBlock, ItemBlockPrivate.class, "private_block");
		GameRegistry.registerBlock(privateChest, "private_chest");
		GameRegistry.registerBlock(privateDoor, ItemBlockPrivateDoor.class, "private_door");

		GameRegistry.registerTileEntity(TileEntityPrivate.class, "privatizer:Private");
		GameRegistry.registerTileEntity(TileEntityPrivateAdaptable.class, "privatizer:PrivateAdaptable");
		GameRegistry.registerTileEntity(TileEntityPrivateChest.class, "privatizer:PrivateChest");
		GameRegistry.registerTileEntity(TileEntityPrivateDoor.class, "privatizer:PrivateDoor");
		GameRegistry.registerTileEntity(TileEntityFriend.class, "privatizer:Friend");
		GameRegistry.registerTileEntity(TileEntityPassword.class, "privatizer:PassWord");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PrivatizerEventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new PrivatizerGuiHandler());
	}
}
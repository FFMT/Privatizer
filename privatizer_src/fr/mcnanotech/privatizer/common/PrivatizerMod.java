package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = PrivatizerMod.MODID, name = "Privatizer", version = "@VERSION@")
public class PrivatizerMod
{
	public static final String MODID = "privatizer";

	@Instance("privatizer")
	public static PrivatizerMod instance;

	@SidedProxy(clientSide = "fr.mcnanotech.privatizer.client.ClientProxy", serverSide = "fr.mcnanotech.privatizer.common.CommonProxy")
	public static CommonProxy proxy;

	public static Block privateBlock, privateChest, privateDoor, privateEditor;
	public static Item key;
	public static boolean opCanRemoveBlock;
	public static CreativeTabs privatizerTab = new CreativeTabs("privatizer")
	{
		@Override
		public Item getTabIconItem()
		{
			return key;
		}

		@Override
		public int func_151243_f()
		{
			return 3;
		}
	};

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

		privateBlock = new BlockPrivate(Material.rock).setBlockName("private").setResistance(5000F).setHardness(10F).setCreativeTab(privatizerTab);
		privateChest = new BlockPrivateChest(Material.iron).setBlockName("privateChest").setResistance(5000F).setHardness(10F).setCreativeTab(privatizerTab);
		privateDoor = new BlockPrivateDoor(Material.iron).setBlockName("privateDoor").setResistance(5000F).setHardness(10F).setCreativeTab(privatizerTab);

		GameRegistry.registerBlock(privateBlock, ItemBlockPrivate.class, "block_private");
		GameRegistry.registerBlock(privateChest, "block_private_chest");
		GameRegistry.registerBlock(privateDoor, ItemBlockPrivateDoor.class, "block_private_door");

		GameRegistry.registerTileEntity(TileEntityPrivate.class, "privatizer:Private");
		GameRegistry.registerTileEntity(TileEntityPrivateAdaptable.class, "privatizer:PrivateAdaptable");
		GameRegistry.registerTileEntity(TileEntityPrivateFurnace.class, "privatizer:Furnace");
		GameRegistry.registerTileEntity(TileEntityPrivateChest.class, "privatizer:PrivateChest");
		GameRegistry.registerTileEntity(TileEntityPrivateDoor.class, "privatizer:PrivateDoor");
		GameRegistry.registerTileEntity(TileEntityFriend.class, "privatizer:Friend");
		GameRegistry.registerTileEntity(TileEntityPassword.class, "privatizer:PassWord");

		key = new ItemKey().setUnlocalizedName("key").setCreativeTab(privatizerTab);

		GameRegistry.registerItem(key, "item_key");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PrivatizerEventHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(this.instance, new PrivatizerGuiHandler());
		proxy.registerRender();
	}
}
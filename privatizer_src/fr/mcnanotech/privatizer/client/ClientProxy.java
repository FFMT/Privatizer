package fr.mcnanotech.privatizer.client;

import fr.mcnanotech.privatizer.common.CommonProxy;
import fr.mcnanotech.privatizer.common.PrivatizerMod;
import fr.mcnanotech.privatizer.common.TileEntityPrivateChest;
import fr.minecraftforgefrance.ffmtlibs.FFMTClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRender()
	{
		FFMTClientRegistry.bindTESRWithInventoryRender(PrivatizerMod.privateChest, 0, TileEntityPrivateChest.class, new TileEntityPrivateChestRenderer());
	}
}
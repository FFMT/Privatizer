package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import fr.mcnanotech.privatizer.client.GuiPrivateChest;
import fr.mcnanotech.privatizer.client.GuiPrivateFurnace;

public class PrivatizerGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntityPrivateChest)
		{
			return new ContainerPrivateChest((TileEntityPrivateChest)te, player.inventory);
		}
		else if(te instanceof TileEntityPrivateFurnace)
		{
			return new ContainerPrivateFurnace((TileEntityPrivateFurnace)te, player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TileEntityPrivateChest)
		{
			return new GuiPrivateChest((TileEntityPrivateChest)te, player.inventory);
		}
		else if(te instanceof TileEntityPrivateFurnace)
		{
			return new GuiPrivateFurnace((TileEntityPrivateFurnace)te, player.inventory);
		}
		return null;
	}
}
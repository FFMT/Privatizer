package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import fr.mcnanotech.kevin_68.nanotechmod.main.container.ContainerPortableChest;
import fr.mcnanotech.kevin_68.nanotechmod.main.tileentity.TileEntityPortableChest;
import fr.mcnanotech.privatizer.client.GuiPrivateChest;

public class PrivatizerGuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityPrivateChest)
		{
			return new ContainerPrivateChest((TileEntityPrivateChest)te, player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityPrivateChest)
		{
			return new GuiPrivateChest((TileEntityPrivateChest)te, player.inventory);
		}
		return null;
	}
}
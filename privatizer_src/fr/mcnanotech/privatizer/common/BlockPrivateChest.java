package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPrivateChest extends Block
{
	protected BlockPrivateChest(Material material)
	{
		super(material);
	}
	
	/*
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }*/
    
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }
    
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityPrivateChest();
    }
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te != null && te instanceof TileEntityPrivateChest)
    	{
    		player.openGui(PrivatizerMod.instance, 0, world, x, y, z);
    		return true;
    	}
        return false;
    }
    
    public boolean hasComparatorInputOverride()
    {
        return true;
    }
    
    public int getComparatorInputOverride(World world, int x, int y, int z, int direction)
    {
        return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
    }
    
    public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int value)
    {
        super.onBlockEventReceived(world, x, y, z, eventId, value);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(eventId, value) : false;
    }
}
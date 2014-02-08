package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPrivate extends Block
{
	protected BlockPrivate(Material material)
	{
		super(material);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityPrivate();
	}
	
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack stack)
    {
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te != null && te instanceof TileEntityPrivate)
    	{
    		TileEntityPrivate tePrivate = (TileEntityPrivate)te;
    		tePrivate.setOwner(living.getCommandSenderName());
    	}
    }
}
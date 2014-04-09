package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockPrivateChest extends Block
{
	protected BlockPrivateChest(Material material)
	{
		super(material);
	}

	/*
	 * public boolean isOpaqueCube() { return false; }
	 * 
	 * public boolean renderAsNormalBlock() { return false; }
	 */

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
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityPrivateChest)
		{
			TileEntityPrivateChest chest = (TileEntityPrivateChest)tile;
			if(PrivatizerHelper.canBreak(player.getCommandSenderName(), chest.getOwner()))
			{
				player.openGui(PrivatizerMod.instance, 0, world, x, y, z);
			}
			else if(!world.isRemote)
			{
				player.addChatMessage(new ChatComponentTranslation("message.deny.open", chest.getOwner() != null ? chest.getOwner() : "null"));
			}
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
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack stack)
	{
		if(stack.getItemDamage() == 0)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileEntityPrivate)
			{
				TileEntityPrivate tePrivate = (TileEntityPrivate)te;
				tePrivate.setOwner(living.getCommandSenderName());
			}
		}
	}
	
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityPrivate)
		{
			TileEntityPrivate tePrivate = (TileEntityPrivate)te;
			if(!player.getCommandSenderName().equals(tePrivate.getOwner()))
			{
				return -1;
			}
		}
		return ForgeHooks.blockStrength(this, player, world, x, y, z);
	}
	
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileEntityPrivate)
			{
				TileEntityPrivate tePrivate = (TileEntityPrivate)te;
				if(!player.getCommandSenderName().equals(tePrivate.getOwner()))
				{
					player.addChatMessage(new ChatComponentTranslation("message.deny.open", tePrivate.getOwner() != null ? tePrivate.getOwner() : "null"));
				}
			}
		}
	}
}
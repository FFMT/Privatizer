package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.minecraftforgefrance.ffmtlibs.FFMTClientRegistry;

public class BlockPrivateChest extends Block
{
	protected BlockPrivateChest(Material material)
	{
		super(material);
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderType()
	{
		return FFMTClientRegistry.tesrRenderId;
	}

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
		if(world.isRemote)
		{
			return true;
		}
		if(world.getBlockMetadata(x, y, z) == 0 && tile instanceof TileEntityPrivateChest && !player.isSneaking())
		{
			TileEntityPrivateChest chest = (TileEntityPrivateChest)tile;
			if(PrivatizerHelper.canBreak(player.getCommandSenderName(), chest.getOwner()))
			{
				player.openGui(PrivatizerMod.instance, 0, world, x, y, z);
			}
			else
			{
				player.addChatMessage(new ChatComponentTranslation("message.deny.open", chest.getOwner() != null ? chest.getOwner() : "null"));
			}
		}
		return true;
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
		int direction = (MathHelper.floor_double((double)(living.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3) + 2;
		if(stack.getItemDamage() == 0)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileEntityPrivateChest)
			{
				TileEntityPrivateChest tePrivate = (TileEntityPrivateChest)te;
				tePrivate.setOwner(living.getCommandSenderName());
				tePrivate.setDirection((byte)direction);
				if(stack.hasDisplayName())
				{
					tePrivate.setInventoryName(stack.getDisplayName());
				}
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

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return AxisAlignedBB.getAABBPool().getAABB(x + 0.065D, y, z + 0.065D, x + 0.935D, y + 1, z + 0.935D);
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return AxisAlignedBB.getAABBPool().getAABB(x + 0.065D, y, z + 0.065D, x + 0.935D, y + 1, z + 0.935D);
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
	{
		if((axis == ForgeDirection.UP || axis == ForgeDirection.DOWN) && !world.isRemote)
		{
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityPrivateChest)
			{
				TileEntityPrivateChest tePrivate = (TileEntityPrivateChest)tile;
				byte direction = tePrivate.getDirection();
				direction++;
				if(direction > 5)
				{
					direction = 2;
				}
				tePrivate.setDirection(direction);
				return true;
			}
		}
		return false;
	}

	public ForgeDirection[] getValidRotations(World world, int x, int y, int z)
	{
		return new ForgeDirection[] {ForgeDirection.UP, ForgeDirection.DOWN};
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof IInventory)
		{
			IInventory inv = (IInventory)tile;
			for(int i1 = 0; i1 < inv.getSizeInventory(); ++i1)
			{
				ItemStack itemstack = inv.getStackInSlot(i1);

				if(itemstack != null)
				{
					float f = world.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for(float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
					{
						int j1 = world.rand.nextInt(21) + 10;

						if(j1 > itemstack.stackSize)
						{
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
						entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);

						if(itemstack.hasTagCompound())
						{
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
						}
					}
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, metadata);
	}
}
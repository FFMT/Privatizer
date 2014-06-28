package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPrivateDoor extends ItemBlock
{
	public ItemBlockPrivateDoor(Block block)
	{
		super(block);
		this.maxStackSize = 3;
	}

	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata)
	{
		return itemIcon;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iiconRegister)
	{
		this.itemIcon = iiconRegister.registerIcon("privatizer:private_door");
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
	{
		if(side != 1)
		{
			return false;
		}
		else
		{
			++y;

			if(player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))
			{
				if(!field_150939_a.canPlaceBlockAt(world, x, y, z))
				{
					return false;
				}
				else
				{
					int direction = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					placeDoorBlock(world, x, y, z, direction, field_150939_a, player);
					--stack.stackSize;
					return true;
				}
			}
			else
			{
				return false;
			}
		}
	}

	public static void placeDoorBlock(World world, int x, int y, int z, int direction, Block block, EntityPlayer player)
	{
		byte b0 = 0;
		byte b1 = 0;

		if(direction == 0)
		{
			b1 = 1;
		}

		if(direction == 1)
		{
			b0 = -1;
		}

		if(direction == 2)
		{
			b1 = -1;
		}

		if(direction == 3)
		{
			b0 = 1;
		}

		int i1 = (world.getBlock(x - b0, y, z - b1).isNormalCube() ? 1 : 0) + (world.getBlock(x - b0, y + 1, z - b1).isNormalCube() ? 1 : 0);
		int j1 = (world.getBlock(x + b0, y, z + b1).isNormalCube() ? 1 : 0) + (world.getBlock(x + b0, y + 1, z + b1).isNormalCube() ? 1 : 0);
		boolean flag = world.getBlock(x - b0, y, z - b1) == block || world.getBlock(x - b0, y + 1, z - b1) == block;
		boolean flag1 = world.getBlock(x + b0, y, z + b1) == block || world.getBlock(x + b0, y + 1, z + b1) == block;
		boolean flag2 = false;

		if(flag && !flag1)
		{
			flag2 = true;
		}
		else if(j1 > i1)
		{
			flag2 = true;
		}

		world.setBlock(x, y, z, block, 0, 2);
		world.setBlock(x, y + 1, z, block, 1, 2);
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityPrivateDoor)
		{
			TileEntityPrivateDoor tileDoor = (TileEntityPrivateDoor)te;
			tileDoor.setDirection(direction);
			tileDoor.setOwner(player.getUniqueID());
		}
		world.markBlockForUpdate(x, y, z);
		world.notifyBlocksOfNeighborChange(x, y, z, block);
		world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
	}
}
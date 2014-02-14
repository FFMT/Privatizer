package fr.mcnanotech.privatizer.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPrivate extends Block
{
	public static String[] type = new String[] {"stone", "brick", "adaptable", "furnace", "friendlyStone", "friendlyBrick", "friendlyAdaptable", "friendlyFurnace", "passwordStone", "passwordBrick", "passwordAdaptable", "passwordFurnace"};

	public IIcon stoneIcon, brickIcon, adaptableIcon, friendlyStoneIcon, friendlyBrickIcon, passwordStoneIcon, passwordBrickIcon;

	protected BlockPrivate(Material material)
	{
		super(material);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public boolean hasTileEntity(int metadata)
	{
		if(metadata >= 0 && metadata <= type.length)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public TileEntity createTileEntity(World world, int metadata)
	{
		switch(metadata)
		{
		case 0:
			return new TileEntityPrivate();
		case 1:
			return new TileEntityPrivate();
		case 2:
			return new TileEntityPrivateAdaptable();
		case 3:
			return new TileEntityPrivate(); // TODO create furnace tile entity private
		case 4:
			return new TileEntityFriend();
		case 5:
			return new TileEntityFriend();
		case 6:
			return new TileEntityFriend();
		case 7:
			return new TileEntityFriend(); // TODO create furnace tile entity friend
		case 8:
			return new TileEntityPassword();
		case 9:
			return new TileEntityPassword();
		case 10:
			return new TileEntityPassword();
		case 11:
			return new TileEntityPassword(); // TODO create furnace tile entity password
		default:
			return null;
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase living, ItemStack stack)
	{
		if(stack.getItemDamage() >= 0 && stack.getItemDamage() <= 3)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityPrivate)
			{
				TileEntityPrivate tePrivate = (TileEntityPrivate)te;
				tePrivate.setOwner(living.getCommandSenderName());
			}
		}
		else if(stack.getItemDamage() >= 4 && stack.getItemDamage() <= 7)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityFriend)
			{
				TileEntityFriend teFriend = (TileEntityFriend)te;
				// TODO friend list
				// teFriend.setOwners(list);
			}
		}
		else if(stack.getItemDamage() >= 8 && stack.getItemDamage() <= 11)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityPassword)
			{
				TileEntityPassword tePassword = (TileEntityPassword)te;
				// TODO password
				// tePassword.setPassword(str);
			}
		}
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if(world.getBlockMetadata(x, y, z) == 2)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityPrivateAdaptable)
			{
				TileEntityPrivateAdaptable tePrivAdaptable = (TileEntityPrivateAdaptable)te;
				if(player.getCurrentEquippedItem() != null && Block.getBlockFromItem(player.getCurrentEquippedItem().getItem()) != null)
				{
					if(player.getCurrentEquippedItem().getItem() == Item.getItemFromBlock(this) || (player.getCurrentEquippedItem().getItem() == Item.getItemFromBlock(tePrivAdaptable.getBlockForTexture()) && player.getCurrentEquippedItem().getItemDamage() == tePrivAdaptable.getBlockMetadataForTexture()))
					{
						return false;
					}
					if(Block.getBlockFromItem(player.getCurrentEquippedItem().getItem()).isOpaqueCube())
					{
						tePrivAdaptable.setStack(player.getCurrentEquippedItem());
						world.markBlockForUpdate(x, y, z);
					}
					else if(!world.isRemote)
					{
						// TODO translation
						player.addChatMessage(new ChatComponentText("You can't apply this texture !"));
					}
				}
				else if(!world.isRemote)
				{
					// TODO translation
					player.addChatMessage(new ChatComponentText("Applied texture : " + (tePrivAdaptable.getBlockForTexture() != null ? tePrivAdaptable.getBlockForTexture().getLocalizedName() : "null")));
				}
				return true;
			}
		}
		return false;
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

	public int damageDropped(int metadata)
	{
		return metadata;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tabs, List list)
	{
		for(int i = 0; i < this.type.length; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	public void registerBlockIcons(IIconRegister iiconRegister)
	{
		this.blockIcon = iiconRegister.registerIcon("privatizer:null");
		this.stoneIcon = iiconRegister.registerIcon("privatizer:private_stone");
		this.brickIcon = iiconRegister.registerIcon("privatizer:private_brick");
		this.adaptableIcon = iiconRegister.registerIcon("privatizer:private_adaptable");
		this.friendlyStoneIcon = iiconRegister.registerIcon("privatizer:friendly_stone");
		this.friendlyBrickIcon = iiconRegister.registerIcon("privatizer:friendly_brick");
		this.passwordStoneIcon = iiconRegister.registerIcon("privatizer:password_stone");
		this.passwordBrickIcon = iiconRegister.registerIcon("privatizer:password_brick");
	}

	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		if(world.getBlockMetadata(x, y, z) == 2)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityPrivateAdaptable)
			{
				TileEntityPrivateAdaptable tePrivAdaptable = (TileEntityPrivateAdaptable)te;
				if(tePrivAdaptable.getBlockForTexture() != null && tePrivAdaptable.getBlockForTexture().isOpaqueCube())
				{
					return tePrivAdaptable.getBlockForTexture().getIcon(side, tePrivAdaptable.getBlockMetadataForTexture());
				}
			}
		}
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
	}

	public IIcon getIcon(int side, int metadata)
	{
		switch(metadata)
		{
		case 0:
			return this.stoneIcon;
		case 1:
			return this.brickIcon;
		case 2:
			return this.adaptableIcon;
		case 4:
			return this.friendlyStoneIcon;
		case 5:
			return this.friendlyBrickIcon;
		case 8:
			return this.passwordStoneIcon;
		case 9:
			return this.passwordBrickIcon;
		default:
			return this.blockIcon;
		}
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityPrivate)
			{
				TileEntityPrivate tePrivate = (TileEntityPrivate)te;
				if(!player.getCommandSenderName().equals(tePrivate.getOwner()))
				{
					// TODO translation
					player.addChatMessage(new ChatComponentText("You can't remove this block, the owner is : " + tePrivate.getOwner()));
				}
			}
		}
	}
    
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
		if(world.getBlockMetadata(target.blockX, target.blockY, target.blockZ) == 2)
		{
			TileEntity te = world.getTileEntity(target.blockX, target.blockY, target.blockZ);
			if(te != null && te instanceof TileEntityPrivateAdaptable)
			{
				TileEntityPrivateAdaptable tePrivAdaptable = (TileEntityPrivateAdaptable)te;
				if(tePrivAdaptable.getBlockForTexture() != null)
				{
			        float f = 0.1F;
			        double d0 = (double)target.blockX + world.rand.nextDouble() * (tePrivAdaptable.getBlockForTexture().getBlockBoundsMaxX() - tePrivAdaptable.getBlockForTexture().getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + tePrivAdaptable.getBlockForTexture().getBlockBoundsMinX();
			        double d1 = (double)target.blockY + world.rand.nextDouble() * (tePrivAdaptable.getBlockForTexture().getBlockBoundsMaxY() - tePrivAdaptable.getBlockForTexture().getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + tePrivAdaptable.getBlockForTexture().getBlockBoundsMinY();
			        double d2 = (double)target.blockZ + world.rand.nextDouble() * (tePrivAdaptable.getBlockForTexture().getBlockBoundsMaxZ() - tePrivAdaptable.getBlockForTexture().getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + tePrivAdaptable.getBlockForTexture().getBlockBoundsMinZ();

			        if (target.sideHit == 0)
			        {
			            d1 = (double)target.blockY + tePrivAdaptable.getBlockForTexture().getBlockBoundsMinY() - (double)f;
			        }

			        if (target.sideHit == 1)
			        {
			            d1 = (double)target.blockY + tePrivAdaptable.getBlockForTexture().getBlockBoundsMaxY() + (double)f;
			        }

			        if (target.sideHit == 2)
			        {
			            d2 = (double)target.blockZ + tePrivAdaptable.getBlockForTexture().getBlockBoundsMinZ() - (double)f;
			        }

			        if (target.sideHit == 3)
			        {
			            d2 = (double)target.blockZ + tePrivAdaptable.getBlockForTexture().getBlockBoundsMaxZ() + (double)f;
			        }

			        if (target.sideHit == 4)
			        {
			            d0 = (double)target.blockX + tePrivAdaptable.getBlockForTexture().getBlockBoundsMinX() - (double)f;
			        }

			        if (target.sideHit == 5)
			        {
			            d0 = (double)target.blockX + tePrivAdaptable.getBlockForTexture().getBlockBoundsMaxX() + (double)f;
			        }

			        effectRenderer.addEffect((new EntityDiggingFX(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, tePrivAdaptable.getBlockForTexture(), tePrivAdaptable.getBlockMetadataForTexture())).applyColourMultiplier(target.blockX, target.blockY, target.blockZ).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
			        return true;
				}
			}
		}
		return super.addHitEffects(world, target, effectRenderer);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
		if(world.getBlockMetadata(x, y, z) == 2)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileEntityPrivateAdaptable)
			{
				TileEntityPrivateAdaptable tePrivAdaptable = (TileEntityPrivateAdaptable)te;
				if(tePrivAdaptable.getBlockForTexture() != null)
				{
		            byte b0 = 4;

		            for (int i1 = 0; i1 < b0; ++i1)
		            {
		                for (int j1 = 0; j1 < b0; ++j1)
		                {
		                    for (int k1 = 0; k1 < b0; ++k1)
		                    {
		                        double d0 = (double)x + ((double)i1 + 0.5D) / (double)b0;
		                        double d1 = (double)y + ((double)j1 + 0.5D) / (double)b0;
		                        double d2 = (double)z + ((double)k1 + 0.5D) / (double)b0;
		                        effectRenderer.addEffect((new EntityDiggingFX(world, d0, d1, d2, d0 - (double)x - 0.5D, d1 - (double)y - 0.5D, d2 - (double)z - 0.5D, tePrivAdaptable.getBlockForTexture(), tePrivAdaptable.getBlockMetadata())).applyColourMultiplier(x, y, z));
		                    }
		                }
		            }
					return true;
				}
			}
		}
        return super.addDestroyEffects(world, x, y, z, meta, effectRenderer);
    }
}
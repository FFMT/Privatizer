package fr.mcnanotech.privatizer.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.minecraftforgefrance.ffmtlibs.FFMTClientRegistry;

public class BlockPrivateChest extends Block
{
    public static String[] subBlock = new String[] {"private", "friendly", "password"};
    private IIcon[] icons = new IIcon[subBlock.length];

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

    public void registerBlockIcons(IIconRegister iiconRegister)
    {

    }

    public IIcon getIcon(int side, int metadata)
    {
        return PrivatizerMod.privateBlock.getIcon(0, metadata * 4);
    }

    public boolean hasTileEntity(int metadata)
    {
        return metadata == 0 || metadata == 2;
    }

    public TileEntity createTileEntity(World world, int metadata)
    {
        return metadata == 0 ? new TileEntityPrivateChest() : metadata == 2 ? new TileEntityPasswordChest() : null;
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
            if(PrivatizerHelper.isOwner(player, chest.getOwner()))
            {
                player.openGui(PrivatizerMod.instance, 3, world, x, y, z);
            }
            else
            {
                player.addChatMessage(new ChatComponentTranslation("message.deny.open", chest.getOwner() != null ? chest.getOwner().getName() : "unknown"));
            }
        }
        else if(world.getBlockMetadata(x, y, z) == 2 && tile instanceof TileEntityPasswordChest && !player.isSneaking())
        {
            TileEntityPasswordChest chest = (TileEntityPasswordChest)tile;
            if(PrivatizerHelper.checkPassword(player, chest.getPassword()))
            {
                player.openGui(PrivatizerMod.instance, 4, world, x, y, z);
            }
            else
            {
                player.addChatMessage(new ChatComponentTranslation("message.password.open"));
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        for(int i = 0; i < subBlock.length; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public int damageDropped(int metadata)
    {
        return metadata;
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
        if(living instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)living;
            TileEntity te = world.getTileEntity(x, y, z);
            if(stack.getItemDamage() == 0 && te instanceof TileEntityPrivateChest)
            {
                TileEntityPrivateChest tePrivate = (TileEntityPrivateChest)te;
                tePrivate.setOwner(player.getGameProfile());
                tePrivate.setDirection((byte)direction);
                if(stack.hasDisplayName())
                {
                    tePrivate.setInventoryName(stack.getDisplayName());
                }
            }
            else if(stack.getItemDamage() == 2 && te instanceof TileEntityPasswordChest)
            {
                TileEntityPasswordChest tePassword = (TileEntityPasswordChest)te;
                tePassword.setDirection((byte)direction);
                if(stack.hasDisplayName())
                {
                    tePassword.setInventoryName(stack.getDisplayName());
                }
                if(stack.hasTagCompound())
                {
                    NBTTagList list = stack.getTagCompound().getTagList("privatizer", Constants.NBT.TAG_STRING);
                    tePassword.setPassword(list.getStringTagAt(1));
                }
            }
        }
    }

    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof TileEntityPrivateChest)
        {
            TileEntityPrivateChest tePrivate = (TileEntityPrivateChest)te;
            if(!player.getGameProfile().equals(tePrivate.getOwner()))
            {
                return -1;
            }
        }
        else if(te instanceof TileEntityPassword)
        {
            TileEntityPassword tePass = (TileEntityPassword)te;
            if(!PrivatizerHelper.checkPassword(player, tePass.getPassword()))
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
                if(!player.getGameProfile().equals(tePrivate.getOwner()))
                {
                    player.addChatMessage(new ChatComponentTranslation("message.deny.break", tePrivate.getOwner() != null ? tePrivate.getOwner().getName() : "unknown"));
                }
            }
            else if(te instanceof TileEntityPassword)
            {
                TileEntityPassword tePass = (TileEntityPassword)te;
                if(!PrivatizerHelper.checkPassword(player, tePass.getPassword()))
                {
                    player.addChatMessage(new ChatComponentTranslation("message.password.break"));
                }
            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox(x + 0.065D, y, z + 0.065D, x + 0.935D, y + 1, z + 0.935D);
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox(x + 0.065D, y, z + 0.065D, x + 0.935D, y + 1, z + 0.935D);
    }

    @Override
    public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
    {
        if((axis == ForgeDirection.UP || axis == ForgeDirection.DOWN) && !world.isRemote)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof IDirectionalTile)
            {
                IDirectionalTile directionalTile = (IDirectionalTile)tile;
                byte direction = directionalTile.getDirection();
                direction++;
                if(direction > 5)
                {
                    direction = 2;
                }
                directionalTile.setDirection(direction);
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
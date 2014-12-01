package fr.mcnanotech.privatizer.common;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.mcnanotech.privatizer.client.ClientProxy;

public class BlockPrivateDoor extends Block
{
    private IIcon[] upperIcon = new IIcon[2];
    private IIcon[] lowerIcon = new IIcon[2];
    private IIcon[] upperPasswordIcon = new IIcon[2];
    private IIcon[] lowerPasswordIcon = new IIcon[2];

    protected BlockPrivateDoor(Material material)
    {
        super(material);
        float f = 0.5F;
        float f1 = 1.0F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    public boolean hasTileEntity(int metadata)
    {
        switch(metadata)
        {
            case 0:
                return true;
            case 2:
                return true;
            case 4:
                return true;
            default:
                return false;
        }
    }

    public TileEntity createTileEntity(World world, int metadata)
    {
        switch(metadata)
        {
            case 0:
                return new TileEntityPrivateDoor();
            case 2:
                return new TileEntityFriendDoor();
            case 4:
                return new TileEntityPasswordDoor();
            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        if(metadata == 4 || metadata == 5)
        {
            return this.lowerPasswordIcon[0];
        }
        return this.lowerIcon[0];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        if(side < 2)
        {
            if(world.getBlockMetadata(x, y, z) == 0 || world.getBlockMetadata(x, y, z) == 1)
            {
                return this.lowerIcon[0];
            }
            else if(world.getBlockMetadata(x, y, z) == 4 || world.getBlockMetadata(x, y, z) == 5)
            {
                return this.lowerPasswordIcon[0];
            }
        }
        if(world.getBlockMetadata(x, y, z) == 1)
        {
            TileEntity tile = world.getTileEntity(x, y - 1, z);
            if(tile instanceof TileEntityPrivateDoor)
            {
                TileEntityPrivateDoor door = (TileEntityPrivateDoor)tile;
                return shouldFlip(side, door.getDirection(), door.isDoubleDoor(), door.isOpen()) ? this.upperIcon[1] : this.upperIcon[0];
            }
        }
        else if(world.getBlockMetadata(x, y, z) == 0)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof TileEntityPrivateDoor)
            {
                TileEntityPrivateDoor door = (TileEntityPrivateDoor)tile;
                return shouldFlip(side, door.getDirection(), door.isDoubleDoor(), door.isOpen()) ? this.lowerIcon[1] : this.lowerIcon[0];
            }
        }
        else if(world.getBlockMetadata(x, y, z) == 4)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof TileEntityPasswordDoor)
            {
                TileEntityPasswordDoor door = (TileEntityPasswordDoor)tile;
                return shouldFlip(side, door.getDirection(), door.isDoubleDoor(), door.isOpen()) ? this.lowerPasswordIcon[1] : this.lowerPasswordIcon[0];
            }
        }
        else if(world.getBlockMetadata(x, y, z) == 5)
        {
            TileEntity tile = world.getTileEntity(x, y - 1, z);
            if(tile instanceof TileEntityPasswordDoor)
            {
                TileEntityPasswordDoor door = (TileEntityPasswordDoor)tile;
                return shouldFlip(side, door.getDirection(), door.isDoubleDoor(), door.isOpen()) ? this.upperPasswordIcon[1] : this.upperPasswordIcon[0];
            }
        }
        return this.lowerIcon[0];
    }

    private boolean shouldFlip(int side, int direction, boolean doubleDoor, boolean open)
    {
        switch(direction)
        {
            case 0:
                if((side == 5 && !open && !doubleDoor) || (side == 2 && open) || (side == 4 && doubleDoor))
                {
                    return true;
                }
                break;
            case 1:
                if((side == 3 && !open && !doubleDoor) || (side == 5 && open) || (side == 2 && doubleDoor))
                {
                    return true;
                }
                break;
            case 2:
                if((side == 4 && !open && !doubleDoor) || (side == 3 && open) || (side == 5 && doubleDoor))
                {
                    return true;
                }
                break;
            case 3:
                if((side == 2 && !open && !doubleDoor) || (side == 4 && open) || (side == 3 && doubleDoor))
                {
                    return true;
                }
                break;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iiconRegister)
    {
        this.upperIcon[0] = iiconRegister.registerIcon(PrivatizerMod.MODID + ":private_door_upper");
        this.lowerIcon[0] = iiconRegister.registerIcon(PrivatizerMod.MODID + ":private_door_lower");
        this.upperIcon[1] = new IconFlipped(this.upperIcon[0], true, false);
        this.lowerIcon[1] = new IconFlipped(this.lowerIcon[0], true, false);
        this.upperPasswordIcon[0] = iiconRegister.registerIcon(PrivatizerMod.MODID + ":password_door_upper");
        this.lowerPasswordIcon[0] = iiconRegister.registerIcon(PrivatizerMod.MODID + ":password_door_lower");
        this.upperPasswordIcon[1] = new IconFlipped(this.upperPasswordIcon[0], true, false);
        this.lowerPasswordIcon[1] = new IconFlipped(this.lowerPasswordIcon[0], true, false);
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
        return ClientProxy.doorId;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tile = (world.getBlockMetadata(x, y, z) & 1) == 0 ? world.getTileEntity(x, y, z) : world.getTileEntity(x, y - 1, z);
        if(tile instanceof IDoorTile)
        {
            IDoorTile tileDoor = (IDoorTile)tile;
            this.setBlockBounds(0.0F, 1.0F, tileDoor.getDirection(), tileDoor.isOpen(), tileDoor.isDoubleDoor());
        }
    }

    public void setBlockBounds(float min, float max, int direction, boolean open, boolean doubleDoor)
    {
        float f = 0.1875F;
        switch(direction)
        {
            case 0:
                if(open)
                {
                    if(doubleDoor)
                    {
                        this.setBlockBounds(0.0F, min, 1.0F - f, 1.0F, max, 1.0F);
                        break;
                    }
                    this.setBlockBounds(0.0F, min, 0.0F, 1.0F, max, f);
                    break;
                }
                this.setBlockBounds(0.0F, min, 0.0F, f, max, 1.0F);
                break;
            case 1:
                if(open)
                {
                    if(doubleDoor)
                    {
                        this.setBlockBounds(0.0F, min, 0.0F, f, max, 1.0F);
                        break;
                    }
                    this.setBlockBounds(1.0F - f, min, 0.0F, 1.0F, max, 1.0F);
                    break;
                }
                this.setBlockBounds(0.0F, min, 0.0F, 1.0F, max, f);
                break;
            case 2:
                if(open)
                {
                    if(doubleDoor)
                    {
                        this.setBlockBounds(0.0F, min, 0.0F, 1.0F, max, f);
                        break;
                    }
                    this.setBlockBounds(0.0F, min, 1.0F - f, 1.0F, max, 1.0F);
                    break;
                }
                this.setBlockBounds(1.0F - f, min, 0.0F, 1.0F, max, 1.0F);
                break;
            case 3:
                if(open)
                {
                    if(doubleDoor)
                    {
                        this.setBlockBounds(1.0F - f, min, 0.0F, 1.0F, max, 1.0F);
                        break;
                    }
                    this.setBlockBounds(0.0F, min, 0.0F, f, max, 1.0F);
                    break;
                }
                this.setBlockBounds(0.0F, min, 1.0F - f, 1.0F, max, 1.0F);
                break;
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if((metadata & 1) == 0)
        {
            boolean flag = false;

            if(world.getBlock(x, y + 1, z) != this)
            {
                world.setBlockToAir(x, y, z);
                flag = true;
            }

            if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z))
            {
                world.setBlockToAir(x, y, z);
                flag = true;

                if(world.getBlock(x, y + 1, z) == this)
                {
                    world.setBlockToAir(x, y + 1, z);
                }
            }

            if(flag)
            {
                if(!world.isRemote)
                {
                    this.dropBlockAsItem(world, x, y, z, metadata, 0);
                }
            }
        }
        else
        {
            if(world.getBlock(x, y - 1, z) != this)
            {
                world.setBlockToAir(x, y, z);
            }

            if(block != this)
            {
                this.onNeighborBlockChange(world, x, y - 1, z, block);
            }
        }
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec31, Vec3 vec32)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, vec31, vec32);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return y >= 255 ? false : World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && super.canPlaceBlockAt(world, x, y, z) && super.canPlaceBlockAt(world, x, y + 1, z);
    }

    public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player)
    {
        if(player.capabilities.isCreativeMode && (world.getBlockMetadata(x, y, z) & 1) != 0 && world.getBlock(x, y - 1, z) == this)
        {
            world.setBlockToAir(x, y - 1, z);
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List list)
    {
        for(int i = 0; i < 3; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }
    
    public int damageDropped(int metadata)
    {
        return metadata / 2;
    }
    
    public Item getItemDropped(int metadata, Random rand, int fortune)
    {
        return (metadata & 1) != 0 ? null : Item.getItemFromBlock(PrivatizerMod.privateDoor);
    }
}
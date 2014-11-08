package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
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
    private IIcon[] upperIcon;
    private IIcon[] lowerIcon;

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
            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return this.lowerIcon[0];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        if(side < 2)
        {
            return this.lowerIcon[0];
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
        else
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof TileEntityPrivateDoor)
            {
                TileEntityPrivateDoor door = (TileEntityPrivateDoor)tile;
                return shouldFlip(side, door.getDirection(), door.isDoubleDoor(), door.isOpen()) ? this.lowerIcon[1] : this.lowerIcon[0];
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
        this.upperIcon = new IIcon[2];
        this.lowerIcon = new IIcon[2];
        this.upperIcon[0] = iiconRegister.registerIcon(PrivatizerMod.MODID + ":private_door_upper");
        this.lowerIcon[0] = iiconRegister.registerIcon(PrivatizerMod.MODID + ":private_door_lower");
        this.upperIcon[1] = new IconFlipped(this.upperIcon[0], true, false);
        this.lowerIcon[1] = new IconFlipped(this.lowerIcon[0], true, false);
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
        if(world.getBlockMetadata(x, y, z) == 0)
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if(te instanceof TileEntityPrivateDoor)
            {
                TileEntityPrivateDoor tileDoor = (TileEntityPrivateDoor)te;
                this.setBlockBounds(0.0F, 1.0F, tileDoor.getDirection(), tileDoor.isOpen(), tileDoor.isDoubleDoor());
            }
        }
        else if(world.getBlockMetadata(x, y, z) == 1)
        {
            TileEntity te = world.getTileEntity(x, y - 1, z);
            if(te instanceof TileEntityPrivateDoor)
            {
                TileEntityPrivateDoor tileDoor = (TileEntityPrivateDoor)te;
                this.setBlockBounds(0, 1.0F, tileDoor.getDirection(), tileDoor.isOpen(), tileDoor.isDoubleDoor());
            }
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

        if(this.hasTileEntity(metadata))
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
        if(player.capabilities.isCreativeMode && !this.hasTileEntity(metadata) && world.getBlock(x, y - 1, z) == this)
        {
            world.setBlockToAir(x, y - 1, z);
        }
    }
}
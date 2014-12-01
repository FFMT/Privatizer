package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPrivateDoor extends ItemBlock
{
    private IIcon friendlyIcon, passwordIcon;

    public ItemBlockPrivateDoor(Block block)
    {
        super(block);
        this.maxStackSize = 16;
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public int getSpriteNumber()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata)
    {
        return metadata == 0 ? this.itemIcon : metadata == 1 ? this.friendlyIcon : this.passwordIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iiconRegister)
    {
        this.itemIcon = iiconRegister.registerIcon("privatizer:private_door");
        this.friendlyIcon = iiconRegister.registerIcon("privatizer:friendly_door");
        this.passwordIcon = iiconRegister.registerIcon("privatizer:password_door");
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        switch(stack.getItemDamage())
        {
            case 1:
                return "tile.friendlyDoor";
            case 2:
                return "tile.passwordDoor";
            default:
                return field_150939_a.getUnlocalizedName();
        }
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
                    placeDoorBlock(world, x, y, z, direction, field_150939_a, player, stack);
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

    private void placeDoorBlock(World world, int x, int y, int z, int direction, Block block, EntityPlayer player, ItemStack stack)
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
        world.setBlock(x, y, z, block, stack.getItemDamage() * 2, 2);
        world.setBlock(x, y + 1, z, block, stack.getItemDamage() * 2 + 1, 2);
        TileEntity te = world.getTileEntity(x, y, z);
        if(te instanceof IDoorTile)
        {
            IDoorTile tileDoor = (IDoorTile)te;
            tileDoor.setDirection((byte)direction);
            if(world.getBlock(x - b0, y, z - b1) == PrivatizerMod.privateDoor && (world.getBlockMetadata(x - b0, y, z - b1) & 1) == 0)
            {
                TileEntity adjacentTe = world.getTileEntity(x - b0, y, z - b1);
                if(adjacentTe instanceof IDoorTile)
                {
                    IDoorTile adjacentTileDoor = (IDoorTile)adjacentTe;
                    if(!adjacentTileDoor.isDoubleDoor())
                    {
                        tileDoor.setDoubleDoor(true);
                    }
                }
            }
            if(te instanceof TileEntityPrivateDoor)
            {
                ((TileEntityPrivateDoor)tileDoor).setOwner(player.getGameProfile());
            }
            else if(te instanceof TileEntityPasswordDoor)
            {
                if(stack.hasTagCompound())
                {
                    NBTTagList list = stack.getTagCompound().getTagList("privatizer", Constants.NBT.TAG_STRING);
                    ((TileEntityPasswordDoor)te).setPassword(list.getStringTagAt(1));
                }
            }
        }
        world.markBlockForUpdate(x, y, z);
        world.notifyBlocksOfNeighborChange(x, y, z, block);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
    }
}
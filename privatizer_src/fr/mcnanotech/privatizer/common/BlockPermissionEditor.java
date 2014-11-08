package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPermissionEditor extends Block
{
    protected BlockPermissionEditor(Material material)
    {
        super(material);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(!player.isSneaking())
        {
            if(world.isRemote)
            {
                return true;
            }
            player.openGui(PrivatizerMod.instance, 6, world, x, y, z);
            return true;
        }
        return false;
    }

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityPermissionEditor();
    }
}
package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import fr.mcnanotech.privatizer.client.GuiPasswordChest;
import fr.mcnanotech.privatizer.client.GuiPasswordFurnace;
import fr.mcnanotech.privatizer.client.GuiPrivateChest;
import fr.mcnanotech.privatizer.client.GuiPrivateEditor;
import fr.mcnanotech.privatizer.client.GuiPrivateFurnace;

public class PrivatizerGuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(ID == 0 && te instanceof TileEntityPrivateFurnace)
        {
            return new ContainerPrivateFurnace((TileEntityPrivateFurnace)te, player.inventory);
        }
        else if(ID == 2 && te instanceof TileEntityPasswordFurnace)
        {
            return new ContainerPasswordFurnace((TileEntityPasswordFurnace)te, player.inventory);
        }
        else if(ID == 3 && te instanceof TileEntityPrivateChest)
        {
            return new ContainerPrivateChest((TileEntityPrivateChest)te, player.inventory);
        }
        else if(ID == 4 && te instanceof TileEntityPasswordChest)
        {
            return new ContainerPasswordChest((TileEntityPasswordChest)te, player.inventory);
        }
        else if(ID == 6 && te instanceof TileEntityPermissionEditor)
        {
            return new ContainerPrivateEditor((TileEntityPermissionEditor)te, player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if(ID == 0 && te instanceof TileEntityPrivateFurnace)
        {
            return new GuiPrivateFurnace((TileEntityPrivateFurnace)te, player.inventory);
        }
        else if(ID == 2 && te instanceof TileEntityPasswordFurnace)
        {
            return new GuiPasswordFurnace((TileEntityPasswordFurnace)te, player.inventory);
        }
        else if(ID == 3 && te instanceof TileEntityPrivateChest)
        {
            return new GuiPrivateChest((TileEntityPrivateChest)te, player.inventory);
        }
        else if(ID == 4 && te instanceof TileEntityPasswordChest)
        {
            return new GuiPasswordChest((TileEntityPasswordChest)te, player.inventory);
        }
        else if(ID == 6 && te instanceof TileEntityPermissionEditor)
        {
            return new GuiPrivateEditor((TileEntityPermissionEditor)te, player.inventory);
        }
        return null;
    }
}
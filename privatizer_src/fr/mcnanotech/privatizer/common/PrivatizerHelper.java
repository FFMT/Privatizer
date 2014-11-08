package fr.mcnanotech.privatizer.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.Constants;

import com.google.common.base.Splitter;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;

public class PrivatizerHelper
{
    public static final Splitter newLineSplitter = Splitter.on("\\n");

    public static boolean isOwner(EntityPlayer player, GameProfile owner)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(owner != null && (player.getGameProfile().equals(owner) || (PrivatizerMod.opCanRemoveBlock && server.getConfigurationManager().func_152596_g(player.getGameProfile()))))
        {
            return true;
        }
        return false;
    }

    public static boolean checkPassword(EntityPlayer player, String password)
    {
        if(password == null || password.isEmpty())
        {
            return true;
        }
        if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().hasTagCompound())
        {
            NBTTagList list = player.getCurrentEquippedItem().getTagCompound().getTagList("privatizer", Constants.NBT.TAG_STRING);
            if(list.getStringTagAt(1).equals(password))
            {
                return true;
            }
        }
        return false;
    }
}
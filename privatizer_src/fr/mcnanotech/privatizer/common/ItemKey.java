package fr.mcnanotech.privatizer.common;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemKey extends Item
{
    private String[] subItem = new String[] {"key", "bunchOfKeys", "passPaper", "padlock"};
    private String[] subTexture = new String[] {"key", "bunch_of_keys", "pass_paper", "padlock"};
    public IIcon[] icons = new IIcon[subTexture.length];

    public ItemKey()
    {
        this.setHasSubtypes(true);
    }

    public IIcon getIconFromDamage(int metadata)
    {
        if(metadata >= 0 && metadata <= this.icons.length)
        {
            return this.icons[metadata];
        }
        return this.icons[0];
    }

    public void registerIcons(IIconRegister iconRegister)
    {
        for(int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(PrivatizerMod.MODID + ":" + this.subTexture[i]);
        }
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int metadata = stack.getItemDamage();
        if(metadata < 0 || metadata >= this.subItem.length)
        {
            metadata = 0;
        }
        return "item." + this.subItem[metadata];
    }

    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for(int i = 0; i < this.subItem.length; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
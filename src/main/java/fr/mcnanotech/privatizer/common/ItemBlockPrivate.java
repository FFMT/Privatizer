package fr.mcnanotech.privatizer.common;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockPrivate extends ItemBlock
{
	public ItemBlockPrivate(Block block)
	{
		super(block);
	}
	
    public int getMetadata(int metadata)
    {
        return metadata;
    }
    
    public String getUnlocalizedName(ItemStack stack)
    {
    	if(stack.getItemDamage() >= 0 && stack.getItemDamage() <= BlockPrivate.type.length)
    	{
    		return field_150939_a.getUnlocalizedName() + "." + BlockPrivate.type[stack.getItemDamage()];
    	}
        return field_150939_a.getUnlocalizedName();
    }
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedItemTooltips)
    {
    	if(stack.getItemDamage() == 2)
    	{
    		String[] desc = StatCollector.translateToLocal("tile.private.adaptable.desc").split("/");
    		for(String desPart : desc)
    		{
    			list.add(desPart);
    		}
    	}
    	if(stack.getItemDamage() >= 3 && stack.getItemDamage() <= 11)
    	{
    		list.add("In dev, comming soon !");
    	}
    }
}
package fr.mcnanotech.privatizer.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockPrivate extends ItemBlock
{
	public ItemBlockPrivate(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
	}

	public int getMetadata(int metadata)
	{
		return metadata;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		if(stack.getItemDamage() >= 0 && stack.getItemDamage() <= BlockPrivate.subBlock.length)
		{
			return field_150939_a.getUnlocalizedName() + "." + BlockPrivate.subBlock[stack.getItemDamage()];
		}
		return field_150939_a.getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedItemTooltips)
	{
		if(stack.getItemDamage() == 2)
		{
			list.addAll(PrivatizerHelper.newLineSplitter.splitToList(StatCollector.translateToLocal("tile.private.adaptable.desc")));
		}
		if(stack.getItemDamage() >= 4 && stack.getItemDamage() <= 11)
		{
			list.add("In dev, comming soon !");
		}
	}
}
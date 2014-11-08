package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPrivateChest extends ItemBlock
{
    public ItemBlockPrivateChest(Block block)
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
        if(stack.getItemDamage() >= 0 && stack.getItemDamage() <= BlockPrivateChest.subBlock.length)
        {
            return field_150939_a.getUnlocalizedName() + "." + BlockPrivateChest.subBlock[stack.getItemDamage()];
        }
        return field_150939_a.getUnlocalizedName();
    }
}

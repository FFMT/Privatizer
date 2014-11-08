package fr.mcnanotech.privatizer.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IAdaptableTile
{
    public void setStack(ItemStack stack);

    public Block getBlockForTexture();

    public int getBlockMetadataForTexture();
}
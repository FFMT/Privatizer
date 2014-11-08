package fr.mcnanotech.privatizer.common;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerFilter implements IEntitySelector
{
    @Override
    public boolean isEntityApplicable(Entity entity)
    {
        return entity instanceof EntityPlayer;
    }
}
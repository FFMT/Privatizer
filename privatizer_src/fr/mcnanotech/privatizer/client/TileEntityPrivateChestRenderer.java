package fr.mcnanotech.privatizer.client;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.privatizer.common.TileEntityPrivateChest;
import fr.minecraftforgefrance.ffmtlibs.client.renderer.TileEntityInventorySpecialRenderer;

public class TileEntityPrivateChestRenderer extends TileEntityInventorySpecialRenderer
{
	private final ResourceLocation texture = new ResourceLocation("privatizer", "textures/models/blocks/private_chest.png");
	private final ModelPrivateChest model = new ModelPrivateChest();
	
	@Override
	public void renderInventory(double x, double y, double z)
	{
		this.renderPrivateChestAt(null, x, y, z, 0);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float tick)
	{
		this.renderPrivateChestAt((TileEntityPrivateChest)tile, x, y, z, tick);
	}

	public void renderPrivateChestAt(TileEntityPrivateChest chest, double x, double y, double z, float tick)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		this.bindTexture(this.texture);
		if(chest != null)
		{
			// TODO direction
			//GL11.glRotatef(90F * chest.getDirection(), 0.0F, 1.0F, 0.0F);
			float angle = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * tick;
			angle = 1.0F - angle;
			angle = 1.0F - angle * angle * angle;
			this.model.door.rotateAngleY = +(angle * (float)Math.PI / 2.0F);
			this.model.grip.rotateAngleY = +(angle * (float)Math.PI / 2.0F);
		}
		else
		{
			GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		}
		this.model.renderAll();
		GL11.glPopMatrix();
	}
}
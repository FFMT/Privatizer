package fr.mcnanotech.privatizer.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPrivateChest extends ModelBase
{
	ModelRenderer bottom = new ModelRenderer(this, 0, 28).setTextureSize(64, 64);
	ModelRenderer left = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
	ModelRenderer right = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
	ModelRenderer back = new ModelRenderer(this, 31, 0).setTextureSize(64, 64);
	ModelRenderer top = new ModelRenderer(this, 0, 28).setTextureSize(64, 64);
	ModelRenderer door = new ModelRenderer(this, 31, 13).setTextureSize(64, 64);
	ModelRenderer grip = new ModelRenderer(this, 58, 13).setTextureSize(64, 64);

	public ModelPrivateChest()
	{
		this.bottom.addBox(0F, 0F, 0F, 14, 1, 14);
		this.bottom.setRotationPoint(-7F, 23F, -7F);
		this.left.addBox(0F, 0F, 0F, 1, 13, 14);
		this.left.setRotationPoint(6F, 10F, -7F);
		this.right.addBox(0F, 0F, 0F, 1, 13, 14);
		this.right.setRotationPoint(-7F, 10F, -7F);
		this.back.addBox(0F, 0F, 0F, 12, 13, 1);
		this.back.setRotationPoint(-6F, 10F, 6F);
		this.top.addBox(0F, 0F, 0F, 14, 1, 14);
		this.top.setRotationPoint(-7F, 9F, -7F);
		this.door.addBox(0F, -6F, 0F, 12, 13, 1);
		this.door.setRotationPoint(-6F, 16F, -6F);
		this.grip.addBox(10F, -1F, -1F, 1, 3, 1);
		this.grip.setRotationPoint(-6F, 16F, -6F);
	}
	
	public void renderAll()
	{
		this.bottom.render(0.0625F);
		this.left.render(0.0625F);
		this.right.render(0.0625F);
		this.back.render(0.0625F);
		this.top.render(0.0625F);
		this.door.render(0.0625F);
		this.grip.render(0.0625F);
	}
}
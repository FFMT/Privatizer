package fr.mcnanotech.privatizer.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelPrivateChest extends ModelBase
{
	ModelRenderer bottom = new ModelRenderer(this, 0, 31).setTextureSize(64, 64);
	ModelRenderer left = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
	ModelRenderer right = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
	ModelRenderer back = new ModelRenderer(this, 31, 0).setTextureSize(64, 64);
	ModelRenderer top = new ModelRenderer(this, 0, 31).setTextureSize(64, 64);
	ModelRenderer door = new ModelRenderer(this, 31, 15).setTextureSize(64, 64);
	ModelRenderer grip = new ModelRenderer(this, 58, 16).setTextureSize(64, 64);

	public ModelPrivateChest()
	{
		this.bottom.addBox(0F, 0F, 0F, 14, 1, 14);
		this.bottom.setRotationPoint(-7F, 23F, -7F);
		this.left.addBox(0F, 0F, 0F, 1, 14, 14);
		this.left.setRotationPoint(6F, 9F, -7F);
		this.right.addBox(0F, 0F, 0F, 1, 14, 14);
		this.right.setRotationPoint(-7F, 9F, -7F);
		this.back.addBox(0F, 0F, 0F, 12, 14, 1);
		this.back.setRotationPoint(-6F, 9F, 6F);
		this.top.addBox(0F, 0F, 0F, 14, 1, 14);
		this.top.setRotationPoint(-7F, 8F, -7F);
		this.door.addBox(0F, -6F, 0F, 12, 14, 1);
		this.door.setRotationPoint(-6F, 15F, -6F);
		this.grip.addBox(9F, -1F, -1F, 2, 4, 1);
		this.grip.setRotationPoint(-6F, 15F, -6F);
	}

	public void renderAll()
	{
		bottom.render(0.0625F);
		left.render(0.0625F);
		right.render(0.0625F);
		back.render(0.0625F);
		top.render(0.0625F);
		door.render(0.0625F);
		grip.render(0.0625F);
	}
}
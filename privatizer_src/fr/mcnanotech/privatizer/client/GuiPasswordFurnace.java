package fr.mcnanotech.privatizer.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.privatizer.common.ContainerPasswordFurnace;
import fr.mcnanotech.privatizer.common.TileEntityPasswordFurnace;

public class GuiPasswordFurnace extends GuiContainer
{
    private static final ResourceLocation texture = new ResourceLocation("textures/gui/container/furnace.png");
    private TileEntityPasswordFurnace tilePasswordFurnace;

    public GuiPasswordFurnace(TileEntityPasswordFurnace te, InventoryPlayer inventory)
    {
        super(new ContainerPasswordFurnace(te, inventory));
        this.tilePasswordFurnace = te;
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        String s = this.tilePasswordFurnace.hasCustomInventoryName() ? this.tilePasswordFurnace.getInventoryName() : I18n.format(this.tilePasswordFurnace.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float tick, int mouseX, int mouseZ)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if(this.tilePasswordFurnace.isBurning())
        {
            i1 = this.tilePasswordFurnace.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = this.tilePasswordFurnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
    }
}
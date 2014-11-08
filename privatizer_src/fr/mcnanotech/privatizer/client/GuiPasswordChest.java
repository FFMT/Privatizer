package fr.mcnanotech.privatizer.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.mcnanotech.privatizer.common.ContainerPasswordChest;
import fr.mcnanotech.privatizer.common.TileEntityPasswordChest;

public class GuiPasswordChest extends GuiContainer
{
    private final TileEntityPasswordChest passwordChest;
    private final InventoryPlayer inventoryPlayer;
    private final ResourceLocation texture = new ResourceLocation("textures/gui/container/generic_54.png");

    public GuiPasswordChest(TileEntityPasswordChest tile, InventoryPlayer inventory)
    {
        super(new ContainerPasswordChest(tile, inventory));
        this.passwordChest = tile;
        this.inventoryPlayer = inventory;
        this.allowUserInput = false;
        this.ySize = 219;
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        this.fontRendererObj.drawString(I18n.format(this.inventoryPlayer.getInventoryName()), 8, this.ySize - 92, 4210752);
        this.fontRendererObj.drawString(this.passwordChest.hasCustomInventoryName() ? this.passwordChest.getInventoryName() : I18n.format(this.passwordChest.getInventoryName()), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float tick, int mouseX, int mouseZ)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, 6 * 18 + 17);
        this.drawTexturedModalRect(k, l + 6 * 18 + 17, 0, 126, this.xSize, 96);
    }
}
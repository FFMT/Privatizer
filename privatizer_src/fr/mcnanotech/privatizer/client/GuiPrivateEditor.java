package fr.mcnanotech.privatizer.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.mcnanotech.privatizer.common.ContainerPrivateEditor;
import fr.mcnanotech.privatizer.common.PrivatizerMod;
import fr.mcnanotech.privatizer.common.TileEntityPermissionEditor;
import fr.mcnanotech.privatizer.common.packet.PacketEditor;

public class GuiPrivateEditor extends GuiContainer
{
    private final TileEntityPermissionEditor privateEditor;
    private final ContainerPrivateEditor containerEditor;
    private final InventoryPlayer inventoryPlayer;
    private final ResourceLocation texture = new ResourceLocation(PrivatizerMod.MODID, "textures/gui/container/editor.png");
    private GuiTextField textField;
    private GuiButton confirmButton;

    public GuiPrivateEditor(TileEntityPermissionEditor tile, InventoryPlayer inventory)
    {
        super(new ContainerPrivateEditor(tile, inventory));
        this.privateEditor = tile;
        this.inventoryPlayer = inventory;
        this.containerEditor = (ContainerPrivateEditor)this.inventorySlots;
    }

    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.textField = new GuiTextField(this.fontRendererObj, i + 10, j + 24, 103, 12);
        this.textField.setTextColor(-1);
        this.textField.setDisabledTextColour(0);
        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setMaxStringLength(40);
        this.textField.setEnabled(true);
        this.confirmButton = new GuiButton(0, i + 120, j + 20, 40, 20, "Confirm");

        this.buttonList.add(confirmButton);
    }

    public void drawScreen(int x, int y, float partialRenderTick)
    {
        super.drawScreen(x, y, partialRenderTick);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        this.textField.drawTextBox();
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        this.textField.mouseClicked(x, y, button);
    }

    protected void keyTyped(char c, int id)
    {
        if(this.textField.textboxKeyTyped(c, id))
        {

        }
        else
        {
            super.keyTyped(c, id);
        }
    }

    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        this.fontRendererObj.drawString(I18n.format(this.inventoryPlayer.getInventoryName()), 8, this.ySize - 92, 4210752);
        this.fontRendererObj.drawString(this.privateEditor.hasCustomInventoryName() ? this.privateEditor.getInventoryName() : I18n.format(this.privateEditor.getInventoryName()), 8, 6, 4210752);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float tick, int mouseX, int mouseZ)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(k + 7, l + 20, 0, this.ySize + (this.containerEditor.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
    }

    protected void actionPerformed(GuiButton guiButton)
    {
        if(guiButton.id == 0)
        {
            PrivatizerMod.packetManager.sendToServer(new PacketEditor(this.textField.getText()));
            this.textField.writeText("");
        }
    }
}
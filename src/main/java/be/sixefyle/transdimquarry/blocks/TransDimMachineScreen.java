package be.sixefyle.transdimquarry.blocks;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.gui.EnergyBar;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.Vec2i;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class TransDimMachineScreen<T extends TransDimMachineMenu<?>> extends AbstractContainerScreen<T> {

    protected ResourceLocation texture;
    protected EnergyBar energyBar;

    protected Vec2i titlePos;
    protected Vec2i inventoryTitlePos;
    protected Vec2i texturePos;

    protected int textureRes = 256;

    public TransDimMachineScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        titlePos = new Vec2i(0, 0);
        inventoryTitlePos = new Vec2i(0, 0);
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        texturePos = new Vec2i(x, y);

        energyBar = new EnergyBar(
                new Vec2i(x + 158, y + 9),
                new Vec2i(imageWidth, 0),
                new Vec2i(10, 80),
                textureRes,
                false);
    }

    protected void setTexture(String textureName){
        texture = new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/" + textureName + ".png");
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, texture);

        guiGraphics.blit(texture, texturePos.x, texturePos.y, 0, 0, imageWidth, imageHeight, textureRes, textureRes);
        energyBar.renderProgressEnergyBar(guiGraphics, texture, menu.getScaledEnergy());
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //Draw titles
        guiGraphics.drawString(this.font, this.title, titlePos.x, titlePos.y, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, inventoryTitlePos.x, inventoryTitlePos.y, 4210752, false);

        energyBar.renderAreaTooltips(guiGraphics, mouseX, mouseY, x, y, menu);
    }

    protected boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

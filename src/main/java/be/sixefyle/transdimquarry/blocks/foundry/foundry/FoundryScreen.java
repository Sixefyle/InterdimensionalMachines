package be.sixefyle.transdimquarry.blocks.foundry.foundry;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FoundryScreen extends AbstractContainerScreen<FoundryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/foundry.png");

    public FoundryScreen(FoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;
    }

    int[][] smeltingBar = new int[][] {
            {65, 37},
            {110, 37}
    };

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressEnergyBar(guiGraphics, x, y);

        for (int i = 0; i < menu.blockEntity.getCookTime().length; i++) {
            renderProgressSmeltingBar(guiGraphics, x, y, i);
        }
    }

    private void renderProgressEnergyBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x + 158, y + 9, imageWidth, 0, 10, (int) (menu.getScaledEnergy() * 80), 256, 256);
    }
    private void renderProgressSmeltingBar(GuiGraphics guiGraphics, int x, int y, int index){
        guiGraphics.blit(TEXTURE, x + smeltingBar[index][0], y + smeltingBar[index][1], imageWidth + 10, 0, 5, (int) (menu.getScaledSmelting(index) * 23), 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //Draw titles
        guiGraphics.drawString(this.font, this.title, 7, 5, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 90, 4210752, false);

        renderEnergyAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 158, 9, 10, 80)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(NumberUtil.formatToEnergy(menu.getEnergyStored())+"/"+NumberUtil.formatToEnergy(menu.getMaxEnergyStored())), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

package be.sixefyle.transdimquarry.blocks.quarry;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.quarry.TransdimQuarryMenu;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.SwitchQuarryStatePacket;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.IEnergyStorage;

public class TransdimQuarryScreen extends AbstractContainerScreen<TransdimQuarryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/transdimensional_quarry.png");

    public TransdimQuarryScreen(TransdimQuarryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        imageHeight = 214;
        imageWidth = 176;

        addRenderableWidget(new Button.Builder(Component.literal("Start"), (button) -> {
            PacketSender.sendToServer(new SwitchQuarryStatePacket(menu.getBlockEntity().getBlockPos()));
            button.setMessage(Component.literal(menu.isWorking() ? "Start" : "Pause"));
        })      .size(54, 20)
                .pos(width / 2 - 73, height / 2 - 10)
                .build());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressEnergyBar(guiGraphics, x, y);
    }

    private void renderProgressEnergyBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x + 92, y + 69, imageWidth, 13, (int) (menu.getScaledEnergy() * 77), 10);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.drawString(this.font, this.title, 7, -20, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 96, 4210752, false);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.5f,.5f,.5f);
        guiGraphics.drawString(this.font, "Progression: " + String.format("%.0f", menu.getScaledProgress() * 100) + "%", 190, 120, 0x00FF00, false);
        guiGraphics.drawString(this.font, "Tick Needed: " + menu.getTimeToMine(), 190, 132, 0x00FF00, false);
        guiGraphics.drawString(this.font, "Power Usage: " + NumberUtil.format(menu.getPowerConsumption()) + " FE/t", 190, 144, 0x00FF00, false);
        guiGraphics.drawString(this.font, "Energy Multiplier: " + menu.getEnergyCostMultiplier() + "% ", 190, 158, 0x00FF00, false);

        if(menu.isSilkTouch()){
            guiGraphics.drawString(this.font, "Silk Touch Activated", 190, 170, 0x9370DB, false);
        }
        else if(menu.getFortuneLevel() > 0){
            guiGraphics.drawString(this.font, "Fortune Power: " + menu.getFortuneLevel(), 190, 170, 0xFFD700, false);
        }
        guiGraphics.pose().popPose();

        renderEnergyAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        IEnergyStorage energyStorage = menu.blockEntity.getEnergyStorage();
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 92, 69, 77, 10)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(NumberUtil.format(energyStorage.getEnergyStored())+"/"+NumberUtil.format(energyStorage.getMaxEnergyStored())+" FE"), pMouseX, pMouseY);
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

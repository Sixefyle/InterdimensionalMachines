package be.sixefyle.transdimquarry.blocks.toolinfuser;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.SetMaxEnergyInputPacket;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.IEnergyStorage;

public class TransdimToolInfuserScreen extends AbstractContainerScreen<TransdimToolInfuserMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/transdimensional_tool_infuser.png");

    EditBox energyInput;

    public TransdimToolInfuserScreen(TransdimToolInfuserMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        imageHeight = 151;
        imageWidth = 176;

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInput = new EditBox(this.font, x + 43, y + 45, 60, 10, Component.literal("max_energy_input"));

        addRenderableWidget(energyInput);
        addRenderableWidget(new Button.Builder(Component.literal("Apply"), (button) -> {
            try {
                PacketSender.sendToServer(new SetMaxEnergyInputPacket(
                        menu.getBlockEntity().getBlockPos(), Integer.parseInt(energyInput.getValue())));
            } catch (Exception ignore){

            }
        })      .size(30, 10)
                .pos(x + 104, y + 45)
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
        renderProgressEnergyBar(guiGraphics, x + 42, y + 15);
        renderProgressInfusingBar(guiGraphics, x + 42, y + 26);
        renderProgressInfusedBar(guiGraphics, x + 118, y + 30);
    }

    private void renderProgressEnergyBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x, y, imageWidth, 0, (int) (menu.getScaledEnergy() * 72), 11);
    }

    private void renderProgressInfusingBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x, y, imageWidth, 11, (int) (menu.getScaledInfusingEnergy() * 70), 2);
    }

    private void renderProgressInfusedBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x, y, imageWidth, 13, (int) (menu.getScaledInfusedEnergy() * 18), 4);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.drawString(this.font, this.title, 7, 11, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 67, 4210752, false);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.5f,.5f,.5f);

        guiGraphics.drawString(this.font, String.format("%s added per infuse.", NumberUtil.format(Math.min(menu.getNeededEnergy(), menu.blockEntity.getEnergyStorage().getMaxEnergyStored()))), 86, 95, 0xffffff, true);

        guiGraphics.pose().popPose();

        renderEnergyAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        IEnergyStorage energyStorage = menu.blockEntity.getEnergyStorage();
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 42, 15, 72, 11)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(NumberUtil.format(energyStorage.getEnergyStored())+"/"+NumberUtil.format(energyStorage.getMaxEnergyStored())), pMouseX - x, pMouseY - y);
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

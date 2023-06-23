package be.sixefyle.transdimquarry.screen;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.SwitchQuarryStatePacket;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;
import java.util.Optional;

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

        addRenderableWidget(new Button(width / 2 - 73, height / 2 - 10,
                54, 20, Component.literal(menu.isWorking() ? "Start" : "Pause"), (button) -> {
            PacketSender.sendToServer(new SwitchQuarryStatePacket(menu.getBlockEntity().getBlockPos()));
            button.setMessage(Component.literal(menu.isWorking() ? "Start" : "Pause"));
        }));
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressEnergyBar(poseStack, x, y);
    }

    private void renderProgressEnergyBar(PoseStack poseStack, int x, int y){
        blit(poseStack, x + 92, y + 69, imageWidth, 13, (int) (menu.getScaledEnergy() * 77), 10);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.font.draw(poseStack, this.title, 7, -20, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, 7, 96, 4210752);

        poseStack.scale(.5f,.5f,.5f);
        this.font.draw(poseStack, "Progression: " + String.format("%.0f", menu.getScaledProgress() * 100) + "%", 190, 120, 0x00FF00);
        this.font.draw(poseStack, "Tick Needed: " + menu.getTimeToMine(), 190, 132, 0x00FF00);
        this.font.draw(poseStack, "Power Usage: " + NumberUtil.format(menu.getPowerConsumption()) + " FE/t", 190, 144, 0x00FF00);
        this.font.draw(poseStack, "Energy Multiplier: " + menu.getEnergyCostMultiplier() + "% ", 190, 158, 0x00FF00);

        if(menu.isSilkTouch()){
            this.font.draw(poseStack, "Silk Touch Activated", 190, 170, 0x9370DB);
        }
        else if(menu.getFortuneLevel() > 0){
            this.font.draw(poseStack, "Fortune Power: " + menu.getFortuneLevel(), 190, 170, 0xFFD700);
        }
        poseStack.popPose();

        renderEnergyAreaTooltips(poseStack, mouseX, mouseY, x, y);
    }

    private void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        IEnergyStorage energyStorage = menu.blockEntity.getEnergyStorage();
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 92, 69, 77, 10)) {
            renderTooltip(pPoseStack, List.of(
                    Component.literal(NumberUtil.format(energyStorage.getEnergyStored())+"/"+NumberUtil.format(energyStorage.getMaxEnergyStored())+" FE")),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        renderTooltip(poseStack, mouseX, mouseY);
    }

}

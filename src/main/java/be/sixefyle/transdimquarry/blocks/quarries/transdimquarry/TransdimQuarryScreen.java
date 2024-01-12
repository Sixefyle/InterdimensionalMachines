package be.sixefyle.transdimquarry.blocks.quarries.transdimquarry;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.items.quarryupgrades.QuarryUpgrade;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.SwitchQuarryStatePacket;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class TransdimQuarryScreen extends AbstractContainerScreen<TransdimQuarryMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/transdimensional_quarry512.png");

    Button switchButton;
    Map<String, Integer> blackScreenLabels = new LinkedHashMap<>();

    public TransdimQuarryScreen(TransdimQuarryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 234;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();

        switchButton = new Button.Builder(Component.literal("Start"), (button) -> {
            PacketSender.sendToServer(new SwitchQuarryStatePacket(menu.getBlockEntity().getBlockPos()));
        }).size(54, 20)
          .pos(width / 2 - 75, height / 2 - 10)
          .build();

        addRenderableWidget(switchButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 512, 512);
        renderProgressEnergyBar(guiGraphics, x, y);
    }

    private void renderProgressEnergyBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x + 75, y + 71, imageWidth, 0, (int) (menu.getScaledEnergy() * 94), 10, 512, 512);
    }

    String[] dots = {"", ".", "..", "..."};
    int dotIndex = 0;
    int ticksToWait = 30;
    int lastTick = ticksToWait;

    /**
     * Show error message with 5 message max
     * @param guiGraphics
     * @param messages
     */
    private void showErrorMessage(GuiGraphics guiGraphics, String... messages){
        if(messages.length > 5) return;

        if(lastTick >= ticksToWait){
            dotIndex = (dotIndex + 1) % (dots.length);
            lastTick = 0;
        }
        lastTick++;

        int baseY = 124;
        guiGraphics.drawString(this.font, "ERROR" + dots[dotIndex], 112, baseY, 0xff2020, false);

        for (int i = messages.length; i > 0; i--) {
            guiGraphics.drawString(this.font, messages[i-1], 112, baseY + (i * 10), 0xff2020, false);
        }
    }

    private void addBlackScreenLabel(String text, Integer hexColor){
        blackScreenLabels.put(text, hexColor);
    }

    private void showBlackScreenLabels(GuiGraphics guiGraphics){
        int i = 0;
        int hexColor;
        for (String text : blackScreenLabels.keySet()){
            hexColor = blackScreenLabels.get(text);
            guiGraphics.drawString(this.font, text, 112, 124 + (i * 10), hexColor, false);
            i++;
        }
    }

    private void clearRegisteredLabels(){
        blackScreenLabels.clear();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        switchButton.setMessage(Component.literal(menu.isWorking() ? "Stop" : "Start"));
        switchButton.setFGColor(menu.isWorking() ? TextColor.parseColor(EnumColor.RED.getHexColor()).getValue() : TextColor.parseColor(EnumColor.TEAL.getHexColor()).getValue());

        //Draw titles
        guiGraphics.drawString(this.font, this.title, 7, 6, 4210752, false);
        guiGraphics.drawString(this.font, "Upgrades", 16, 72, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 142, 4210752, false);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.7f,.7f,.7f);

        clearRegisteredLabels();
        if(menu.isWorking() && menu.isInventoryFull()) {
            showErrorMessage(guiGraphics, "Inventory full!","", "Tips: You can attach a", "container to the quarry");
        } else if(menu.isWorking() && menu.getEnergyStored() < menu.getPowerConsumption()){
            showErrorMessage(guiGraphics, "Not enough energy!", String.format("Need %s to work", NumberUtil.formatToEnergy(menu.getEnergyNeeded())));
        } else {
            addBlackScreenLabel("Progression: " + String.format("%.0f", menu.getScaledProgress() * 100) + "%", 0xfffbe7);
            addBlackScreenLabel("Time to mine: " + menu.getTimeToMine() + " ticks", 0xfffbe7);

            boolean costTooHigh = menu.getPowerConsumption() > menu.getMaxEnergyStored();
            addBlackScreenLabel(String.format("Energy need: %s", NumberUtil.formatToEnergy(menu.getPowerConsumption())), costTooHigh ? 0xff0000 : 0xfffbe7);
            addBlackScreenLabel(String.format("Energy cap.: %s", NumberUtil.formatToEnergy(menu.getMaxEnergyStored())), 0xfffbe7);

            if(menu.isSilkTouch()){
                addBlackScreenLabel("Silk Touch activated", 0x9370DB);
            }
            else if(menu.getFortuneLevel() > 0){
                addBlackScreenLabel("Fortune power: " + menu.getFortuneLevel(), 0xFFD700);
            }
        }
        showBlackScreenLabels(guiGraphics);
        guiGraphics.pose().popPose();

        renderEnergyAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
        renderUpgradeAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderUpgradeAreaTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y) {
        ItemStack carriedItem = Minecraft.getInstance().player.containerMenu.getCarried();
        if(carriedItem.getItem() instanceof QuarryUpgrade upgrade){
            boolean canPlaceMultiple = upgrade.canPlaceMultiple();
            boolean canPlace = menu.blockEntity.canPlaceUpgrade(carriedItem);
            if(!canPlaceMultiple && !canPlace){
                if(isMouseAboveArea(mouseX, mouseY, x, y, 13, 81, 54, 18)) {
                    guiGraphics.renderTooltip(this.font, EnumColor.RED.getColoredComponent("You can only place cosmic upgrade once!"), mouseX - x, mouseY - y);
                }
            }
        }
    }

    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 75, 71, 94, 10)) {
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

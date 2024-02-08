package be.sixefyle.transdimquarry.blocks.quarries.transdimquarry;

import be.sixefyle.transdimquarry.blocks.TransDimMachineScreen;
import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.gui.EnergyBar;
import be.sixefyle.transdimquarry.items.quarryupgrades.QuarryUpgrade;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.SwitchQuarryStatePacket;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TransdimQuarryScreen extends TransDimMachineScreen<TransdimQuarryMenu> {
    Button switchButton;

    public TransdimQuarryScreen(TransdimQuarryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        textureRes = 512;

        imageHeight = 234;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();

        energyBar = new EnergyBar(
                new Vec2i(texturePos.x + 75, texturePos.y + 71),
                new Vec2i(imageWidth, 0),
                new Vec2i(94, 10),
                textureRes,
                true
        );

        switchButton = new Button.Builder(Component.literal("Start"), (button) -> {
            PacketSender.sendToServer(new SwitchQuarryStatePacket(menu.blockEntity.getBlockPos()));
        }).size(54, 20)
          .pos(width / 2 - 75, height / 2 - 10)
          .build();

        addRenderableWidget(switchButton);
    }

    @Override
    protected String getTextureName() {
        return "transdimensional_quarry512";
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = texturePos.x;
        int y = texturePos.y;

        switchButton.setMessage(Component.literal(menu.isWorking() ? "Stop" : "Start"));
        switchButton.setFGColor(menu.isWorking() ? TextColor.parseColor(EnumColor.RED.getHexColor()).getValue() : TextColor.parseColor(EnumColor.TEAL.getHexColor()).getValue());

        //Draw titles
        guiGraphics.drawString(this.font, "Upgrades", 16, 72, 4210752, false);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.7f,.7f,.7f);

        clearRegisteredBlackScreenLabels();
        if(menu.isWorking() && menu.isInventoryFull()) {
            showErrorMessage(guiGraphics, "Inventory full!","", "Tips: You can attach a", "container to the quarry");
        } else if(menu.isWorking() && menu.getEnergyStored() < menu.getPowerConsumption()){
            showErrorMessage(guiGraphics, "Not enough energy!", String.format("Need %s to work", NumberUtil.formatToEnergy(menu.getPowerConsumption())));
        } else {
            addBlackScreenLabel("Progression: " + String.format("%.0f", menu.getScaledProgression() * 100) + "%", 0xfffbe7);
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

        renderUpgradeAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
        energyBar.renderAreaTooltips(guiGraphics, mouseX, mouseY, x, y, menu);
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
}

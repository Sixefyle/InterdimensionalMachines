package be.sixefyle.transdimquarry.items.tools.screen;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.items.tools.TransdimExcavator;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.ChangeToolModePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class TransdimExcavatorScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/transdimensional_tool_infuser.png");

    ForgeSlider digSpeed;
    ForgeSlider digWidth;
    ForgeSlider digHeight;

    TransdimExcavator excavator;
    ItemStack itemStack;

    public TransdimExcavatorScreen(ItemStack itemStack) {
        super(Component.literal("Transdim Excavator Gui"));
        this.itemStack = itemStack;
        this.excavator = (TransdimExcavator) itemStack.getItem();
    }

    @Override
    protected void init() {
        super.init();
        renderButtons();
        renderEditBoxs();
    }

    public void apply(){
        try{
            int speed, width, height;
            speed = digSpeed.getValueInt();
            width = digWidth.getValueInt();
            height = digHeight.getValueInt();
            PacketSender.sendToServer(new ChangeToolModePacket(speed, width, height));
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Settings applied!"));
        } catch (NumberFormatException e){
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Only numbers are accepted!"));
        }
    }

    public void renderButtons(){
        Button applyButton = Button.builder(Component.literal("Apply"), button -> {
                    apply();
                })
                .size(100, 20)
                .pos(this.width / 2 - 50, this.height / 2 + 40)
                .build();

        addRenderableWidget(applyButton);
    }

    public void renderEditBoxs(){
        int centerWidth = this.width / 2;
        int centerHeight = this.height / 2;
        int editBoxWidth = 100;
        int editBoxHeight = 12;
        int editBoxCenterWidth = centerWidth - editBoxWidth / 2;
        int editBoxCenterHeight = centerHeight - editBoxHeight / 2;

        digSpeed = new ForgeSlider(editBoxCenterWidth, editBoxCenterHeight - 20, editBoxWidth, editBoxHeight,
                Component.literal("Speed: "), Component.empty(), 1, excavator.getMaxMiningSpeed(itemStack),
                excavator.getMineSpeed(itemStack), 1, 1, true);
        digWidth = new ForgeSlider(editBoxCenterWidth, editBoxCenterHeight + 2, editBoxWidth, editBoxHeight,
                Component.literal("Width: "), Component.empty(), 1, excavator.getMaxMineWidth(itemStack),
                excavator.getMineWidth(itemStack), 1, 1, true);
        digHeight = new ForgeSlider(editBoxCenterWidth, editBoxCenterHeight + 24, editBoxWidth, editBoxHeight,
                Component.literal("Height: "), Component.empty(), 1, excavator.getMaxMineHeight(itemStack),
                excavator.getMineHeight(itemStack), 1, 1, true);


        addRenderableWidget(digSpeed);
        addRenderableWidget(digWidth);
        addRenderableWidget(digHeight);
    }

    public void renderLabels(GuiGraphics guiGraphics){
        int centerWidth = this.width / 2;
        int centerHeight = this.height / 2;
        int editBoxWidth = 100;
        int editBoxHeight = 10;
        int editBoxCenterWidth = centerWidth - editBoxWidth / 2;
        int editBoxCenterHeight = centerHeight - editBoxHeight / 2;

        guiGraphics.drawString(this.font, String.format("Dig Speed (Max %d)", excavator.getMaxMiningSpeed(itemStack)),
                editBoxCenterWidth, editBoxCenterHeight - 30, 0xffffff, true);
        guiGraphics.drawString(this.font, String.format("Dig Width (Max %d)", excavator.getMaxMineWidth(itemStack)),
                editBoxCenterWidth, editBoxCenterHeight - 8, 0xffffff, true);
        guiGraphics.drawString(this.font, String.format("Dig height (Max %d)", excavator.getMaxMineHeight(itemStack)),
                editBoxCenterWidth, editBoxCenterHeight + 14, 0xffffff, true);

        double perc = digSpeed.getValue() / excavator.getMaxMiningSpeed(itemStack);
        int perBlockEnergyCost = excavator.getMineEnergyCost(itemStack, perc);
        int maxEnergyCost = perBlockEnergyCost * digWidth.getValueInt() * digHeight.getValueInt();

        guiGraphics.drawString(this.font, String.format("Power Per Block: %d FE", perBlockEnergyCost),
                editBoxCenterWidth + 102, editBoxCenterHeight - 19, 0xe05a2d, true);

        guiGraphics.drawCenteredString(this.font, String.format("Max Power Usage: %d FE", maxEnergyCost),
                centerWidth, centerHeight + 61, 0xf55318);
    }

    @Override
    public void onClose() {
        super.onClose();
        apply();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        renderLabels(guiGraphics);
        guiGraphics.drawCenteredString(this.font, "Trans-dimensional Excavator Setting", this.width / 2, this.height / 2 - 55, 0xfffff);
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

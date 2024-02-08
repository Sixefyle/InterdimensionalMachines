package be.sixefyle.transdimquarry.blocks.foundry;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.TransDimMachineScreen;
import be.sixefyle.transdimquarry.gui.Widgets;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.cts.SwitchAutoSplitPacket;
import be.sixefyle.transdimquarry.utils.Vec2i;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class BaseFoundryScreen<T extends BaseFoundryMenu> extends TransDimMachineScreen<T> {
    protected int[][] smeltingBar = new int[][] {
            {65, 37},
            {110, 37}
    };
    protected final ResourceLocation WIDGETS = new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/icons/widgets.png");

    private ImageButton splitButton;
    private boolean oldAutoSplitState;

    public BaseFoundryScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;

        titlePos = new Vec2i(7, 5);
        inventoryTitlePos = new Vec2i(7, 90);
    }

    @Override
    protected void init() {
        super.init();

        // x, y, width, height, widget pos x, widget pos y, y offset, resource, on press
        splitButton = Widgets.SPLIT.getImageButton(new Vec2i(texturePos.x - 20, texturePos.y + 8), new Vec2i(oldAutoSplitState ? 122 : 82, oldAutoSplitState ? -20 : 20), pButton -> {
            PacketSender.sendToServer(new SwitchAutoSplitPacket(menu.blockEntity.getBlockPos()));
            rebuildWidgets();
        });
//        splitButton = new ImageButton(texturePos.x - 20, texturePos.y + 8, 20, 20, 0, oldAutoSplitState ? 122 : 82, oldAutoSplitState ? -20 : 20, WIDGETS, button -> {
//            PacketSender.sendToServer(new SwitchAutoSplitPacket(menu.blockEntity.getBlockPos()));
//            rebuildWidgets();
//        });
        splitButton.setTooltip(Tooltip.create(Component.literal(String.format("Auto Split: %s", menu.isAutoSplit() ? "Enable" : "Disable"))));

        addRenderableWidget(splitButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        boolean isAutoSplitEnable = menu.isAutoSplit();
        if(oldAutoSplitState != isAutoSplitEnable){
            oldAutoSplitState = isAutoSplitEnable;
            rebuildWidgets();
        }

        RenderSystem.setShaderTexture(0, WIDGETS);
        guiGraphics.blit(WIDGETS, texturePos.x - 25, texturePos.y + 3, 20, 82, 28, 30, 256, 256);

        for (int i = 0; i < menu.blockEntity.getCookTime().length; i++) {
            renderProgressSmeltingBar(guiGraphics, texturePos.x, texturePos.y, i);
        }
    }



    private void renderProgressSmeltingBar(GuiGraphics guiGraphics, int x, int y, int index){
        guiGraphics.blit(texture, x + smeltingBar[index][0], y + smeltingBar[index][1], imageWidth + 10, 0, 5, (int) (menu.getScaledSmelting(index) * 23), 256, 256);
    }
}

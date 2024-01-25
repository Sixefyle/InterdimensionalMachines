package be.sixefyle.transdimquarry.blocks.foundry;

import be.sixefyle.transdimquarry.blocks.TransDimMachineScreen;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BaseFoundryScreen<T extends BaseFoundryMenu> extends TransDimMachineScreen<T> {
    protected int[][] smeltingBar = new int[][] {
            {65, 37},
            {110, 37}
    };


    public BaseFoundryScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        setTexture("foundry");

        imageHeight = 182;
        imageWidth = 176;

        titlePos = new Vec2i(7, 5);
        inventoryTitlePos = new Vec2i(7, 90);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        for (int i = 0; i < menu.blockEntity.getCookTime().length; i++) {
            renderProgressSmeltingBar(guiGraphics, texturePos.x, texturePos.y, i);
        }
    }

    private void renderProgressSmeltingBar(GuiGraphics guiGraphics, int x, int y, int index){
        guiGraphics.blit(texture, x + smeltingBar[index][0], y + smeltingBar[index][1], imageWidth + 10, 0, 5, (int) (menu.getScaledSmelting(index) * 23), 256, 256);
    }
}

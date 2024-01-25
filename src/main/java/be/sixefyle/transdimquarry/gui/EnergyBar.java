package be.sixefyle.transdimquarry.gui;

import be.sixefyle.transdimquarry.blocks.TransDimMachineMenu;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record EnergyBar(Vec2i position, Vec2i offset, Vec2i size, int textureRes, boolean isHorizontal) {

    public void renderProgressEnergyBar(GuiGraphics guiGraphics, ResourceLocation texture, double scale) {
        if (isHorizontal) {
            guiGraphics.blit(texture, position.x, position.y, offset.x, offset.y, (int) (scale * size.x), size.y, textureRes, textureRes);
        } else {
            guiGraphics.blit(texture, position.x, position.y, offset.x, offset.y, size.x, (int) (scale * size.y), textureRes, textureRes);
        }
    }

    public void renderAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y, TransDimMachineMenu<?> menu) {
        if (isMouseAbove(pMouseX, pMouseY)) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font,
                    Component.literal(NumberUtil.formatToEnergy(menu.getEnergyStored()) + "/" + NumberUtil.formatToEnergy(menu.getMaxEnergyStored())), pMouseX - x, pMouseY - y);
        }
    }

    public boolean isMouseAbove(int pMouseX, int pMouseY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, position.x, position.y, size.x, size.y);
    }
}

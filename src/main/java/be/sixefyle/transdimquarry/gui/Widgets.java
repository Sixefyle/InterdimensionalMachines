package be.sixefyle.transdimquarry.gui;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public enum Widgets {
    SPLIT(new Vec2i(20,20), new Vec2i(0, 82)),
    EMPTY_SLOT(new Vec2i(18,18), new Vec2i(22, 0)),
    ;

    private final Vec2i imageSize;
    private final Vec2i imageOffset;

    Widgets(Vec2i imageSize, Vec2i imageOffset) {
        this.imageSize = imageSize;
        this.imageOffset = imageOffset;
    }

    private static final ResourceLocation WIDGETS = new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/icons/widgets.png");

    public void showImage(GuiGraphics guiGraphics, Vec2i location){
        guiGraphics.blit(WIDGETS, location.x, location.y, imageOffset.x, imageOffset.y, imageSize.x, imageSize.y, 256, 256);
    }

    public ImageButton getImageButton(Vec2i location, Vec2i hoverOffset, Button.OnPress onPress){
        return new ImageButton(location.x, location.y, imageSize.x, imageSize.y, 0, hoverOffset.x, hoverOffset.y, WIDGETS, onPress);
    }

    public ImageButton getImageButton(Vec2i location, Button.OnPress onPress){
        return new ImageButton(location.x, location.y, imageSize.x, imageSize.y, imageSize.y, imageOffset.x, imageOffset.y, WIDGETS, onPress);
    }
}

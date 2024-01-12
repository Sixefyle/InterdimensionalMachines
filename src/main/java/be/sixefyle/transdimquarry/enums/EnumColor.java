package be.sixefyle.transdimquarry.enums;

import be.sixefyle.transdimquarry.utils.ComponentUtil;
import net.minecraft.network.chat.MutableComponent;

public enum EnumColor {
    TEAL("#2fdaed"),
    CYAN("#1095a3"),
    GRAY("#c3bfc7"),
    DARK_GRAY("#49484a"),
    BLUE("#1A5F7A"),
    PURPLE("#ac49fc"),
    YELLOW("#fae714"),
    RED("#f51b1b"),
    GREEN("#48d136"),
    WHITE("#ebeced"),
    ;


    private String hexColor;

    EnumColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getHexColor() {
        return hexColor;
    }

    public MutableComponent getColoredComponent(String text){
        return ComponentUtil.coloredComponent(text, this.getHexColor());
    }
}

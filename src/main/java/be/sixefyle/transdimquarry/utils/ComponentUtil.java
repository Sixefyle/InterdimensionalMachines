package be.sixefyle.transdimquarry.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

public class ComponentUtil {
    public static MutableComponent coloredComponent(MutableComponent component, String hexColor) {
        return component.setStyle(component.getStyle()
                .withColor(TextColor.parseColor(hexColor)));
    }

    public static MutableComponent coloredComponent(String text, String hexColor) {
        MutableComponent component = Component.literal(text);
        return component.setStyle(component.getStyle()
                .withColor(TextColor.parseColor(hexColor)));
    }
}

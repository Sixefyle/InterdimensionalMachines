package be.sixefyle.transdimquarry.items.tools;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IModeHandle {
    void onChangeMode(ItemStack itemStack, Object... params);

    Screen getScreen(ItemStack itemStack);
}

package be.sixefyle.transdimquarry.items.tools;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;

public interface IModeHandle {
    void onChangeMode(ItemStack itemStack, Object... params);
    @OnlyIn(Dist.CLIENT)
    Screen getScreen(ItemStack itemStack);
}

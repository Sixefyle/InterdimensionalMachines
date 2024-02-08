package be.sixefyle.transdimquarry.blocks.foundry.advancedfoundry;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryMenu;
import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryScreen;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedFoundryScreen extends BaseFoundryScreen<AdvancedFoundryMenu> {

    public AdvancedFoundryScreen(AdvancedFoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;

        smeltingBar = new int[][] {
                {30, 36},
                {65, 36},
                {100, 36},
                {134, 36}
        };
    }

    @Override
    protected String getTextureName() {
        return "advanced_foundry";
    }
}

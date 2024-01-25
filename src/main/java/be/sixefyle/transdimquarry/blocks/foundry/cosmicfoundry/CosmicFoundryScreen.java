package be.sixefyle.transdimquarry.blocks.foundry.cosmicfoundry;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
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

public class CosmicFoundryScreen extends BaseFoundryScreen<CosmicFoundryMenu> {
    public CosmicFoundryScreen(CosmicFoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;

        setTexture("cosmic_foundry");

        smeltingBar = new int[][] {
                {24, 36},
                {46, 36},
                {68, 36},
                {90, 36},
                {112, 36},
                {134, 36}
        };
    }
}

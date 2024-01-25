package be.sixefyle.transdimquarry.blocks.foundry.foundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryMenu;
import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FoundryScreen extends BaseFoundryScreen<BaseFoundryMenu> {
    public FoundryScreen(BaseFoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;

        setTexture("foundry");

        smeltingBar = new int[][] {
                {65, 37},
                {110, 37}
        };
    }
}

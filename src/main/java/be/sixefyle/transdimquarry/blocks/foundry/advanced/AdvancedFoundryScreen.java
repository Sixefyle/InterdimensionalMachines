package be.sixefyle.transdimquarry.blocks.foundry.advanced;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryScreen;
import net.minecraft.network.chat.Component;
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

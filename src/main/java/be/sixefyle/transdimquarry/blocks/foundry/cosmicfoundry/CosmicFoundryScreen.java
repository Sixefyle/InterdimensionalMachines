package be.sixefyle.transdimquarry.blocks.foundry.cosmicfoundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CosmicFoundryScreen extends BaseFoundryScreen<CosmicFoundryMenu> {
    public CosmicFoundryScreen(CosmicFoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;

        smeltingBar = new int[][] {
                {24, 36},
                {46, 36},
                {68, 36},
                {90, 36},
                {112, 36},
                {134, 36}
        };
    }

    @Override
    protected String getTextureName() {
        return "cosmic_foundry";
    }
}

package be.sixefyle.transdimquarry.blocks.foundry.ethereal;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EtherealFoundryScreen extends BaseFoundryScreen<EtherealFoundryMenu> {
    public EtherealFoundryScreen(EtherealFoundryMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 182;
        imageWidth = 176;

        smeltingBar = new int[][] {
                {12, 36},
                {31, 36},
                {50, 36},
                {69, 36},
                {88, 36},
                {107, 36},
                {126, 36},
                {145, 36}
        };
    }

    @Override
    protected String getTextureName() {
        return "ethereal_foundry";
    }
}

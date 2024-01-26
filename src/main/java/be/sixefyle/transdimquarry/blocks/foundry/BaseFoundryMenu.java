package be.sixefyle.transdimquarry.blocks.foundry;

import be.sixefyle.transdimquarry.blocks.TransDimMachineMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;

public abstract class BaseFoundryMenu extends TransDimMachineMenu<BaseFoundry> {

    public static final int CONTAINER_SIZE = 4;

    public BaseFoundryMenu(MenuType<?> menuType, int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(menuType, id, inv, entity, data);

        playerInventoryPos = new Vec2(9,100);
        playerToolBarPos = new Vec2(9,158);

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    public double getScaledSmelting(int slot){
        return (double) blockEntity.getCookTime()[slot] / blockEntity.getMaxProgress();
    }

    public boolean isAutoSplit(){
        return data.get(3) > 0;
    }
}

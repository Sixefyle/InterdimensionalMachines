package be.sixefyle.transdimquarry.blocks.foundry.foundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryMenu;
import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserBlockEntity;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.MenuRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class FoundryMenu extends BaseFoundryMenu {
    public FoundryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(CONTAINER_SIZE));
    }

    public FoundryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.FOUNDRY.get(), id, inv, entity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 1, 60, 18));
            addSlot(new SlotItemHandler(handler, 3, 104, 18));

            addSlot(new SlotItemHandler(handler, 0, 60, 62));
            addSlot(new SlotItemHandler(handler, 2, 104, 62));
        });
    }

    @Override
    public Block getBlock() {
        return BlockRegister.FOUNDRY.get();
    }
}

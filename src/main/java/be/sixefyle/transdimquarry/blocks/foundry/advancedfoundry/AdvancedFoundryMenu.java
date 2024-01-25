package be.sixefyle.transdimquarry.blocks.foundry.advancedfoundry;

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

public class AdvancedFoundryMenu extends BaseFoundryMenu {
    public AdvancedFoundryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public AdvancedFoundryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.ADVANCED_FOUNDRY.get(), id, inv, entity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 1, 24, 17));
            addSlot(new SlotItemHandler(handler, 3, 60, 17));
            addSlot(new SlotItemHandler(handler, 5, 94, 17));
            addSlot(new SlotItemHandler(handler, 7, 128, 17));


            addSlot(new SlotItemHandler(handler, 0, 24, 62));
            addSlot(new SlotItemHandler(handler, 2, 60, 62));
            addSlot(new SlotItemHandler(handler, 4, 94, 62));
            addSlot(new SlotItemHandler(handler, 6, 128, 62));
        });

    }

    @Override
    public Block getBlock() {
        return BlockRegister.ADVANCED_FOUNDRY.get();
    }
}

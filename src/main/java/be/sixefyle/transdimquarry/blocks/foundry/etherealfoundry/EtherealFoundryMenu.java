package be.sixefyle.transdimquarry.blocks.foundry.etherealfoundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundryMenu;
import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserBlockEntity;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.MenuRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class EtherealFoundryMenu extends BaseFoundryMenu {

    public EtherealFoundryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public EtherealFoundryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.ETHEREAL_FOUNDRY.get(), id, inv, entity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 1, 6, 17));
            addSlot(new SlotItemHandler(handler, 3, 25, 17));
            addSlot(new SlotItemHandler(handler, 5, 44, 17));
            addSlot(new SlotItemHandler(handler, 7, 63, 17));
            addSlot(new SlotItemHandler(handler, 9, 82, 17));
            addSlot(new SlotItemHandler(handler, 11, 101, 17));
            addSlot(new SlotItemHandler(handler, 13, 120, 17));
            addSlot(new SlotItemHandler(handler, 15, 139, 17));


            addSlot(new SlotItemHandler(handler, 0, 6,  62));
            addSlot(new SlotItemHandler(handler, 2, 25, 62));
            addSlot(new SlotItemHandler(handler, 4, 44, 62));
            addSlot(new SlotItemHandler(handler, 6, 63, 62));
            addSlot(new SlotItemHandler(handler, 8, 82, 62));
            addSlot(new SlotItemHandler(handler, 10, 101, 62));
            addSlot(new SlotItemHandler(handler, 12, 120, 62));
            addSlot(new SlotItemHandler(handler, 14, 139, 62));
        });
    }

    @Override
    public Block getBlock() {
        return BlockRegister.ETHEREAL_FOUNDRY.get();
    }
}

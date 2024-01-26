package be.sixefyle.transdimquarry.blocks.foundry.cosmicfoundry;

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

public class CosmicFoundryMenu extends BaseFoundryMenu {
    public CosmicFoundryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(CONTAINER_SIZE));
    }

    public CosmicFoundryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.COSMIC_FOUNDRY.get(), id, inv, entity, data);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 1, 18, 17));
            addSlot(new SlotItemHandler(handler, 3, 40, 17));
            addSlot(new SlotItemHandler(handler, 5, 62, 17));
            addSlot(new SlotItemHandler(handler, 7, 84, 17));
            addSlot(new SlotItemHandler(handler, 9, 106, 17));
            addSlot(new SlotItemHandler(handler, 11, 128, 17));


            addSlot(new SlotItemHandler(handler, 0, 18, 62));
            addSlot(new SlotItemHandler(handler, 2, 40, 62));
            addSlot(new SlotItemHandler(handler, 4, 62, 62));
            addSlot(new SlotItemHandler(handler, 6, 84,  62));
            addSlot(new SlotItemHandler(handler, 8, 106, 62));
            addSlot(new SlotItemHandler(handler, 10, 128, 62));
        });
    }

    @Override
    public Block getBlock() {
        return BlockRegister.COSMIC_FOUNDRY.get();
    }
}

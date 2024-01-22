package be.sixefyle.transdimquarry.blocks.foundry.etherealfoundry;

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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class EtherealFoundryMenu extends AbstractContainerMenu {
    public final EtherealFoundryBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public EtherealFoundryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public EtherealFoundryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.ETHEREAL_FOUNDRY.get(), id);

        blockEntity = (EtherealFoundryBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

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

        addDataSlots(data);
    }

    public long getEnergyStored(){
        return blockEntity.getEnergy();
    }
    public long getMaxEnergyStored(){
        return blockEntity.getEnergyCapacity();
    }
    public long getPowerConsumption(){
        return blockEntity.getNeededEnergy();
    }

    public int getProgression(){
        return data.get(1);
    }
    public double getScaledProgression(){
        return (double) data.get(1) / data.get(2);
    }
    public boolean isWorking() {
        return data.get(0) > 0;
    }
    public double getScaledEnergy(){
        return Math.min((double) getEnergyStored() / getMaxEnergyStored(), 1);
    }

    public double getScaledSmelting(int slot){
        return (double) blockEntity.getCookTime()[slot] / blockEntity.getMaxProgress();
    }


    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 16;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlockRegister.ETHEREAL_FOUNDRY.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 100 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 158));
        }
    }
}

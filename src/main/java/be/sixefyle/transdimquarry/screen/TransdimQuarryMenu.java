package be.sixefyle.transdimquarry.screen;

import be.sixefyle.transdimquarry.BlockRegister;
import be.sixefyle.transdimquarry.MenuRegister;
import be.sixefyle.transdimquarry.blocks.entity.TransdimQuarryBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class TransdimQuarryMenu extends AbstractContainerMenu {
    public final TransdimQuarryBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public TransdimQuarryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(6));
    }

    public TransdimQuarryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.TRANSDIMENSIONAL_QUARRY.get(), id);
        checkContainerSize(inv, TransdimQuarryBlockEntity.CONTAINER_SIZE);
        blockEntity = (TransdimQuarryBlockEntity) entity;
        this.level = inv.player.level;
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            for(int i = 0; i < (blockEntity.getItemSlotsSize()) / 9; ++i) {
                for(int j = 0; j < 9; ++j) {
                    this.addSlot(new SlotItemHandler(handler, i * 9 + j, 8 + j * 18, -10 + i * 18));
                }
            }

            for (int i = 0; i < blockEntity.getUpgradeSlotsSize(); i++) {
                this.addSlot(new SlotItemHandler(handler, blockEntity.getItemSlotsSize() + i, 16 + i * 18, 53));
            }
        });

        addDataSlots(data);
    }

    public TransdimQuarryBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public boolean isWorking() {
        return data.get(0) > 0;
    }

    public int getPowerConsumption(){
        return data.get(2);
    }
    public int getFortuneLevel(){
        return data.get(3);
    }
    public int getEnergyCostMultiplier(){
        return data.get(5);
    }
    public int getProgression(){
        return data.get(0);
    }
    public int getTimeToMine(){
        return data.get(1);
    }
    public boolean isSilkTouch(){
        return data.get(4) > 0;
    }
    public double getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);

        return Math.min((double) progress / maxProgress, 1);
    }

    public double getScaledEnergy(){
        return (double) blockEntity.getEnergyStorage().getEnergyStored() / blockEntity.getEnergyStorage().getMaxEnergyStored();
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
    private static final int TE_INVENTORY_SLOT_COUNT = TransdimQuarryBlockEntity.CONTAINER_SIZE;  // must be the number of slots you have!

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
                player, BlockRegister.TRANSDIMENSIONAL_QUARRY.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 108 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 166));
        }
    }
}

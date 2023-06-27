package be.sixefyle.transdimquarry.blocks.toolinfuser;

import be.sixefyle.transdimquarry.BlockEntityRegister;
import be.sixefyle.transdimquarry.energy.BlockEnergyStorage;
import be.sixefyle.transdimquarry.items.tools.TransdimSword;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class TransdimToolInfuserBlockEntity extends BaseContainerBlockEntity implements MenuProvider {

    public static final int CONTAINER_SIZE = 1;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    private final BlockEnergyStorage energyStorage = new BlockEnergyStorage(10000000, Integer.MAX_VALUE, Integer.MAX_VALUE) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            //PacketSender.sendToClients(new EnergySyncPacket(this.energy, getBlockPos()));
        }
    };
    private int baseEnergyNeeded = 100000;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private boolean isWorking = false;

    protected final ContainerData data;

    public TransdimToolInfuserBlockEntity(BlockPos pos, BlockState state) {

        super(BlockEntityRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TransdimToolInfuserBlockEntity.this.baseEnergyNeeded;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TransdimToolInfuserBlockEntity.this.baseEnergyNeeded = value;
                };
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Trans-dimensional Tool Infuser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TransdimToolInfuserMenu(id, inventory, this, this.data);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        lazyEnergyHandler.invalidate();
    }


    @Override
    public void reviveCaps() {
        super.reviveCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        ContainerHelper.saveAllItems(nbt, this.items);
        nbt.putInt("energy", energyStorage.getEnergyStored());
        nbt.putBoolean("isWorking", isWorking);
    }


    @Override
    public void onLoad() {
        super.onLoad();

        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

        ContainerHelper.loadAllItems(nbt, this.items);
        energyStorage.setEnergy(nbt.getInt("energy"));
        setWorking(nbt.getBoolean("isWorking"));
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void dropInventory(){
        Containers.dropContents(level, worldPosition, getItems());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TransdimToolInfuserBlockEntity blockEntity) {
        if(level.isClientSide) return;
        if(!blockEntity.isWorking) return;

        setChanged(level, pos, state);

        if(blockEntity.hasEnoughEnergy()){
            blockEntity.energyStorage.extractEnergy(blockEntity.baseEnergyNeeded, false);

        }
    }

    private boolean hasEnoughEnergy() {
        return energyStorage.getEnergyStored() >= baseEnergyNeeded;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return itemStack.getItem() instanceof TransdimSword;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.items.set(slot, itemStack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, slot, amount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    public int getBaseEnergyNeeded() {
        return baseEnergyNeeded;
    }

    public void setBaseEnergyNeeded(int baseEnergyNeeded) {
        this.baseEnergyNeeded = baseEnergyNeeded;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean started) {
        isWorking = started;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void setEnergyLevel(int energy) {
        this.energyStorage.setEnergy(energy);
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}

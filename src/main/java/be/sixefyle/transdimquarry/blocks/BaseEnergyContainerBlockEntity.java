package be.sixefyle.transdimquarry.blocks;

import be.sixefyle.transdimquarry.energy.BlockEnergyStorage;
import be.sixefyle.transdimquarry.items.quarryupgrades.QuarryUpgrade;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEnergyContainerBlockEntity extends BaseContainerBlockEntity implements IEnergyHandler, WorldlyContainer, MenuProvider {
    private final int containerSize;
    private final long baseEnergyCapacity;

    private NonNullList<ItemStack> items;

    private final BlockEnergyStorage energyStorage;

    private LazyOptional<BlockEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private long baseEnergyNeeded;
    private long energyNeeded = baseEnergyNeeded;
    private boolean isWorking = false;
    private int progress = 0;
    private int maxProgress;

    private ContainerData baseData;

    public BaseEnergyContainerBlockEntity(BlockEntityType<?> be, BlockPos pos, BlockState state, int containerSize, int energyCapacity) {
        super(be, pos, state);

        this.containerSize = containerSize;
        this.baseEnergyCapacity = energyCapacity;

        items = NonNullList.withSize(containerSize, ItemStack.EMPTY);

        energyStorage = new BlockEnergyStorage(energyCapacity, Integer.MAX_VALUE, Integer.MAX_VALUE);

        baseData = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex){
                    case 0 -> (isWorking() ? 1 : 0);
                    case 1 -> getProgress();
                    case 2 -> getMaxProgress();
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex){
                    case 0 -> setWorking(pValue > 0);
                    case 1 -> setProgress(pValue);
                    case 2 -> setMaxProgress(pValue);
                };
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    protected abstract Component getDefaultName();

    @Override
    protected abstract AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_);

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this,
            Direction.UP, Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if (!this.remove && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            return handlers[0].cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public int[] getSlotsForFace(Direction p_19238_) {
        int[] slots = new int[getItemSlotsSize()];
        for (int i = 0; i < slots.length; i++) {
            slots[i]=i;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot < getContainerSize();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();

        lazyEnergyHandler.invalidate();
    }


    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers =  SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        ContainerHelper.saveAllItems(nbt, this.items);
        nbt.putLong("energy", energyStorage.getEnergyStored());
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
        this.items = NonNullList.withSize(containerSize, ItemStack.EMPTY);

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

    protected List<Container> getAttachedContainers(){
        if(level == null) return null;

        List<Container> containers = new ArrayList<>();
        Container container;
        Block block;
        BlockEntity blockEntity;
        BlockPos blockPos;
        BlockState blockState;
        for (Direction direction : Direction.values()) {
            blockPos = getBlockPos().relative(direction);
            blockState = level.getBlockState(blockPos);
            block = blockState.getBlock();
            blockEntity = level.getBlockEntity(blockPos);

            if(blockState.hasBlockEntity()){
                if(blockEntity instanceof Container && getCapability(ForgeCapabilities.ITEM_HANDLER, direction).isPresent()){
                    container = (Container) blockEntity;
                    if(container instanceof ChestBlockEntity && block instanceof ChestBlock chestBlock){
                        containers.add(ChestBlock.getContainer(chestBlock, blockState, level, blockPos, true));
                    } else {
                        containers.add(container);
                    }
                }
            }
        }
        return containers;
    }

    protected List<ItemStack> tryAddItemToAttachedContainer(List<ItemStack> itemStacks){
        List<Container> attachedContainer = getAttachedContainers();
        List<ItemStack> itemsLeft = itemStacks;
        if(attachedContainer != null){
            for (Container container : attachedContainer) {
                itemsLeft = tryAddItems(itemsLeft, container);
            }
        }
        if(attachedContainer == null || itemsLeft.size() > 0){
            itemsLeft = tryAddItems(itemsLeft, this, getItemSlotsSize());
        }
        return itemsLeft;
    }

    protected List<ItemStack> tryAddItems(List<ItemStack> itemStacks, Container container){
        return tryAddItems(itemStacks, container, container.getContainerSize());
    }

    protected List<ItemStack> tryAddItems(List<ItemStack> itemStacks, Container container, int containerSize){
        List<ItemStack> itemsLeft = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            for (int j = 0; j < containerSize && !itemStack.isEmpty(); ++j) {
                itemStack = tryAddItem(itemStack, container, j);
            }
            if(itemStack.getCount() > 0){
                itemsLeft.add(itemStack);
            } else {
                itemsLeft.remove(itemStack);
            }
        }
        return itemsLeft;
    }

    protected ItemStack tryAddItem(ItemStack itemStack, int slot){
        return tryAddItem(itemStack,this, slot);
    }

    protected ItemStack tryAddItem(ItemStack itemStack, Container container, int slot){
        ItemStack currentItemStack = container.getItem(slot);

        if(slot >= container.getContainerSize() && itemStack.getCount() > 0){
            return itemStack;
        }

        if (currentItemStack.isEmpty()) {
            container.setItem(slot, itemStack);
            itemStack = ItemStack.EMPTY;
        } else if (canMergeItems(currentItemStack, itemStack)) {
            int i = getMaxStackSize() - currentItemStack.getCount();
            int j = Math.min(itemStack.getCount(), i);
            itemStack.shrink(j);
            currentItemStack.grow(j);
        }

        container.setChanged();
        return itemStack;
    }

    protected static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.is(itemStack2.getItem())) {
            return false;
        } else if (itemStack.getDamageValue() != itemStack2.getDamageValue()) {
            return false;
        } else if (itemStack.getCount() > itemStack.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.isSameItemSameTags(itemStack, itemStack2);
        }
    }

    protected boolean hasEnoughEnergy() {
        return energyStorage.getEnergyStored() >= energyNeeded;
    }

    public long getNeededEnergy() {
        return energyNeeded;
    }

    protected void resetProgress() {
        progress = 0;
    }

    public boolean isContainerFull(){
        for (int i = 0; i < getItemSlotsSize(); ++i) {
            if(getItem(i).getItem().equals(Items.AIR)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.items.set(slot, itemStack);
        setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return super.getMaxStackSize();
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return true;
    }

    @Override
    public void startOpen(Player pPlayer) {
        super.startOpen(pPlayer);
    }

    @Override
    public void stopOpen(Player pPlayer) {
        super.stopOpen(pPlayer);
    }

    @Override
    public int getContainerSize() {
        return containerSize;
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

    public int getItemSlotsSize(){
        return containerSize;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public long getBaseEnergyNeeded() {
        return baseEnergyNeeded;
    }

    public void setBaseEnergyNeeded(int baseEnergyNeeded) {
        this.baseEnergyNeeded = baseEnergyNeeded;
        if(this.getNeededEnergy() <= 0) {
            setEnergyNeeded(baseEnergyNeeded);
        }
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean started) {
        isWorking = started;
    }

    public BlockEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void setEnergy(long energy) {
        this.energyStorage.setEnergy(energy);
    }

    @Override
    public void setMaxEnergyInput(long value) {
        baseEnergyNeeded = value;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public long getEnergy() {
        return this.getEnergyStorage().getLongEnergyStored();
    }

    public void setEnergyNeeded(long energyNeeded) {
        this.energyNeeded = energyNeeded;
    }

    public LazyOptional<BlockEnergyStorage> getLazyEnergyHandler() {
        return lazyEnergyHandler;
    }

    public void setLazyEnergyHandler(LazyOptional<BlockEnergyStorage> lazyEnergyHandler) {
        this.lazyEnergyHandler = lazyEnergyHandler;
    }

    public LazyOptional<? extends IItemHandler>[] getHandlers() {
        return handlers;
    }

    public void setHandlers(LazyOptional<? extends IItemHandler>[] handlers) {
        this.handlers = handlers;
    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }


    public void setEnergyCapacity(long energyCapacity) {
        this.getEnergyStorage().setCapacity(energyCapacity);
        if(getEnergy() > getEnergyCapacity()) {
            this.getEnergyStorage().setEnergy(energyCapacity);
        }
    }

    public long getEnergyCapacity() {
        return getEnergyStorage().getLongMaxEnergyStored();
    }

    public long getBaseEnergyCapacity() {
        return baseEnergyCapacity;
    }

    public ContainerData getBaseData() {
        return baseData;
    }

    public void setBaseData(ContainerData baseData) {
        this.baseData = baseData;
    }
}

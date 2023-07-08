package be.sixefyle.transdimquarry.blocks.toolinfuser;

import be.sixefyle.transdimquarry.BlockEntityRegister;
import be.sixefyle.transdimquarry.blocks.IEnergyHandler;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.energy.BlockEnergyStorage;
import be.sixefyle.transdimquarry.items.tools.InfusedTool;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
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

public class TransdimToolInfuserBlockEntity extends BaseContainerBlockEntity implements MenuProvider, IEnergyHandler {

    public static final int CONTAINER_SIZE = 1;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    private final BlockEnergyStorage energyStorage = new BlockEnergyStorage(CommonConfig.TOOL_INFUSER_ENERGY_CAPACITY.get(), Integer.MAX_VALUE, Integer.MAX_VALUE) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            PacketSender.sendToClients(new EnergySyncPacket(this.energy, getBlockPos()));
        }
    };
    private int baseEnergyNeeded = 100000;
    private int progress = 0;
    private int maxProgress = CommonConfig.TOOL_INFUSER_MAX_PROGRESS.get();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    protected final ContainerData data;

    public TransdimToolInfuserBlockEntity(BlockPos pos, BlockState state) {

        super(BlockEntityRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TransdimToolInfuserBlockEntity.this.baseEnergyNeeded;
                    case 1 -> TransdimToolInfuserBlockEntity.this.progress;
                    case 2 -> TransdimToolInfuserBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TransdimToolInfuserBlockEntity.this.baseEnergyNeeded = value;
                    case 1 -> TransdimToolInfuserBlockEntity.this.progress = value;
                    case 2 -> TransdimToolInfuserBlockEntity.this.maxProgress = value;
                };
            }

            @Override
            public int getCount() {
                return 3;
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
        nbt.putInt("energyNeeded", baseEnergyNeeded);
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
        baseEnergyNeeded = nbt.getInt("energyNeeded");
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void dropInventory(){
        Containers.dropContents(level, worldPosition, getItems());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TransdimToolInfuserBlockEntity blockEntity) {
        if(level.isClientSide) return;

        if(!pos.equals(blockEntity.getBlockPos())) return;

        setChanged(level, pos, state);

//        if(blockEntity.getEnergyStorage().getEnergyStored() <= 1){
//            blockEntity.getEnergyStorage().receiveEnergy(1000000000, false);
//        }

        int energyCost = blockEntity.getEnergyCost();
        ItemStack itemStack = blockEntity.items.get(0);
        if(!itemStack.isEmpty() && energyCost > 0){
            boolean isItemMaxed = itemStack.hasTag() && itemStack.getTag().getBoolean("is_maxed");
            if(!isItemMaxed && ++blockEntity.progress >= blockEntity.maxProgress){
                blockEntity.energyStorage.extractEnergy(energyCost, false);

                if(!itemStack.isEmpty() && itemStack.getItem() instanceof InfusedTool tool) {
                    if(!itemStack.hasTag())
                        itemStack.setTag(new CompoundTag());

                    tool.addInfusedEnergy(itemStack, energyCost);
                }

                blockEntity.progress = 0;
                blockEntity.maxProgress = Math.max(CommonConfig.TOOL_INFUSER_MIN_PROGRESS.get(), blockEntity.maxProgress - 5);
            }
        } else {
            blockEntity.progress = 0;
            blockEntity.maxProgress = CommonConfig.TOOL_INFUSER_MAX_PROGRESS.get();
        }
    }

    private boolean hasEnoughEnergy() {
        return energyStorage.getEnergyStored() >= baseEnergyNeeded;
    }

    private int getEnergyCost(){
        return Math.min(energyStorage.getEnergyStored(), baseEnergyNeeded);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return itemStack.getItem() instanceof InfusedTool;
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

    @Override
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public int getEnergy() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public void setEnergy(int value) {
        this.energyStorage.setEnergy(value);
    }

    @Override
    public void setMaxEnergyInput(int value) {
        baseEnergyNeeded = value;
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}

package be.sixefyle.transdimquarry.blocks.entity;

import be.sixefyle.transdimquarry.BlockEntityRegister;
import be.sixefyle.transdimquarry.items.QuarryFortuneUpgradeItem;
import be.sixefyle.transdimquarry.items.QuarrySilkTouchUpgradeItem;
import be.sixefyle.transdimquarry.items.QuarryUpgrade;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
import be.sixefyle.transdimquarry.screen.TransdimQuarryMenu;
import be.sixefyle.transdimquarry.utils.OreUtils;
import be.sixefyle.transdimquarry.utils.TDQEnergyStorage;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TransdimQuarryBlockEntity extends BaseContainerBlockEntity implements MenuProvider, WorldlyContainer {

    public static final int CONTAINER_SIZE = 30;
    public static final int[] UPGRADE_SLOTS = {27,28,29};

    private final Map<Integer, ItemStack> upgrades = new HashMap<>(){{
        for (int upgradeSlot : UPGRADE_SLOTS) {
            put(upgradeSlot, ItemStack.EMPTY);
        }
    }};

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    private final TDQEnergyStorage energyStorage = new TDQEnergyStorage(100000000, Integer.MAX_VALUE, Integer.MAX_VALUE) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            PacketSender.sendToClients(new EnergySyncPacket(this.energy, getBlockPos()));
        }
    };
    private int baseEnergyNeeded = 32500;
    private int energyNeeded = baseEnergyNeeded;
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private boolean isWorking = false;

    protected final ContainerData data;
    private int progress = 0;
    private int timeToMine = 10;
    private double oreFindChange = .07;
    private boolean isSilkTouch = false;
    private int fortuneLevel = 0;
    private ItemStack tool;
    private ItemStack silkTool;
    private ItemStack fortuneTool;
    private double energyCostMultiplier = 1.0;

    public TransdimQuarryBlockEntity(BlockPos pos, BlockState state) {

        super(BlockEntityRegister.TRANSDIMENSIONAL_QUARRY.get(), pos, state);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TransdimQuarryBlockEntity.this.progress;
                    case 1 -> TransdimQuarryBlockEntity.this.timeToMine;
                    case 2 -> TransdimQuarryBlockEntity.this.energyNeeded;
                    case 3 -> TransdimQuarryBlockEntity.this.fortuneLevel;
                    case 4 -> TransdimQuarryBlockEntity.this.isSilkTouch ? 1 : 0;
                    case 5 -> (int) (TransdimQuarryBlockEntity.this.energyCostMultiplier * 100);
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TransdimQuarryBlockEntity.this.progress = value;
                    case 1 -> TransdimQuarryBlockEntity.this.timeToMine = value;
                    case 2 -> TransdimQuarryBlockEntity.this.energyNeeded = value;
                    case 3 -> TransdimQuarryBlockEntity.this.fortuneLevel = value;
                    case 4 -> TransdimQuarryBlockEntity.this.isSilkTouch = value > 0;
                    case 5 -> TransdimQuarryBlockEntity.this.energyCostMultiplier = value;
                };
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Trans-dimensional Quarry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TransdimQuarryMenu(id, inventory, this, this.data);
    }

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

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
    public boolean canPlaceItemThroughFace(int p_19235_, ItemStack p_19236_, @Nullable Direction p_19237_) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack p_19240_, Direction p_19241_) {
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
        nbt.putInt("energy", energyStorage.getEnergyStored());
        nbt.putBoolean("isWorking", isWorking);

//        if(currentBlockPos != null){
//            nbt.putLong("currentPos", currentBlockPos.asLong());
//        }
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

        ItemStack itemStack;
        for (int upgradeSlot : UPGRADE_SLOTS) {
            itemStack = items.get(upgradeSlot);
            if(itemStack.getItem() instanceof QuarryUpgrade upgrade){
                upgrades.replace(upgradeSlot, itemStack.copy());
                upgrade.onPlaced(this);
                energyNeeded = (int) (baseEnergyNeeded * energyCostMultiplier);
            }
        }
        setWorking(nbt.getBoolean("isWorking"));
//        currentBlockPos = BlockPos.of(nbt.getLong("currentPos"));
//        currentY = currentBlockPos.getY();
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public void dropInventory(){
        Containers.dropContents(level, worldPosition, getItems());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TransdimQuarryBlockEntity blockEntity) {
        if(level.isClientSide) return;
        if(!blockEntity.isWorking) return;

        setChanged(level, pos, state);

        if(!blockEntity.isContainerFull() && blockEntity.hasEnoughEnergy()){
            blockEntity.energyStorage.extractEnergy((int) (blockEntity.energyNeeded * blockEntity.energyCostMultiplier), false);
            if(++blockEntity.progress >= blockEntity.timeToMine){
                blockEntity.virtuallyMineNextBlock();
            }
        }
    }

    private static List<Map<Block, Double>> NOT_ORE_LIST = new ArrayList<>(){{
        add(new HashMap<>() {{
            put(Blocks.STONE, .5);
            put(Blocks.DEEPSLATE, .3);
            put(Blocks.SAND, .03);
            put(Blocks.GRAVEL, .07);
            put(Blocks.DIORITE, .05);
            put(Blocks.ANDESITE, .05);
        }});
        add(new HashMap<>() {{
            put(Blocks.NETHERRACK, .9);
            put(Blocks.GRAVEL, .06);
            put(Blocks.SOUL_SAND, .03);
            put(Blocks.SOUL_SOIL, .01);
        }});
        add(new HashMap<>() {{
            put(Blocks.END_STONE, 1.0);
        }});
    }};

    public static Block getRandomBlock(Random random) {
        Map<Block, Double> blockChances = NOT_ORE_LIST.get(random.nextInt(NOT_ORE_LIST.size()));
        if (blockChances != null) {
            double totalWeight = blockChances.values().stream().mapToDouble(Double::doubleValue).sum();
            double randomValue = random.nextDouble() * totalWeight;
            for (Map.Entry<Block, Double> entry : blockChances.entrySet()) {
                randomValue -= entry.getValue();
                if (randomValue <= 0) {
                    return entry.getKey();
                }
            }
        }
        return Blocks.STONE;
    }

    private void virtuallyMineNextBlock() {
        if(level != null){
            boolean isOre;
            BlockState generatedBlockState;
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                isOre = random.nextDouble() <= oreFindChange;
                if(!isOre){
                    generatedBlockState = getRandomBlock(random).defaultBlockState();
                } else {
                    generatedBlockState = OreUtils.getOres().get(random.nextInt(OreUtils.getOres().size())).defaultBlockState();
                }

                LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.TOOL, getHarvesterTool())
                        .withParameter(LootContextParams.ORIGIN, new Vec3(0, 0, 0))
                        .withOptionalParameter(LootContextParams.BLOCK_STATE, generatedBlockState);
                if (fortuneLevel > 0) {
                    context.withLuck(fortuneLevel);
                }

                List<ItemStack> blockDrops = generatedBlockState.getDrops(context);
                int containerSize = getContainerSize();
                for (ItemStack itemStack : blockDrops) {
                    for (int j = 0; j < containerSize && !itemStack.isEmpty(); ++j) {
                        itemStack = tryAddItem(itemStack, j);
                    }
                }

                if(!isContainerFull()) {
                    resetProgress();
                }
            }
        }
    }

    private ItemStack tryAddItem(ItemStack itemStack, int slot){
        ItemStack currentItemStack = items.get(slot);

        if(slot >= getItemSlotsSize() && itemStack.getCount() > 0){
            return itemStack;
        }

        if (currentItemStack.isEmpty()) {
            setItem(slot, itemStack);
            itemStack = ItemStack.EMPTY;
        } else if (canMergeItems(currentItemStack, itemStack)) {
            int i = getMaxStackSize() - currentItemStack.getCount();
            int j = Math.min(itemStack.getCount(), i);
            itemStack.shrink(j);
            currentItemStack.grow(j);
        }

        setChanged();
        return itemStack;
    }

    private static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
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

    private boolean hasEnoughEnergy() {
        return energyStorage.getEnergyStored() >= energyNeeded;
    }

    private void resetProgress() {
        progress = 0;
    }
    private ItemStack getHarvesterTool() {
        if (isSilkTouch) {
            if (silkTool == null || silkTool.isEmpty()) {
                silkTool = new ItemStack(Items.DIAMOND_PICKAXE);
                silkTool.enchant(Enchantments.SILK_TOUCH, 1);
            }
            return silkTool;
        } else if (fortuneLevel > 0) {
            if (fortuneTool == null || fortuneTool.isEmpty()) {
                fortuneTool = new ItemStack(Items.DIAMOND_PICKAXE);
                fortuneTool.enchant(Enchantments.BLOCK_FORTUNE, fortuneLevel);
            }
            return fortuneTool;
        } else {
            if (tool == null || tool.isEmpty()) {
                tool = new ItemStack(Items.DIAMOND_PICKAXE);
            }
            return tool;
        }
    }

    private boolean isContainerFull(){
        for (int i = 0; i < getItemSlotsSize(); ++i) {
            if(items.get(i).getItem().equals(Items.AIR)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if(slot < UPGRADE_SLOTS[0]) return false;

        if(slot > UPGRADE_SLOTS[0]){
            Object itemClass = itemStack.getItem().getClass();
            if(isSilkTouch && (itemClass.equals(QuarryFortuneUpgradeItem.class) || itemClass.equals(QuarrySilkTouchUpgradeItem.class))){
                return false;
            } else if(fortuneLevel > 0 && itemClass.equals(QuarrySilkTouchUpgradeItem.class)) {
                return false;
            }
        }

        return itemStack.getItem() instanceof QuarryUpgrade;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.items.set(slot, itemStack);

        if(slot >= UPGRADE_SLOTS[0]){
            if(!upgrades.get(slot).isEmpty() && upgrades.get(slot).getItem() instanceof QuarryUpgrade upgrade){
                upgrade.onRemoved(this);

                if (itemStack.isEmpty()){
                    upgrades.replace(slot, ItemStack.EMPTY);
                }
            }

            if(!itemStack.isEmpty() && itemStack.getItem() instanceof QuarryUpgrade upgrade) {
                upgrade.onPlaced(this);
                upgrades.replace(slot, itemStack.copy());
            }

            energyNeeded = (int) (baseEnergyNeeded * energyCostMultiplier);
        }

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

    public int getItemSlotsSize(){
        return CONTAINER_SIZE - UPGRADE_SLOTS.length;
    }

    public int getUpgradeSlotsSize(){
        return UPGRADE_SLOTS.length;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTimeToMine() {
        return timeToMine;
    }

    public void setTimeToMine(int timeToMine) {
        this.timeToMine = Math.max(1, timeToMine);
    }

    public int getFortuneLevel() {
        return fortuneLevel;
    }

    public void setFortuneLevel(int fortuneLevel) {
        this.fortuneLevel = fortuneLevel;
    }

    public boolean isSilkTouch() {
        return isSilkTouch;
    }

    public void setSilkTouch(boolean silkTouch) {
        isSilkTouch = silkTouch;
    }

    public double getEnergyCostMultiplier() {
        return energyCostMultiplier;
    }

    public void setEnergyCostMultiplier(double energyCostMultiplier) {
        this.energyCostMultiplier = energyCostMultiplier;
    }

    public double getOreFindChange() {
        return oreFindChange;
    }

    public void setOreFindChange(double oreFindChange) {
        this.oreFindChange = oreFindChange;
    }

    public int getBaseEnergyNeeded() {
        return baseEnergyNeeded;
    }

    public void setBaseEnergyNeeded(int baseEnergyNeeded) {
        this.baseEnergyNeeded = baseEnergyNeeded;
    }

    public Map<Integer, ItemStack> getUpgrades() {
        return upgrades;
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

package be.sixefyle.transdimquarry.blocks.quarries;

import be.sixefyle.transdimquarry.blocks.BaseEnergyContainerBlockEntity;
import be.sixefyle.transdimquarry.items.quarryupgrades.QuarryUpgrade;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
import be.sixefyle.transdimquarry.utils.LevelUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class QuarryBaseBlockEntity extends BaseEnergyContainerBlockEntity {
    private final int[] upgradeSlots;

    private final Map<Integer, ItemStack> upgrades;
    protected double timeToMine;
    protected int baseTimeToMine;
    protected double oreFindChange;
    private boolean isMining = false;
    private boolean isSilkTouch = false;
    private int fortuneLevel = 0;
    private ItemStack tool;
    private ItemStack silkTool;
    private ItemStack fortuneTool;

    private List<ItemStack> waitingItems = new ArrayList<>();
    FakePlayer fakePlayer;

    public QuarryBaseBlockEntity(BlockEntityType<?> be, BlockPos pos, BlockState state, int containerSize, int[] upgradeSlots, int energyCapacity) {
        super(be, pos, state, containerSize, energyCapacity);

        this.upgradeSlots = upgradeSlots;

        upgrades = new HashMap<>(){{
            for (int upgradeSlot : upgradeSlots) {
                put(upgradeSlot, ItemStack.EMPTY);
            }
        }};

        ContainerData baseData = getBaseData();
        this.setBaseData(new ContainerData() {
            @Override
            public int get(int index) {
                for (int i = 0; i < baseData.getCount(); i++) {
                    if(index == i){
                        return baseData.get(i);
                    }
                }

                int baseCount = baseData.getCount() - 1;
                if (index == baseCount + 1) {
                    return (int) Math.round(QuarryBaseBlockEntity.this.getRoundTimeToMine());
                }
                if(index == baseCount + 2) {
                    return QuarryBaseBlockEntity.this.getFortuneLevel();
                }
                if(index == baseCount + 3) {
                    return QuarryBaseBlockEntity.this.isSilkTouch() ? 1 : 0;
                }

                return baseData.get(0);
            }

            @Override
            public void set(int index, int value) {
                for (int i = 0; i < baseData.getCount(); i++) {
                    if(index == i){
                        baseData.set(index, value);
                    }
                }

                int baseCount = baseData.getCount() - 1;
                if (index == baseCount + 1) {
                    QuarryBaseBlockEntity.this.setTimeToMine(value);
                }
                if(index == baseCount + 2) {
                    QuarryBaseBlockEntity.this.setFortuneLevel(value);
                }
                if(index == baseCount + 3) {
                    QuarryBaseBlockEntity.this.setSilkTouch(value > 0);
                }
            }

            @Override
            public int getCount() {
                return baseData.getCount() + 3;
            }
        });
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    protected boolean canMineBlock(BlockState state, Level level, BlockPos pos, FakePlayer fakePlayer) {
        boolean canMine = false;
        if(state.getBlock().canEntityDestroy(state, level, pos, fakePlayer)) {
            canMine = true;

            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, fakePlayer);
            if(event.isCanceled()) {
                canMine = false;
            }
        }

        return canMine;
    }

    public void onTick(Level level, BlockPos pos) {
        if(level.isClientSide) return;

        if(fakePlayer == null){
            this.fakePlayer = LevelUtil.getFakePlayer((ServerLevel) level, "TransDim-Player");;
        }

        PacketSender.sendToClients(new EnergySyncPacket(getEnergy(), pos));


        if(!isWorking()) return;
        if(isContainerFull()) return;

        if(waitingItems.stream().anyMatch(itemStack -> !itemStack.isEmpty())){
            waitingItems = tryAddItemToAttachedContainer(waitingItems);
            if(isContainerFull()) return;
        }

        if(isMining()) {
            setProgress(getProgress()+1);
            if(getProgress() >= getRoundTimeToMine()){
                setMining(false);

                List<ItemStack> itemStacks = mineNextBlock();
                if(itemStacks.size() > 0){
                    tryAddItemToAttachedContainer(itemStacks);
                }
            }
        } else if(hasEnoughEnergy()){
            getEnergyStorage().extractEnergy(getNeededEnergy(), false);
            setMining(true);
        }

        setChanged(level, pos, getBlockState());
    }

    protected ItemStack getHarvesterTool() {
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

    public boolean canPlaceUpgrade(ItemStack upgrade){
        for (ItemStack itemStack : upgrades.values()) {
            if(itemStack.isEmpty()) continue;
            if(itemStack.getItem() instanceof QuarryUpgrade quarryUpgrade &&
                    !quarryUpgrade.canPlaceMultiple() &&
                    itemStack.is(upgrade.getItem()))
                return false;
        }
        return true;
    }

    public int getItemSlotsSize(){
        return getContainerSize() - upgradeSlots.length;
    }

    public int getUpgradeSlotsSize(){
        return upgradeSlots.length;
    }

    public double getRoundTimeToMine() {
        return Math.max(1, timeToMine);
    }

    public double getTimeToMine() {
        return timeToMine;
    }

    public void setTimeToMine(double timeToMine) {
        this.timeToMine = timeToMine;
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

    public double getOreFindChange() {
        return oreFindChange;
    }

    public void setOreFindChange(double oreFindChange) {
        this.oreFindChange = oreFindChange;
    }

    public Map<Integer, ItemStack> getUpgrades() {
        return upgrades;
    }

    public boolean isMining() {
        return isMining;
    }

    public void setMining(boolean mining) {
        isMining = mining;
    }

    public ItemStack getTool() {
        return tool;
    }

    public ItemStack getSilkTool() {
        return silkTool;
    }

    public ItemStack getFortuneTool() {
        return fortuneTool;
    }

    public void setTool(ItemStack tool) {
        this.tool = tool;
    }

    public void setSilkTool(ItemStack silkTool) {
        this.silkTool = silkTool;
    }

    public void setFortuneTool(ItemStack fortuneTool) {
        this.fortuneTool = fortuneTool;
    }

    public int getBaseTimeToMine() {
        return baseTimeToMine;
    }

    public void setBaseTimeToMine(int baseTimeToMine) {
        this.baseTimeToMine = baseTimeToMine;
    }

    public FakePlayer getFakePlayer() {
        return fakePlayer;
    }

    protected abstract List<ItemStack> mineNextBlock();

}

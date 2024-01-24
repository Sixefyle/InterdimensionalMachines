package be.sixefyle.transdimquarry.blocks.foundry;

import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class BaseFoundry extends TransDimMachine {
    //TODO: check quand le minerais est quasi full
    //      check quand le minerais n'est pas pareil
    //      add energy cost

    protected int[] INPUT_SLOT;
    protected int[] OUTPUT_SLOT;

    private int[] cookTime;
    private int cookMult;
    private boolean autoSplit = true;
    private double inputCostReductionChance = 0.0;

    public BaseFoundry(BlockEntityType<?> be, BlockPos pos, BlockState state) {
        super(be, pos, state, 4, 10_000);

        //INPUT_SLOT = new int[]{1, 3};
        //OUTPUT_SLOT = new int[]{0, 2};
        setInputSlotAmount(2);
        setCookMult(1);
        setMaxProgress(80);

        setBaseEnergyNeeded(120);
    }

    public void setInputSlotAmount(int amount){
        setItems(amount * 2);
        INPUT_SLOT = new int[amount];
        OUTPUT_SLOT = new int[amount];
        cookTime = new int[amount];
        int j = 0, k = 0;
        for (int i = 0; i < amount * 2; i++) {
            if(i%2 == 0){
                OUTPUT_SLOT[j++] = i;
            } else {
                INPUT_SLOT[k++] = i;
            }
        }
    }

    @Override
    public void onTick(Level level, BlockPos blockPos) {
        super.onTick(level, blockPos);

        ItemStack itemStack;
        Optional<SmeltingRecipe> recipe;
        SmeltingRecipe smeltingRecipe;
        ItemStack outputItem;
        for (int i = 0; i < INPUT_SLOT.length; i++) {
            itemStack = getItem(INPUT_SLOT[i]);
            if(itemStack.isEmpty()) {
                cookTime[i] = 0;
                continue;
            }

            recipe = getRecipe(itemStack);
            if(recipe.isEmpty()) continue;

            if(autoSplit && !isWorking()){
                split();
            }

            smeltingRecipe = recipe.get();
            outputItem = getItem(OUTPUT_SLOT[i]);

            if(!outputItem.isEmpty()
                    && !smeltingRecipe.getResultItem(RegistryAccess.EMPTY).is(outputItem.getItem())) return;

            if(outputItem.getCount() < outputItem.getMaxStackSize()) {
                if(getEnergy() >= getNeededEnergy()){
                    addEnergy(-getNeededEnergy());
                    if(cookTime[i]++ >= getMaxProgress()){

                        cookTime[i] = 0;

                        if(level.isClientSide()) return;

                        boolean costReduction = Math.random() <= inputCostReductionChance;

                        ItemStack inputItem = getItem(INPUT_SLOT[i]);
                        ItemStack resultItem = smeltingRecipe.getResultItem(RegistryAccess.EMPTY).copy();
                        int count = costReduction ? cookMult : Math.min(inputItem.getCount(), cookMult);
                        if(outputItem.isEmpty()){
                            resultItem.setCount(count);
                            setItem(OUTPUT_SLOT[i], resultItem);
                        } else if(outputItem.is(resultItem.getItem())) {
                            outputItem.grow(count);
                        }

                        if(costReduction){
                            inputItem.shrink(1);
                        } else {
                            inputItem.shrink(count);
                        }

                        if(autoSplit){
                            split();
                        }
                    }
                }
            } else {
                cookTime[i] = 0;
            }
        }
    }

    @Override
    public boolean isWorking() {
        return Arrays.stream(cookTime).anyMatch(i -> i > 0);
    }

    public List<Integer> availableSlots(ItemStack itemStack){
        List<Integer> slots = new ArrayList<>(INPUT_SLOT.length);
        for (int i : INPUT_SLOT) {
            if(getItem(i).is(itemStack.getItem()) || getItem(i).isEmpty()){
                slots.add(i);
            }
        }
        return slots;
    }

    public List<ItemStack> itemTypeInFoundry(){
        List<ItemStack> itemsType = new ArrayList<>();
        ItemStack item;
        for (int slot : INPUT_SLOT) {
            item = getItem(slot);
            if(!item.isEmpty() && !itemsType.contains(item)) {
                itemsType.add(item);
            }
        }
        return itemsType;
    }

    public void split() {
        if(level == null || level.isClientSide) return;

        List<ItemStack> itemTypes = itemTypeInFoundry();
        if(itemTypes.size() == 0) return;

        for (ItemStack itemType : itemTypes) {
            split(itemType.getItem());
        }
    }

    public void split(Item item) {
        ItemStack itemStack;
        ItemStack tempItemStack = null;
        for (int slot : INPUT_SLOT) {
            itemStack = getItem(slot);

            if (!itemStack.isEmpty()) {
                if(tempItemStack == null && itemStack.is(item)) {
                    tempItemStack = itemStack.copy();
                } else if(tempItemStack != null && tempItemStack.is(item) && itemStack.is(item)){
                    tempItemStack.grow(itemStack.getCount());
                }
            }
        }

        if(tempItemStack == null || tempItemStack.getCount() == 1) return;

        List<Integer> availableSlots = availableSlots(tempItemStack);
        int totalCount = tempItemStack.getCount();
        int elementsPerSlot = totalCount / availableSlots.size();
        int slotsWithExtraElement = totalCount % availableSlots.size();

        ItemStack itemCopy;
        int i = 0;
        for (int slot : availableSlots) {
            itemCopy = tempItemStack.copyWithCount(elementsPerSlot + (i++ < slotsWithExtraElement ? 1 : 0));
            setItem(slot, itemCopy);
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        if (Arrays.stream(INPUT_SLOT).anyMatch(i -> i == slot)){
            return getRecipe(itemStack).isPresent();
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        if (Arrays.stream(OUTPUT_SLOT).anyMatch(i -> i == slot)){
            return !itemStack.isEmpty();
        }

        return false;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if (Arrays.stream(INPUT_SLOT).anyMatch(i -> i == slot)){
            return getRecipe(itemStack).isPresent();
        }

        return false;
    }

    private Optional<SmeltingRecipe> getRecipe(ItemStack itemStack){
        if(itemStack == null) return Optional.empty();
        if(level == null) return Optional.empty();

        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(itemStack), level);
    }

    @Override
    protected abstract Component getDefaultName();

    @Override
    protected abstract AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_);

    public int[] getCookTime() {
        return cookTime;
    }

    public void setCookTime(int[] cookTime) {
        this.cookTime = cookTime;
    }

    public int getCookMult() {
        return cookMult;
    }

    public void setCookMult(int cookMult) {
        this.cookMult = cookMult;
    }

    public boolean isAutoSplit() {
        return autoSplit;
    }

    public void setAutoSplit(boolean autoSplit) {
        this.autoSplit = autoSplit;
    }

    public double getInputCostReductionChance() {
        return inputCostReductionChance;
    }

    public void setInputCostReductionChance(double inputCostReductionChance) {
        this.inputCostReductionChance = inputCostReductionChance;
    }
}

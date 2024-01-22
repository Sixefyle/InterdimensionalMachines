package be.sixefyle.transdimquarry.blocks.foundry;

import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public abstract class BaseFoundry extends TransDimMachine {
    //TODO: check quand le minerais est quasi full
    //      check quand le minerais n'est pas pareil
    //      add energy cost

    protected int[] INPUT_SLOT;
    protected int[] OUTPUT_SLOT;

    private int[] cookTime;
    private int cookMult;
    private boolean autoSplit = false;
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

            smeltingRecipe = recipe.get();
            outputItem = getItem(OUTPUT_SLOT[i]);

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
                    }
                }
            } else {
                cookTime[i] = 0;
            }
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        super.setItem(slot, itemStack);

//        if(autoSplit && !itemStack.isEmpty()){
//            int itemPerSlots = itemStack.getCount() / INPUT_SLOT.length;
//            int itemRemaining = itemStack.getCount() % INPUT_SLOT.length;
//            ItemStack itemCopy = itemStack.copy();
//
//            for (int i : INPUT_SLOT) {
//                itemCopy.setCount(itemPerSlots);
//                setItem(i, itemCopy);
//            }
//        }
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

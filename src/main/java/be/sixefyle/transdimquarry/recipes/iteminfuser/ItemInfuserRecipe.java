package be.sixefyle.transdimquarry.recipes.iteminfuser;


import be.sixefyle.transdimquarry.TransdimensionalMachines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemInfuserRecipe {
    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack harmonizationMatrix;
    private final long energyCost;
    private final int calibratorDurabilityCost;

    private ItemInfuserRecipe(ItemStack input, ItemStack output, ItemStack echo, long energyCost, int calibratorDurabilityCost) {
        this.input = input;
        this.output = output;
        this.harmonizationMatrix = echo;
        this.energyCost = energyCost;
        this.calibratorDurabilityCost = calibratorDurabilityCost;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public long getEnergyCost() {
        return energyCost;
    }

    public int getCalibratorDurabilityCost() {
        return calibratorDurabilityCost;
    }

    public ItemStack getHarmonizationMatrix() {
        return harmonizationMatrix;
    }

    public static ItemInfuserRecipe getRecipe(ItemStack input, ItemStack harmonizationMatrix){
        for (ItemInfuserRecipe itemInfuserRecipe : ItemInfuserRecipeRegister.all) {
            if(itemInfuserRecipe == null) continue;
            if(itemInfuserRecipe.getInput().is(input.getItem())
                    && input.getCount() >= itemInfuserRecipe.getInput().getCount()
                    && itemInfuserRecipe.getHarmonizationMatrix().is(harmonizationMatrix.getItem())
                    && harmonizationMatrix.getCount() >= itemInfuserRecipe.getHarmonizationMatrix().getCount()){

                return itemInfuserRecipe;
            }
        }
        return null;
    }

    public static class Builder {
        private ItemStack input;
        private ItemStack output;
        private ItemStack harmonizationMatrix;
        private long energyCost = 15_000;
        private int calibratorDurabilityCost = 1;

        public Builder setInput(ItemStack input){
            this.input = input;
            return this;
        }

        public Builder setOutput(ItemStack output){
            this.output = output;
            return this;
        }

        public Builder setHarmonizationMatrix(ItemStack harmonizationMatrix){
            this.harmonizationMatrix = harmonizationMatrix;
            return this;
        }

        public Builder setEnergyCost(long energyCost){
            this.energyCost = energyCost;
            return this;
        }

        public Builder setCalibratorDurabilityCost(int calibratorDurabilityCost){
            this.calibratorDurabilityCost = calibratorDurabilityCost;
            return this;
        }

        public ItemInfuserRecipe build(){
            return new ItemInfuserRecipe(input, output, harmonizationMatrix, energyCost, calibratorDurabilityCost);
        }
    }
}

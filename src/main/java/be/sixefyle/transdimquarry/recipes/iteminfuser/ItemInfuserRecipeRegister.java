package be.sixefyle.transdimquarry.recipes.iteminfuser;

import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemInfuserRecipeRegister {
    public static final List<ItemInfuserRecipe> all = new ArrayList<>();
    public static void register(){
        ItemInfuserRecipeRegister.all.add(CALIBRATED_ECHO_SHARD);
        ItemInfuserRecipeRegister.all.add(CALIBRATED_ECHO_INGOT);
        ItemInfuserRecipeRegister.all.add(REINFORCED_NETHER_STAR);
        ItemInfuserRecipeRegister.all.add(CALIBRATOR);
        ItemInfuserRecipeRegister.all.add(RAW_ECHO_INGOT);
        ItemInfuserRecipeRegister.all.add(ECHO_SHARD);
        ItemInfuserRecipeRegister.all.add(DIAMOND);
    }

    public static void register(ItemInfuserRecipe recipe){
        ItemInfuserRecipeRegister.all.add(recipe);
    }

    public static final ItemInfuserRecipe CALIBRATED_ECHO_SHARD = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.QUANTUMITE_INGOT.get(), 4))
            .setInput(new ItemStack(Items.ECHO_SHARD, 5))
            .setOutput(new ItemStack(ItemRegister.CALIBRATED_ECHO_SHARD.get()))
            .setEnergyCost(15_000)
            .setCalibratorDurabilityCost(2)
            .build();

    public static final ItemInfuserRecipe CALIBRATED_ECHO_INGOT = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(BlockRegister.QUANTUMITE_BLOCK.get(), 3))
            .setInput(new ItemStack(ItemRegister.ECHO_INGOT.get(), 8))
            .setOutput(new ItemStack(ItemRegister.CALIBRATED_ECHO_INGOT.get()))
            .setEnergyCost(95_000)
            .setCalibratorDurabilityCost(10)
            .build();

    public static final ItemInfuserRecipe CALIBRATOR = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.ECHO_INGOT.get(), 3))
            .setInput(new ItemStack(Items.COMPASS, 1))
            .setOutput(new ItemStack(ItemRegister.CALIBRATOR.get()))
            .setEnergyCost(55_000)
            .setCalibratorDurabilityCost(1)
            .build();

    public static final ItemInfuserRecipe REINFORCED_NETHER_STAR = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.QUANTUMITE_INGOT.get(), 8))
            .setInput(new ItemStack(Items.NETHER_STAR, 1))
            .setOutput(new ItemStack(ItemRegister.REINFORCED_NETHER_STAR.get()))
            .setEnergyCost(60_000)
            .setCalibratorDurabilityCost(4)
            .build();

    public static final ItemInfuserRecipe RAW_ECHO_INGOT = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.ECHO_SHARD, 2))
            .setInput(new ItemStack(Items.GLOWSTONE_DUST, 1))
            .setOutput(new ItemStack(ItemRegister.RAW_ECHO_INGOT.get()))
            .setEnergyCost(5_000)
            .setCalibratorDurabilityCost(1)
            .build();

    public static final ItemInfuserRecipe ECHO_SHARD = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.ECHO_SHARD, 1))
            .setInput(new ItemStack(Items.BLAZE_ROD, 8))
            .setOutput(new ItemStack(Items.ECHO_SHARD, 2))
            .setEnergyCost(5_000)
            .setCalibratorDurabilityCost(0)
            .build();

    public static final ItemInfuserRecipe DIAMOND = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.ECHO_SHARD, 1))
            .setInput(new ItemStack(Items.EMERALD, 1))
            .setOutput(new ItemStack(Items.DIAMOND, 1))
            .setEnergyCost(25_000)
            .setCalibratorDurabilityCost(0)
            .build();
}

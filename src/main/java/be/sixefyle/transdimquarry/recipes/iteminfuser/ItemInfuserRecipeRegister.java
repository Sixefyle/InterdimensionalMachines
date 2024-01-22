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
        ItemInfuserRecipeRegister.all.add(QUANTUMITE_CALIBRATOR);
        ItemInfuserRecipeRegister.all.add(RAW_ECHO_INGOT);
        ItemInfuserRecipeRegister.all.add(ECHO_SHARD);
        ItemInfuserRecipeRegister.all.add(DIAMOND);
        ItemInfuserRecipeRegister.all.add(CALIBRATED_QUANTUMITE_INGOT);
        ItemInfuserRecipeRegister.all.add(ADVANCED_QUARRY_ENERGY_UPGRADE);
        ItemInfuserRecipeRegister.all.add(ADVANCED_QUARRY_SPEED_UPGRADE);
        ItemInfuserRecipeRegister.all.add(ADVANCED_QUARRY_FORTUNE_UPGRADE);
        ItemInfuserRecipeRegister.all.add(ADVANCED_QUARRY_ORE_FINDER_UPGRADE);
        ItemInfuserRecipeRegister.all.add(COSMIC_QUARRY_ENERGY_UPGRADE);
        ItemInfuserRecipeRegister.all.add(COSMIC_QUARRY_SPEED_UPGRADE);
        ItemInfuserRecipeRegister.all.add(COSMIC_QUARRY_FORTUNE_UPGRADE);
        ItemInfuserRecipeRegister.all.add(COSMIC_QUARRY_ORE_FINDER_UPGRADE);
        ItemInfuserRecipeRegister.all.add(COSMIC_FOUNDRY);
        ItemInfuserRecipeRegister.all.add(ETHEREAL_FOUNDRY);
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

    public static final ItemInfuserRecipe QUANTUMITE_CALIBRATOR = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.CALIBRATED_QUANTUMITE.get(), 32))
            .setInput(new ItemStack(ItemRegister.CALIBRATOR.get(), 1))
            .setOutput(new ItemStack(ItemRegister.QUANTUMITE_CALIBRATOR.get(), 1))
            .setEnergyCost(100_000)
            .setCalibratorDurabilityCost(10)
            .build();

    public static final ItemInfuserRecipe REINFORCED_NETHER_STAR = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.ECHO_INGOT.get(), 4))
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
            .setEnergyCost(500)
            .setCalibratorDurabilityCost(0)
            .build();

    public static final ItemInfuserRecipe DIAMOND = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.ECHO_SHARD, 1))
            .setInput(new ItemStack(Items.EMERALD, 1))
            .setOutput(new ItemStack(Items.DIAMOND, 1))
            .setEnergyCost(25_000)
            .setCalibratorDurabilityCost(0)
            .build();

    public static final ItemInfuserRecipe CALIBRATED_QUANTUMITE_INGOT = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.CALIBRATED_ECHO_INGOT.get(), 4))
            .setInput(new ItemStack(ItemRegister.QUANTUMITE_INGOT.get(), 64))
            .setOutput(new ItemStack(ItemRegister.CALIBRATED_QUANTUMITE.get(), 1))
            .setEnergyCost(100_000)
            .setCalibratorDurabilityCost(10)
            .build();

    public static final ItemInfuserRecipe ADVANCED_QUARRY_SPEED_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.REDSTONE_BLOCK, 64))
            .setInput(new ItemStack(ItemRegister.QUARRY_SPEED_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.ADVANCED_QUARRY_SPEED_UPGRADE.get(), 1))
            .setEnergyCost(50_000)
            .setCalibratorDurabilityCost(6)
            .build();

    public static final ItemInfuserRecipe ADVANCED_QUARRY_FORTUNE_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.DIAMOND_BLOCK, 32))
            .setInput(new ItemStack(ItemRegister.QUARRY_FORTUNE_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.ADVANCED_QUARRY_FORTUNE_UPGRADE.get(), 1))
            .setEnergyCost(50_000)
            .setCalibratorDurabilityCost(6)
            .build();

    public static final ItemInfuserRecipe ADVANCED_QUARRY_ORE_FINDER_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.EMERALD_BLOCK, 32))
            .setInput(new ItemStack(ItemRegister.ORE_FINDER_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.ADVANCED_ORE_FINDER_UPGRADE.get(), 1))
            .setEnergyCost(50_000)
            .setCalibratorDurabilityCost(6)
            .build();

    public static final ItemInfuserRecipe ADVANCED_QUARRY_ENERGY_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(Items.COPPER_BLOCK, 32))
            .setInput(new ItemStack(ItemRegister.QUARRY_ENERGY_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.ADVANCED_QUARRY_ENERGY_UPGRADE.get(), 1))
            .setEnergyCost(50_000)
            .setCalibratorDurabilityCost(6)
            .build();

    public static final ItemInfuserRecipe COSMIC_QUARRY_SPEED_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.REINFORCED_NETHER_STAR.get(), 2))
            .setInput(new ItemStack(ItemRegister.ADVANCED_QUARRY_SPEED_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.COSMIC_QUARRY_SPEED_UPGRADE.get(), 1))
            .setEnergyCost(100_000)
            .setCalibratorDurabilityCost(150)
            .build();
    public static final ItemInfuserRecipe COSMIC_QUARRY_FORTUNE_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.REINFORCED_NETHER_STAR.get(), 2))
            .setInput(new ItemStack(ItemRegister.ADVANCED_QUARRY_FORTUNE_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.COSMIC_QUARRY_FORTUNE_UPGRADE.get(), 1))
            .setEnergyCost(100_000)
            .setCalibratorDurabilityCost(150)
            .build();
    public static final ItemInfuserRecipe COSMIC_QUARRY_ORE_FINDER_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.REINFORCED_NETHER_STAR.get(), 2))
            .setInput(new ItemStack(ItemRegister.ADVANCED_ORE_FINDER_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.COSMIC_ORE_FINDER_UPGRADE.get(), 1))
            .setEnergyCost(100_000)
            .setCalibratorDurabilityCost(150)
            .build();
    public static final ItemInfuserRecipe COSMIC_QUARRY_ENERGY_UPGRADE = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.REINFORCED_NETHER_STAR.get(), 2))
            .setInput(new ItemStack(ItemRegister.ADVANCED_QUARRY_ENERGY_UPGRADE.get(), 1))
            .setOutput(new ItemStack(ItemRegister.COSMIC_QUARRY_ENERGY_UPGRADE.get(), 1))
            .setEnergyCost(100_000)
            .setCalibratorDurabilityCost(150)
            .build();
    public static final ItemInfuserRecipe COSMIC_FOUNDRY = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.ECHO_INGOT.get(), 4))
            .setInput(new ItemStack(BlockRegister.ADVANCED_FOUNDRY.get(), 1))
            .setOutput(new ItemStack(BlockRegister.COSMIC_FOUNDRY.get(), 1))
            .setEnergyCost(2000)
            .setCalibratorDurabilityCost(4)
            .build();
    public static final ItemInfuserRecipe ETHEREAL_FOUNDRY = new ItemInfuserRecipe.Builder()
            .setHarmonizationMatrix(new ItemStack(ItemRegister.CALIBRATED_QUANTUMITE.get(), 4))
            .setInput(new ItemStack(BlockRegister.COSMIC_FOUNDRY.get(), 1))
            .setOutput(new ItemStack(BlockRegister.ETHEREAL_FOUNDRY.get(), 1))
            .setEnergyCost(25000)
            .setCalibratorDurabilityCost(10)
            .build();
}

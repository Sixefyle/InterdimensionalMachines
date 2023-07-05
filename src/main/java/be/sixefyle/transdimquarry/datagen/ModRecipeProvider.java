package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.BlockRegister;
import be.sixefyle.transdimquarry.ItemRegister;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.TRANSDIMENSIONAL_QUARRY.get())
                .define('S', ItemRegister.REINFORCED_NETHER_STAR.get())
                .define('I', ItemRegister.CALIBRATED_ECHO_INGOT.get())
                .define('N', Tags.Items.STORAGE_BLOCKS_NETHERITE)
                .pattern("NIN")
                .pattern("ISI")
                .pattern("NIN")
                .unlockedBy("has_ebony_planks", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(BlockRegister.TRANSDIMENSIONAL_QUARRY.get()).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('P', Items.PAPER)
                .define('B', Tags.Items.DYES_BLACK)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern(" B ")
                .pattern("IPI")
                .pattern("III")
                .unlockedBy("has_ebony_planks", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemRegister.EMPTY_QUARRY_UPGRADE.get()).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.QUARRY_SPEED_UPGRADE.get())
                .define('E', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('R', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('S', Tags.Items.ENDER_PEARLS)
                .pattern("SSS")
                .pattern("RER")
                .pattern("RRR")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.QUARRY_FORTUNE_UPGRADE.get())
                .define('S', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('A', Items.ECHO_SHARD)
                .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                .pattern("AAA")
                .pattern("BSB")
                .pattern("BBB")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.QUARRY_SILK_UPGRADE.get())
                .define('P', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('S', Tags.Items.SLIMEBALLS)
                .define('C', Items.CRYING_OBSIDIAN)
                .pattern("SSS")
                .pattern("CPC")
                .pattern("CCC")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.ORE_FINDER_UPGRADE.get())
                .define('P', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('S', Items.BLUE_ICE)
                .define('C', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .pattern("SSS")
                .pattern("CPC")
                .pattern("CCC")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.ENERGY_UPGRADE.get())
                .define('P', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('S', Items.OXIDIZED_COPPER)
                .define('C', Tags.Items.STORAGE_BLOCKS_EMERALD)
                .pattern("SSS")
                .pattern("CPC")
                .pattern("CCC")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.TRANSDIMENSIONAL_SWORD.get())
                .define('C', ItemRegister.CALIBRATED_ECHO_INGOT.get())
                .define('I', ItemRegister.CALIBRATED_ECHO_SHARD.get())
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" I ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.TRANSDIMENSIONAL_EXCAVATOR.get())
                .define('E', ItemRegister.ECHO_INGOT.get())
                .define('C', ItemRegister.CALIBRATOR.get())
                .define('S', Items.ECHO_SHARD)
                .define('N', Items.NETHERITE_INGOT)
                .pattern("ESE")
                .pattern("NCN")
                .pattern(" E ")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get())
                .define('N', Items.NETHERITE_BLOCK)
                .define('A', ItemRegister.CALIBRATOR.get())
                .define('S', ItemRegister.REINFORCED_NETHER_STAR.get())
                .pattern("SNS")
                .pattern("NAN")
                .pattern("SNS")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.REINFORCED_NETHER_STAR.get())
                .define('E', ItemRegister.ECHO_INGOT.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('S', Items.NETHER_STAR)
                .pattern("ENE")
                .pattern("NSN")
                .pattern("ENE")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.RAW_ECHO_INGOT.get())
                .define('E', Items.ECHO_SHARD)
                .pattern("EE ")
                .pattern("EE ")
                .pattern("   ")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        SimpleCookingRecipeBuilder.blasting(
                Ingredient.of(ItemRegister.RAW_ECHO_INGOT.get()), RecipeCategory.MISC, ItemRegister.ECHO_INGOT.get(),
                10, 40)
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.CALIBRATOR.get())
                .define('E', ItemRegister.ECHO_INGOT.get())
                .define('C', Items.COMPASS)
                .pattern(" E ")
                .pattern("ECE")
                .pattern(" E ")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.CALIBRATED_ECHO_SHARD.get(), 3)
                .define('E', ItemRegister.ECHO_INGOT.get())
                .define('R', ItemRegister.REINFORCED_NETHER_STAR.get())
                .define('C', ItemRegister.CALIBRATOR.get())
                .pattern("EEE")
                .pattern("RCR")
                .pattern("EEE")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.CALIBRATED_ECHO_INGOT.get())
                .define('E', ItemRegister.CALIBRATED_ECHO_SHARD.get())
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

    }
}

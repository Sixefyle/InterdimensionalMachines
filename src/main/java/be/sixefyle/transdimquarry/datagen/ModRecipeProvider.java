package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
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
                .define('S', ItemRegister.QUANTUMITE_CALIBRATOR.get())
                .define('I', ItemRegister.REINFORCED_NETHER_STAR.get())
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
                .pattern(" S ")
                .pattern("RER")
                .pattern(" R ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.QUARRY_FORTUNE_UPGRADE.get())
                .define('S', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('A', Items.ECHO_SHARD)
                .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                .pattern(" A ")
                .pattern("BSB")
                .pattern(" B ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.QUARRY_SILK_UPGRADE.get())
                .define('P', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('S', Tags.Items.SLIMEBALLS)
                .define('C', Items.CRYING_OBSIDIAN)
                .pattern(" S ")
                .pattern("CPC")
                .pattern(" C ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.ORE_FINDER_UPGRADE.get())
                .define('P', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('S', Items.BLUE_ICE)
                .define('C', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .pattern(" S ")
                .pattern("CPC")
                .pattern(" C ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.QUARRY_ENERGY_UPGRADE.get())
                .define('P', ItemRegister.EMPTY_QUARRY_UPGRADE.get())
                .define('S', Items.OXIDIZED_COPPER)
                .define('C', Tags.Items.STORAGE_BLOCKS_EMERALD)
                .pattern(" S ")
                .pattern("CPC")
                .pattern(" C ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.TRANSDIMENSIONAL_SWORD.get())
                .define('C', ItemRegister.CALIBRATED_ECHO_SHARD.get())
                .define('N', Tags.Items.INGOTS_NETHERITE)
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" N ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.TRANSDIMENSIONAL_EXCAVATOR.get())
                .define('S', Items.ECHO_SHARD)
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("ISI")
                .pattern("III")
                .pattern(" I ")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get())
                .define('N', Items.NETHERITE_INGOT)
                .define('A', ItemRegister.CALIBRATOR.get())
                .define('S', ItemRegister.ECHO_INGOT.get())
                .pattern("NSN")
                .pattern("SAS")
                .pattern("NSN")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.REINFORCED_NETHER_STAR.get())
                .define('E', ItemRegister.ECHO_INGOT.get())
                .define('N', Tags.Items.INGOTS_NETHERITE)
                .define('S', Tags.Items.NETHER_STARS)
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

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockRegister.QUANTUMITE_BLOCK.get())
                .define('E', ItemRegister.QUANTUMITE_INGOT.get())
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.ITEM_INFUSER.get())
                .define('Q', ItemRegister.QUANTUMITE_INGOT.get())
                .define('I', Items.IRON_INGOT)
                .define('E', Items.ECHO_SHARD)
                .pattern("III")
                .pattern("QEQ")
                .pattern("III")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.FOUNDRY.get())
                .define('I', ItemRegister.QUANTUMITE_INGOT.get())
                .define('E', Items.FURNACE)
                .define('R', Items.REDSTONE)
                .pattern("EIE")
                .pattern("IRI")
                .pattern("EIE")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.ADVANCED_FOUNDRY.get())
                .define('I', Items.DIAMOND)
                .define('E', BlockRegister.FOUNDRY.get())
                .define('F', Items.FURNACE)
                .pattern("FIF")
                .pattern("IEI")
                .pattern("FIF")
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemRegister.QUANTUMITE_INGOT.get()).build()))
                .save(pFinishedRecipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemRegister.QUANTUMITE_INGOT.get(), 9)
                .requires(BlockRegister.QUANTUMITE_BLOCK.get())
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer);

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ItemRegister.RAW_ECHO_INGOT.get()), RecipeCategory.MISC, ItemRegister.ECHO_INGOT.get(),
                        10, 300)
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer, "echo_ingot_smelting");

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ItemRegister.RAW_ECHO_INGOT.get()), RecipeCategory.MISC, ItemRegister.ECHO_INGOT.get(),
                        10, 100)
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer, "echo_ingot_smelting_blasting");

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ItemRegister.QUANTUMITE_DUST.get()), RecipeCategory.MISC, ItemRegister.QUANTUMITE_INGOT.get(),
                        0, 300)
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer, "quantumite_dust_smelting");

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ItemRegister.QUANTUMITE_DUST.get()), RecipeCategory.MISC, ItemRegister.QUANTUMITE_INGOT.get(),
                        0, 100)
                .unlockedBy("has_echo_shard", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.ECHO_SHARD).build()))
                .save(pFinishedRecipeConsumer, "quantumite_dust_smelting_blasting");
    }
}

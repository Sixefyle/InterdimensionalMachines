package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.BlockRegister;
import be.sixefyle.transdimquarry.ItemRegister;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
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
                .define('S', Tags.Items.NETHER_STARS)
                .define('I', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('N', Tags.Items.INGOTS_NETHERITE)
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegister.TRANSDIM_SWORD.get())
                .define('N', Items.NETHERITE_BLOCK)
                .define('S', Items.STICK)
                .define('A', Items.NETHER_STAR)
                .pattern("ANA")
                .pattern("ANA")
                .pattern(" S ")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get())
                .define('N', Items.NETHERITE_BLOCK)
                .define('A', Items.AMETHYST_CLUSTER)
                .define('S', Items.NETHER_STAR)
                .pattern("NSN")
                .pattern("SAS")
                .pattern("NSN")
                .unlockedBy("has_nether_star", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.NETHER_STAR).build()))
                .save(pFinishedRecipeConsumer);
    }
}

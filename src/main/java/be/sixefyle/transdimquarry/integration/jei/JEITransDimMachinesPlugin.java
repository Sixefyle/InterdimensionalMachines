package be.sixefyle.transdimquarry.integration.jei;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserScreen;
import be.sixefyle.transdimquarry.recipes.iteminfuser.ItemInfuserRecipe;
import be.sixefyle.transdimquarry.recipes.iteminfuser.ItemInfuserRecipeRegister;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

@JeiPlugin
public class JEITransDimMachinesPlugin implements IModPlugin {
    public static RecipeType<ItemInfuserRecipe> ITEM_INFUSER_TYPE =
            new RecipeType<>(ItemInfuserRecipeCategory.UID, ItemInfuserRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TransdimensionalMachines.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ItemInfuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ITEM_INFUSER_TYPE, ItemInfuserRecipeRegister.all);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ItemInfuserScreen.class, 29, 28, 97, 48,
                ITEM_INFUSER_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(BlockRegister.ITEM_INFUSER.get().asItem().getDefaultInstance(), ITEM_INFUSER_TYPE);
        registration.addRecipeCatalyst(BlockRegister.FOUNDRY.get().asItem().getDefaultInstance(), RecipeTypes.SMELTING);
    }
}

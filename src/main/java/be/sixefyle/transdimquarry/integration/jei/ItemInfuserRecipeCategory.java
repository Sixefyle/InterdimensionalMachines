package be.sixefyle.transdimquarry.integration.jei;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.recipes.iteminfuser.ItemInfuserRecipe;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class ItemInfuserRecipeCategory implements IRecipeCategory<ItemInfuserRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(TransdimensionalMachines.MODID, "item_infuser");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/jei_item_infuser.png");

    private final IDrawable background;
    private final IDrawable icon;

    private final int imageWidth;
    private final int imageHeight;
    public ItemInfuserRecipeCategory(IGuiHelper helper) {
        imageWidth = 176;
        imageHeight = 83;

        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegister.ITEM_INFUSER.get()));
    }

    @Override
    public RecipeType<ItemInfuserRecipe> getRecipeType() {
        return JEITransDimMachinesPlugin.ITEM_INFUSER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Item Infuser");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ItemInfuserRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 19).addItemStack(new ItemStack(ItemRegister.CALIBRATOR.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 49).addIngredients(Ingredient.of(recipe.getHarmonizationMatrix()));
        builder.addSlot(RecipeIngredientRole.INPUT, 128, 34).addIngredients(Ingredient.of(recipe.getInput()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 148, 34).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(ItemInfuserRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.7f, .7f, .7f);

        guiGraphics.drawString(Minecraft.getInstance().font, String.format("Energy Cost: %s", NumberUtil.formatToEnergy(recipe.getEnergyCost())), 41, 29, 0xffffff, false);
        guiGraphics.drawString(Minecraft.getInstance().font, String.format("Calibrator Damage: %d", recipe.getCalibratorDurabilityCost()), 41, 39, 0xffffff, false);

        guiGraphics.pose().popPose();
    }
}

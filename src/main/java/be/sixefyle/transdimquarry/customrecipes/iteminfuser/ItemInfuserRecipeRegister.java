package be.sixefyle.transdimquarry.customrecipes.iteminfuser;

import be.sixefyle.transdimquarry.registries.ItemRegister;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemInfuserRecipeRegister {
    public static final List<ItemInfuserRecipe> all = new ArrayList<>();
    public static void register(){
        ItemInfuserRecipeRegister.all.add(CALIBRATED_ECHO_SHARD);
    }

    public static void register(ItemInfuserRecipe recipe){
        ItemInfuserRecipeRegister.all.add(recipe);
    }

    public static final ItemInfuserRecipe CALIBRATED_ECHO_SHARD = new ItemInfuserRecipe.Builder()
            .setInput(new ItemStack(Items.ECHO_SHARD))
            .setOutput(new ItemStack(ItemRegister.CALIBRATED_ECHO_SHARD.get()))
            .setHarmonizationMatrix(new ItemStack(Items.ECHO_SHARD))
            .setEnergyCost(15_000)
            .setCalibratorDurabilityCost(1)
            .build();
}

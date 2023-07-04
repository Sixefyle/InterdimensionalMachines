package be.sixefyle.transdimquarry.items;

import net.minecraft.world.item.ItemStack;

public interface IInfusedTool {
    int getBaseInfusedEnergyNeeded();
    int getInfusedEnergyNeeded(ItemStack itemStack);
    int getInfusedEnergy(ItemStack itemStack);
    void setInfusedEnergy(ItemStack itemStack, int value);
    void addInfusedEnergy(ItemStack itemStack, int energyCost);
}

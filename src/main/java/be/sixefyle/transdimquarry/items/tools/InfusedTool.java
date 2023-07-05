package be.sixefyle.transdimquarry.items.tools;

import be.sixefyle.transdimquarry.items.EnergizedItem;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class InfusedTool extends EnergizedItem {
    private final int useEnergyCost;
    private final int baseInfusedEnergyNeeded;

    public InfusedTool(Properties p_41383_, int capacity, int energyCost, int infusedEnergyNeeded) {
        super(p_41383_.stacksTo(1), capacity);

        this.useEnergyCost = energyCost;
        this.baseInfusedEnergyNeeded = infusedEnergyNeeded;
    }

    public void addInfusedEnergy(ItemStack itemStack, int amount){
        if(!itemStack.hasTag()){
            itemStack.setTag(new CompoundTag());
        }

        if(!itemStack.getTag().contains("infused_energy")) {
            itemStack.getTag().putInt("infused_energy", 0);
        }

        int newInfusedEnergy = getInfusedEnergy(itemStack) + amount;
        int neededEnergy = getInfusedEnergyNeeded(itemStack);
        if(neededEnergy > 0){
            do {
                if(newInfusedEnergy < neededEnergy){
                    setInfusedEnergy(itemStack, newInfusedEnergy);
                } else {
                    onInfuseLevelUp(itemStack);
                    neededEnergy = getInfusedEnergyNeeded(itemStack);
                    itemStack.getTag().putInt("infused_energy", 0);
                }
                newInfusedEnergy -= neededEnergy;
            }while (newInfusedEnergy > 0);
        }
    }

    public abstract void onInfuseLevelUp(ItemStack itemStack);

    public int getBaseInfusedEnergyNeeded() {
        return baseInfusedEnergyNeeded;
    }

    public abstract int getInfusedEnergyNeeded(ItemStack itemStack);

    public boolean isMaxed(ItemStack itemStack){
        return itemStack.hasTag() && itemStack.getTag().getBoolean("is_maxed");
    }

    public int getInfusedEnergy(ItemStack itemStack){
        return itemStack.hasTag() ? itemStack.getTag().getInt("infused_energy") : 0;
    }

    public void setInfusedEnergy(ItemStack itemStack, int value){
        if(!itemStack.hasTag())return;

        itemStack.getTag().putInt("infused_energy", value);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        int infusedEnergy = getInfusedEnergy(itemStack);

        components.add(Component.empty());
        if(isMaxed(itemStack)){
            components.add(Component.literal("Infused Energy Maxed").withStyle(ChatFormatting.YELLOW));
        } else {
            components.add(Component.literal(String.format("Infused Energy %s/%s", NumberUtil.formatToEnergy(infusedEnergy), NumberUtil.formatToEnergy(getInfusedEnergyNeeded(itemStack)))).withStyle(ChatFormatting.YELLOW));
        }
        components.add(Component.literal("Can be infused in a Tool Infuser").withStyle(ChatFormatting.GRAY));
    }

    public int getBaseEnergyCost() {
        return useEnergyCost;
    }
}

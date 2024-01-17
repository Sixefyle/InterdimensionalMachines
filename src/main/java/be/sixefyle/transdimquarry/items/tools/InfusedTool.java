package be.sixefyle.transdimquarry.items.tools;

import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.items.EnergizedItem;
import be.sixefyle.transdimquarry.key.KeyBinding;
import be.sixefyle.transdimquarry.utils.ComponentUtil;
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
    private final int baseNeededInfusedEnergy;


    public InfusedTool(Properties properties, int capacity, int energyCost, int infusedEnergyNeeded) {
        super(properties.stacksTo(1), capacity);

        this.useEnergyCost = energyCost;
        this.baseNeededInfusedEnergy = infusedEnergyNeeded;
    }

    public void addInfusedEnergy(ItemStack itemStack, long amount){
        if(!itemStack.hasTag()){
            itemStack.setTag(new CompoundTag());
        }

        if(!itemStack.getTag().contains("infused_energy")) {
            itemStack.getTag().putInt("infused_energy", 0);
        }

        long infusedEnergy = getInfusedEnergy(itemStack) + amount;
        double neededEnergy = getNeededInfusedEnergy(itemStack);
        if (neededEnergy > 0) {
            while (infusedEnergy > 0) {
                if (infusedEnergy < neededEnergy) {
                    setInfusedEnergy(itemStack, infusedEnergy);
                    break;
                } else {
                    onInfuseLevelUp(itemStack);
                    infusedEnergy -= neededEnergy;
                    neededEnergy = getNeededInfusedEnergy(itemStack);
                }
            }

            setInfusedEnergy(itemStack, infusedEnergy);
        }
    }

    public abstract void onInfuseLevelUp(ItemStack itemStack);

    public int getBaseNeededInfusedEnergy() {
        return baseNeededInfusedEnergy;
    }

    public abstract double getNeededInfusedEnergy(ItemStack itemStack);

    public boolean isMaxed(ItemStack itemStack){
        return itemStack.hasTag() && itemStack.getTag().getBoolean("is_maxed");
    }

    public int getInfusedEnergy(ItemStack itemStack){
        return itemStack.hasTag() ? itemStack.getTag().getInt("infused_energy") : 0;
    }

    public void setInfusedEnergy(ItemStack itemStack, long value){
        if(!itemStack.hasTag()) return;

        itemStack.getTag().putLong("infused_energy", value);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        int infusedEnergy = getInfusedEnergy(itemStack);

        if(isMaxed(itemStack)){
            tooltip.add(EnumColor.PURPLE.getColoredComponent("Tool Maxed"));
        } else {
            tooltip.add(EnumColor.PURPLE.getColoredComponent("Infused Energy: ")
                    .append(EnumColor.GRAY.getColoredComponent(NumberUtil.formatToEnergy(infusedEnergy))
                    .append(EnumColor.GRAY.getColoredComponent("/" + NumberUtil.formatToEnergy(getNeededInfusedEnergy(itemStack))))));
        }

        addCenterTooltip(tooltip, itemStack);

        if(this instanceof IModeHandle){
            tooltip.add(Component.empty());
            tooltip.add(EnumColor.GRAY.getColoredComponent("Press [")
                    .append(EnumColor.RED.getColoredComponent(KeyBinding.TOOL_SETTINGS_KEY.getKey().getDisplayName().getString())
                    .append(EnumColor.GRAY.getColoredComponent("] while holding to open the config menu."))));
        }
    }

    public void addCenterTooltip(List<Component> tooltip, ItemStack itemStack) {}

    public int getBaseEnergyCost() {
        return useEnergyCost;
    }

}

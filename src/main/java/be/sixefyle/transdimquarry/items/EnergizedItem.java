package be.sixefyle.transdimquarry.items;

import be.sixefyle.transdimquarry.energy.ItemEnergyStorage;
import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergizedItem extends Item {
    public static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);
    final int capacity;

    public EnergizedItem(Properties p_41383_, int capacity) {
        super(p_41383_);

        this.capacity = capacity;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnergyCapabilityProvider(new ItemEnergyStorage(stack, capacity));
    }

    public IEnergyStorage getEnergyStorage(ItemStack itemStack){
        if(ForgeCapabilities.ENERGY == null)
            return EMPTY_ENERGY_STORAGE;

        return itemStack.getCapability(ForgeCapabilities.ENERGY).orElse(EMPTY_ENERGY_STORAGE);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        return (int) ((double) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored() * 13.0);
    }

    @Override
    public int getBarColor(ItemStack p_150901_) {
        return FastColor.ARGB32.color(0, 136,21,237);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        double storedEnergy = energyStorage.getEnergyStored();
        double maxEnergy = energyStorage.getMaxEnergyStored();

        components.add(EnumColor.TEAL.getColoredComponent("Tool Energy: ")
                .append(EnumColor.GRAY.getColoredComponent(NumberUtil.formatToEnergy(storedEnergy)))
                .append(EnumColor.GRAY.getColoredComponent("/" + NumberUtil.formatToEnergy(maxEnergy))));
    }
}

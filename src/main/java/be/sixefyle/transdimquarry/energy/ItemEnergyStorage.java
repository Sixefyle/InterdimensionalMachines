package be.sixefyle.transdimquarry.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;

public class ItemEnergyStorage extends EnergyStorage {
    private final ItemStack container;

    public ItemEnergyStorage(ItemStack container, int capacity) {
        super(capacity);
        this.container = container;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energy = container.getTag().getInt("energy");
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

        if (!simulate) {
            energy -= energyExtracted;
            container.getTag().putInt("energy", energy);
        }

        super.receiveEnergy(maxExtract, simulate);
        return energyExtracted;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(!container.hasTag()){
            CompoundTag compoundTag = new CompoundTag();
            container.setTag(compoundTag);
        }

        int energy = container.getTag().getInt("energy");
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            container.getTag().putInt("energy", energy);
        }

        super.receiveEnergy(maxReceive, simulate);
        return energyReceived;
    }

    @Override
    public int getEnergyStored() {
        return container.getTag().getInt("energy");
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public boolean canExtract() {
        return true;
    }
}

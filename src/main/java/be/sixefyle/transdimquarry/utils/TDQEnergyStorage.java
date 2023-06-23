package be.sixefyle.transdimquarry.utils;

import net.minecraftforge.energy.EnergyStorage;

public abstract class TDQEnergyStorage extends EnergyStorage {
    public TDQEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public TDQEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public TDQEnergyStorage(int capacity) {
        super(capacity);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if(extractedEnergy != 0) {
            onEnergyChanged();
        }

        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if(receiveEnergy != 0) {
            onEnergyChanged();
        }

        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }

    public abstract void onEnergyChanged();
}

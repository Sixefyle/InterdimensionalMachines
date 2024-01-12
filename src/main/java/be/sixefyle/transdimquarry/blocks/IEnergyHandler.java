package be.sixefyle.transdimquarry.blocks;

import be.sixefyle.transdimquarry.energy.ILongEnergyStorage;

public interface IEnergyHandler {
    long getEnergy();
    void setEnergy(long value);
    ILongEnergyStorage getEnergyStorage();

    void setMaxEnergyInput(long value);
}

package be.sixefyle.transdimquarry.blocks;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyHandler {
    int getEnergy();
    void setEnergy(int value);
    IEnergyStorage getEnergyStorage();

    void setMaxEnergyInput(int value);
}

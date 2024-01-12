package be.sixefyle.transdimquarry.energy;

public class BlockEnergyStorage extends LongEnergyStorage {
    public BlockEnergyStorage(long capacity, long maxTransfer) {
        super(capacity, maxTransfer);
    }

    public BlockEnergyStorage(long capacity, long maxReceive, long maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public BlockEnergyStorage(long capacity) {
        super(capacity);
    }

    public void setEnergy(long energy) {
        this.energy = energy;
    }

    public void setCapacity(long capacity){
        this.capacity = capacity;
    }
}

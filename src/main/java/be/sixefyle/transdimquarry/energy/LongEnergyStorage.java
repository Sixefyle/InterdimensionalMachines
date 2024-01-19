package be.sixefyle.transdimquarry.energy;

import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class LongEnergyStorage implements ILongEnergyStorage, INBTSerializable<Tag> {
    protected long energy;
    protected long capacity;
    protected long maxReceive;
    protected long maxExtract;

    public LongEnergyStorage(long capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public LongEnergyStorage(long capacity, long maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public LongEnergyStorage(long capacity, long maxReceive, long maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public LongEnergyStorage(long capacity, long maxReceive, long maxExtract, long energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
    }

    @Override
    public long receiveEnergy(long maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        long energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;

        return energyReceived;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        long energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) receiveEnergy((long) maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return (int) extractEnergy((long) maxExtract, simulate);
    }

    @Override
    public int getEnergyStored()
    {
        return (int) energy;
    }
    public long getLongEnergyStored()
    {
        return energy;
    }

    @Override
    public int getMaxEnergyStored()
    {
        return (int) capacity;
    }
    public long getLongMaxEnergyStored()
    {
        return capacity;
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }

    @Override
    public Tag serializeNBT()
    {
        return LongTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        if (!(nbt instanceof LongTag longNbt))
            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        this.energy = longNbt.getAsLong();
    }
}

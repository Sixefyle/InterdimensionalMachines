package be.sixefyle.transdimquarry.items.quarryupgrades;

import be.sixefyle.transdimquarry.blocks.quarry.TransdimQuarryBlockEntity;

public interface QuarryUpgrade {
    void onPlaced(TransdimQuarryBlockEntity blockEntity);
    void onRemoved(TransdimQuarryBlockEntity blockEntity);
}

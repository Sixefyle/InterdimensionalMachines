package be.sixefyle.transdimquarry.items.quarryupgrades;

import be.sixefyle.transdimquarry.blocks.quarries.QuarryBaseBlockEntity;

public interface QuarryUpgrade {
    void onPlaced(QuarryBaseBlockEntity quarry);
    void onRemoved(QuarryBaseBlockEntity quarry);
    boolean canPlaceMultiple();
}

package be.sixefyle.transdimquarry.items;

import be.sixefyle.transdimquarry.blocks.entity.TransdimQuarryBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface QuarryUpgrade {
    void onPlaced(TransdimQuarryBlockEntity blockEntity);
    void onRemoved(TransdimQuarryBlockEntity blockEntity);
}

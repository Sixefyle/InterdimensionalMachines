package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.BlockRegister;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {
    @Override
    protected void addTables() {
        this.dropSelf(BlockRegister.TRANSDIMENSIONAL_QUARRY.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegister.ALL.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

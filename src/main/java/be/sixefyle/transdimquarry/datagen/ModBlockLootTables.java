package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
//        this.dropSelf(BlockRegister.DIMENSIONAL_QUARRY.get());
        this.dropSelf(BlockRegister.TRANSDIMENSIONAL_QUARRY.get());
        this.dropSelf(BlockRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get());
        this.dropSelf(BlockRegister.ITEM_INFUSER.get());

        this.dropSelf(BlockRegister.QUANTUMITE_BLOCK.get());

        this.dropOther(BlockRegister.QUANTUMITE_ORE.get(), ItemRegister.RAW_QUANTUMITE_ORE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegister.ALL.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

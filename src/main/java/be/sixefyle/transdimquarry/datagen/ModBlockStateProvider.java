package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.TransdimensionalMachines;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput packOutput, ExistingFileHelper exFileHelper) {
        super(packOutput, TransdimensionalMachines.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        blockWithItem(BlockRegister.TRANSDIMENSIONAL_QUARRY);
//        blockWithItem(BlockRegister.QUANTUMITE_ORE);
//        blockWithItem(BlockRegister.QUANTUMITE_BLOCK);
//        blockWithItem(BlockRegister.DIMENSIONAL_QUARRY);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));

    }
}

package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.ItemRegister;
import be.sixefyle.transdimquarry.TransdimensionalMachines;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, TransdimensionalMachines.MODID,existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemRegister.EMPTY_QUARRY_UPGRADE.get());
        basicItem(ItemRegister.QUARRY_SPEED_UPGRADE.get());
        basicItem(ItemRegister.QUARRY_FORTUNE_UPGRADE.get());
        basicItem(ItemRegister.QUARRY_SILK_UPGRADE.get());
        basicItem(ItemRegister.ENERGY_UPGRADE.get());
        basicItem(ItemRegister.ORE_FINDER_UPGRADE.get());
    }
}

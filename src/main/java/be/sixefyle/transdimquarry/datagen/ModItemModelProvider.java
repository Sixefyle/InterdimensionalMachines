package be.sixefyle.transdimquarry.datagen;

import be.sixefyle.transdimquarry.ItemRegister;
import be.sixefyle.transdimquarry.TransdimensionalMachines;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TransdimensionalMachines.MODID,existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ItemRegister.EMPTY_QUARRY_UPGRADE.get());
        basicItem(ItemRegister.QUARRY_SPEED_UPGRADE.get());
        basicItem(ItemRegister.QUARRY_FORTUNE_UPGRADE.get());
        basicItem(ItemRegister.QUARRY_SILK_UPGRADE.get());
        basicItem(ItemRegister.ENERGY_UPGRADE.get());
        basicItem(ItemRegister.ORE_FINDER_UPGRADE.get());

        basicItem(ItemRegister.CALIBRATOR.get());
        basicItem(ItemRegister.CALIBRATED_ECHO_INGOT.get());
        basicItem(ItemRegister.RAW_ECHO_INGOT.get());
        basicItem(ItemRegister.ECHO_INGOT.get());
        basicItem(ItemRegister.REINFORCED_NETHER_STAR.get());
        basicItem(ItemRegister.ORE_FINDER_UPGRADE.get());
        basicItem(ItemRegister.CALIBRATED_ECHO_SHARD.get());

        handheld(ItemRegister.TRANSDIMENSIONAL_SWORD);
        handheld(ItemRegister.TRANSDIMENSIONAL_EXCAVATOR);
    }


    private void handheld(RegistryObject<Item> item) {
        String itemName = item.getId().getPath();

        singleTexture(
                itemName,
                new ResourceLocation("item/handheld"),
                "layer0",
                new ResourceLocation(TransdimensionalMachines.MODID, "item/" + itemName));
    }
}

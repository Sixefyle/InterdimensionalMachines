package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.items.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    public static final ModCreativeTab creativeTab = new ModCreativeTab(CreativeModeTab.TABS.length, "test");

    public static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, TransdimensionalMachines.MODID);

    public static final RegistryObject<Item> EMPTY_QUARRY_UPGRADE = ALL.register("empty_quarry_upgrade",
            () -> new Item(new Item.Properties().tab(creativeTab)));

    public static final RegistryObject<Item> QUARRY_SPEED_UPGRADE = ALL.register("speed_quarry_upgrade",
            QuarrySpeedUpgradeItem::new);
    public static final RegistryObject<Item> QUARRY_FORTUNE_UPGRADE = ALL.register("fortune_quarry_upgrade",
            QuarryFortuneUpgradeItem::new);
    public static final RegistryObject<Item> QUARRY_SILK_UPGRADE = ALL.register("silk_quarry_upgrade",
            QuarrySilkTouchUpgradeItem::new);

    public static final RegistryObject<Item> ENERGY_UPGRADE = ALL.register("energy_quarry_upgrade",
            QuarryEnergyOptimizationUpgradeItem::new);

    public static final RegistryObject<Item> ORE_FINDER_UPGRADE = ALL.register("ore_finder_quarry_upgrade",
            QuarryOreFinderUpgradeItem::new);
    public static class ModCreativeTab extends CreativeModeTab {

        private ModCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegister.TRANSDIMENSIONAL_QUARRY.get());
        }
    }
}

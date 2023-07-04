package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.items.quarryupgrades.*;
import be.sixefyle.transdimquarry.items.tools.TransdimExcavator;
import be.sixefyle.transdimquarry.items.tools.TransdimSword;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {

    public static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, TransdimensionalMachines.MODID);

    public static final RegistryObject<Item> EMPTY_QUARRY_UPGRADE = ALL.register("empty_quarry_upgrade",
            () -> new Item(new Item.Properties()));

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

    public static final RegistryObject<Item> TRANSDIM_SWORD = ALL.register("transdimensional_sword",
            TransdimSword::new);

    public static final RegistryObject<Item> TRANSDIM_EXCAVATOR = ALL.register("transdimensional_excavator",
            TransdimExcavator::new);
}

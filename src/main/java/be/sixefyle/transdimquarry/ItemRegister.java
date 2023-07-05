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

    public static final RegistryObject<Item> RAW_ECHO_INGOT = CreativeTabRegister.add(ALL.register("raw_echo_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> ECHO_INGOT = CreativeTabRegister.add(ALL.register("echo_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> REINFORCED_NETHER_STAR = CreativeTabRegister.add(ALL.register("reinforced_nether_star",
            () -> new Item(new Item.Properties().fireResistant())));
    public static final RegistryObject<Item> CALIBRATOR = CreativeTabRegister.add(ALL.register("calibrator",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> CALIBRATED_ECHO_SHARD = CreativeTabRegister.add(ALL.register("calibrated_echo_shard",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> CALIBRATED_ECHO_INGOT = CreativeTabRegister.add(ALL.register("calibrated_echo_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> EMPTY_QUARRY_UPGRADE = CreativeTabRegister.add(ALL.register("empty_quarry_upgrade",
            () -> new Item(new Item.Properties())));

    public static final RegistryObject<Item> QUARRY_SPEED_UPGRADE = CreativeTabRegister.add(ALL.register("speed_quarry_upgrade",
            QuarrySpeedUpgradeItem::new));
    public static final RegistryObject<Item> QUARRY_FORTUNE_UPGRADE = CreativeTabRegister.add(ALL.register("fortune_quarry_upgrade",
            QuarryFortuneUpgradeItem::new));
    public static final RegistryObject<Item> QUARRY_SILK_UPGRADE = CreativeTabRegister.add(ALL.register("silk_quarry_upgrade",
            QuarrySilkTouchUpgradeItem::new));
    public static final RegistryObject<Item> ENERGY_UPGRADE = CreativeTabRegister.add(ALL.register("energy_quarry_upgrade",
            QuarryEnergyOptimizationUpgradeItem::new));
    public static final RegistryObject<Item> ORE_FINDER_UPGRADE = CreativeTabRegister.add(ALL.register("ore_finder_quarry_upgrade",
            QuarryOreFinderUpgradeItem::new));


    public static final RegistryObject<Item> TRANSDIMENSIONAL_SWORD = CreativeTabRegister.add(ALL.register("transdimensional_sword",
            TransdimSword::new));
    public static final RegistryObject<Item> TRANSDIMENSIONAL_EXCAVATOR = CreativeTabRegister.add(ALL.register("transdimensional_excavator",
            TransdimExcavator::new));
}

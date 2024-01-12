package be.sixefyle.transdimquarry.registries;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.items.quarryupgrades.advanced.AdvancedQuarryEnergyOptimizationUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.advanced.AdvancedQuarryFortuneUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.advanced.AdvancedQuarryOreFinderUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.advanced.AdvancedQuarrySpeedUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.cosmic.CosmicQuarryEnergyOptimizationUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.cosmic.CosmicQuarryFortuneUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.cosmic.CosmicQuarryOreFinderUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.cosmic.CosmicQuarrySpeedUpgradeItem;
import be.sixefyle.transdimquarry.items.quarryupgrades.simple.*;
import be.sixefyle.transdimquarry.items.tools.TransdimExcavator;
import be.sixefyle.transdimquarry.items.tools.TransdimSword;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {

    public static final DeferredRegister<Item> ALL = DeferredRegister.create(ForgeRegistries.ITEMS, TransdimensionalMachines.MODID);

    public static final RegistryObject<Item> RAW_QUANTUMITE_ORE = CreativeTabRegister.add(ALL.register("raw_quantumite_ore",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> QUANTUMITE_INGOT = CreativeTabRegister.add(ALL.register("quantumite_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> CALIBRATED_QUANTUMITE_INGOT = CreativeTabRegister.add(ALL.register("calibrated_quantumite_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> RAW_ECHO_INGOT = CreativeTabRegister.add(ALL.register("raw_echo_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> ECHO_INGOT = CreativeTabRegister.add(ALL.register("echo_ingot",
            () -> new Item(new Item.Properties())));
    public static final RegistryObject<Item> REINFORCED_NETHER_STAR = CreativeTabRegister.add(ALL.register("reinforced_nether_star",
            () -> new Item(new Item.Properties().fireResistant())));
    public static final RegistryObject<Item> CALIBRATOR = CreativeTabRegister.add(ALL.register("calibrator",
            () -> new Item(new Item.Properties().durability(10))));
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
    public static final RegistryObject<Item> QUARRY_ENERGY_UPGRADE = CreativeTabRegister.add(ALL.register("energy_quarry_upgrade",
            QuarryEnergyOptimizationUpgradeItem::new));
    public static final RegistryObject<Item> ORE_FINDER_UPGRADE = CreativeTabRegister.add(ALL.register("ore_finder_quarry_upgrade",
            QuarryOreFinderUpgradeItem::new));
    public static final RegistryObject<Item> ADVANCED_QUARRY_SPEED_UPGRADE = CreativeTabRegister.add(ALL.register("advanced_speed_quarry_upgrade",
            AdvancedQuarrySpeedUpgradeItem::new));
    public static final RegistryObject<Item> ADVANCED_QUARRY_FORTUNE_UPGRADE = CreativeTabRegister.add(ALL.register("advanced_fortune_quarry_upgrade",
            AdvancedQuarryFortuneUpgradeItem::new));
    public static final RegistryObject<Item> ADVANCED_QUARRY_ENERGY_UPGRADE = CreativeTabRegister.add(ALL.register("advanced_energy_quarry_upgrade",
            AdvancedQuarryEnergyOptimizationUpgradeItem::new));
    public static final RegistryObject<Item> ADVANCED_ORE_FINDER_UPGRADE = CreativeTabRegister.add(ALL.register("advanced_ore_finder_quarry_upgrade",
            AdvancedQuarryOreFinderUpgradeItem::new));
    public static final RegistryObject<Item> COSMIC_QUARRY_SPEED_UPGRADE = CreativeTabRegister.add(ALL.register("cosmic_speed_quarry_upgrade",
            CosmicQuarrySpeedUpgradeItem::new));
    public static final RegistryObject<Item> COSMIC_QUARRY_FORTUNE_UPGRADE = CreativeTabRegister.add(ALL.register("cosmic_fortune_quarry_upgrade",
            CosmicQuarryFortuneUpgradeItem::new));
    public static final RegistryObject<Item> COSMIC_QUARRY_ENERGY_UPGRADE = CreativeTabRegister.add(ALL.register("cosmic_energy_quarry_upgrade",
            CosmicQuarryEnergyOptimizationUpgradeItem::new));
    public static final RegistryObject<Item> COSMIC_ORE_FINDER_UPGRADE = CreativeTabRegister.add(ALL.register("cosmic_ore_finder_quarry_upgrade",
            CosmicQuarryOreFinderUpgradeItem::new));


    public static final RegistryObject<Item> TRANSDIMENSIONAL_SWORD = CreativeTabRegister.add(ALL.register("transdimensional_sword",
            TransdimSword::new));
    public static final RegistryObject<Item> TRANSDIMENSIONAL_EXCAVATOR = CreativeTabRegister.add(ALL.register("transdimensional_excavator",
            TransdimExcavator::new));
}

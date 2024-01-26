package be.sixefyle.transdimquarry.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // TRANSDIM QUARRY
    public static final ForgeConfigSpec.ConfigValue<Long> TRANSDIMENSIONAL_QUARRY_BASE_ENERGY_COST;
    public static final ForgeConfigSpec.ConfigValue<Long> TRANSDIMENSIONAL_QUARRY_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> MINING_TIME;
    public static final ForgeConfigSpec.ConfigValue<Double> ORE_FIND_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BLACKLISTED_BLOCKS;

    //TOOL INFUSER
    public static final ForgeConfigSpec.ConfigValue<Integer> TOOL_INFUSER_MAX_PROGRESS;
    public static final ForgeConfigSpec.ConfigValue<Integer> TOOL_INFUSER_MIN_PROGRESS;
    public static final ForgeConfigSpec.ConfigValue<Long> TOOL_INFUSER_ENERGY_CAPACITY;

    //ITEM INFUSER
    public static final ForgeConfigSpec.ConfigValue<Long> ITEM_INFUSER_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> ITEM_INFUSER_TIME_TO_INFUSE;

    //FOUNDRY's
    //Simple foundry
    public static final ForgeConfigSpec.ConfigValue<Long> FOUNDRY_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Long> FOUNDRY_ENERGY_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> FOUNDRY_SMELT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> FOUNDRY_COOK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> FOUNDRY_INPUT_REDUCTION_CHANCE;
    //Advanced foundry
    public static final ForgeConfigSpec.ConfigValue<Long> ADVANCED_FOUNDRY_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Long> ADVANCED_FOUNDRY_ENERGY_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> ADVANCED_FOUNDRY_SMELT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ADVANCED_FOUNDRY_COOK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> ADVANCED_FOUNDRY_INPUT_REDUCTION_CHANCE;
    //Cosmic foundry
    public static final ForgeConfigSpec.ConfigValue<Long> COSMIC_FOUNDRY_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Long> COSMIC_FOUNDRY_ENERGY_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> COSMIC_FOUNDRY_SMELT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> COSMIC_FOUNDRY_COOK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> COSMIC_FOUNDRY_INPUT_REDUCTION_CHANCE;
    //Ethereal foundry
    public static final ForgeConfigSpec.ConfigValue<Long> ETHEREAL_FOUNDRY_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Long> ETHEREAL_FOUNDRY_ENERGY_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> ETHEREAL_FOUNDRY_SMELT_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ETHEREAL_FOUNDRY_COOK_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<Double> ETHEREAL_FOUNDRY_INPUT_REDUCTION_CHANCE;

    static {
        BUILDER.push("Machines");
        BUILDER.push("Quarry");
        TRANSDIMENSIONAL_QUARRY_BASE_ENERGY_COST = BUILDER.comment("Energy needed (in FE)").defineInRange("energyConsumption",32_500L, 1, Long.MAX_VALUE);
        TRANSDIMENSIONAL_QUARRY_ENERGY_CAPACITY = BUILDER.comment("Energy max capacity (in FE)").defineInRange("energyCapacity",200_000L, 1, Long.MAX_VALUE);
        MINING_TIME = BUILDER.comment("Time to mine (in ticks)").defineInRange("timeToMine",40, 1, Integer.MAX_VALUE);
        ORE_FIND_CHANCE = BUILDER.comment("Chance to find ore (default value is 0.07 and mean 7% chance to find ore)").defineInRange("oreFindChance",0.07, 0,1);
        BLACKLISTED_BLOCKS = BUILDER.comment("Blacklisted ores (Only work for ores). Example: [\"iron_ore\", \"coal_ore\"])").define("blacklistedOres", new ArrayList<>());

        BUILDER.pop();
        BUILDER.push("ToolInfuser");
        TOOL_INFUSER_ENERGY_CAPACITY = BUILDER.comment("Energy base capacity (in FE)").defineInRange("energyCapacity",1_000_000L, 1, Long.MAX_VALUE);
        TOOL_INFUSER_MIN_PROGRESS = BUILDER.comment("Minimal time to infuse (in ticks)").defineInRange("minProgress",2, 1, Integer.MAX_VALUE);
        TOOL_INFUSER_MAX_PROGRESS = BUILDER.comment("Maximal time to infuse (in ticks)").defineInRange("maxProgress",60, 1, Integer.MAX_VALUE);

        BUILDER.pop();
        BUILDER.push("ItemInfuser");
        ITEM_INFUSER_ENERGY_CAPACITY = BUILDER.comment("Energy max capacity (in FE)").defineInRange("energyCapacity",100_000L,1, Long.MAX_VALUE);
        ITEM_INFUSER_TIME_TO_INFUSE = BUILDER.comment("Time to infuse item (in ticks)").defineInRange("maxProgress",200, 1, Integer.MAX_VALUE);

        BUILDER.pop();
        BUILDER.push("Foundry");
        BUILDER.push("Simple");
        FOUNDRY_ENERGY_CAPACITY = BUILDER.comment("Energy max capacity (in FE) ").defineInRange("energyCapacity", 10_000, 1, Long.MAX_VALUE);
        FOUNDRY_ENERGY_COST = BUILDER.comment("Energy needed (in FE)").defineInRange("energyCost", 80, 1, Long.MAX_VALUE);
        FOUNDRY_SMELT_TIME = BUILDER.comment("Time needed to smelt an item").defineInRange("smeltTime", 80, 1, Integer.MAX_VALUE);
        FOUNDRY_COOK_MULTIPLIER = BUILDER.comment("Amount of items smelted at once").defineInRange("cookMultiplier", 4, 1, Integer.MAX_VALUE);
        FOUNDRY_INPUT_REDUCTION_CHANCE = BUILDER.comment("Chance to reduce the input consumption to one").defineInRange("inputReductionChance", 0.0, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.push("Advanced");
        ADVANCED_FOUNDRY_ENERGY_CAPACITY = BUILDER.comment("Energy max capacity (in FE) ").defineInRange("energyCapacity", 40_000L, 1, Long.MAX_VALUE);
        ADVANCED_FOUNDRY_ENERGY_COST = BUILDER.comment("Energy needed (in FE)").defineInRange("energyCost", 320, 1, Long.MAX_VALUE);
        ADVANCED_FOUNDRY_SMELT_TIME = BUILDER.comment("Time needed to smelt an item").defineInRange("smeltTime", 40, 1, Integer.MAX_VALUE);
        ADVANCED_FOUNDRY_COOK_MULTIPLIER = BUILDER.comment("Amount of items smelted at once").defineInRange("cookMultiplier", 8, 1, Integer.MAX_VALUE);
        ADVANCED_FOUNDRY_INPUT_REDUCTION_CHANCE = BUILDER.comment("Chance to reduce the input consumption to one").defineInRange("inputReductionChance", 0.0, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.push("Cosmic");
        COSMIC_FOUNDRY_ENERGY_CAPACITY = BUILDER.comment("Energy max capacity (in FE) ").defineInRange("energyCapacity", 180_000L, 1, Long.MAX_VALUE);
        COSMIC_FOUNDRY_ENERGY_COST = BUILDER.comment("Energy needed (in FE)").defineInRange("energyCost", 1280, 1, Long.MAX_VALUE);
        COSMIC_FOUNDRY_SMELT_TIME = BUILDER.comment("Time needed to smelt an item").defineInRange("smeltTime", 20, 1, Integer.MAX_VALUE);
        COSMIC_FOUNDRY_COOK_MULTIPLIER = BUILDER.comment("Amount of items smelted at once").defineInRange("cookMultiplier", 16, 1, Integer.MAX_VALUE);
        COSMIC_FOUNDRY_INPUT_REDUCTION_CHANCE = BUILDER.comment("Chance to reduce the input consumption to one").defineInRange("inputReductionChance", 0.01, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.push("Ethereal");
        ETHEREAL_FOUNDRY_ENERGY_CAPACITY = BUILDER.comment("Energy max capacity (in FE) ").defineInRange("energyCapacity", 360_000L, 1, Long.MAX_VALUE);
        ETHEREAL_FOUNDRY_ENERGY_COST = BUILDER.comment("Energy needed (in FE)").defineInRange("energyCost", 10_240L, 1, Long.MAX_VALUE);
        ETHEREAL_FOUNDRY_SMELT_TIME = BUILDER.comment("Time needed to smelt an item").defineInRange("smeltTime", 10, 1, Integer.MAX_VALUE);
        ETHEREAL_FOUNDRY_COOK_MULTIPLIER = BUILDER.comment("Amount of items smelted at once").defineInRange("cookMultiplier", 32, 1, Integer.MAX_VALUE);
        ETHEREAL_FOUNDRY_INPUT_REDUCTION_CHANCE = BUILDER.comment("Chance to reduce the input consumption to one").defineInRange("inputReductionChance", 0.05, 0.0, 1.0);
        BUILDER.pop();
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}

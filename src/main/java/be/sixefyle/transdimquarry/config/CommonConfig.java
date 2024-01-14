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


    static {
        BUILDER.push("Machines");
        BUILDER.push("Quarry");
        TRANSDIMENSIONAL_QUARRY_BASE_ENERGY_COST = BUILDER.comment("Base energy needed (in FE)").defineInRange("energyConsumption",32_500L, 1, Long.MAX_VALUE);
        TRANSDIMENSIONAL_QUARRY_ENERGY_CAPACITY = BUILDER.comment("Base energy capatity (in FE)").defineInRange("energyCapacity",52_500_000L, 1, Long.MAX_VALUE);
        MINING_TIME = BUILDER.comment("Time to mine (in ticks)").defineInRange("timeToMine",40, 1, Integer.MAX_VALUE);
        ORE_FIND_CHANCE = BUILDER.comment("Chance to find ore (default value is 0.07 and mean 7% chance to find ore)").defineInRange("oreFindChance",0.07, 0,1);
        BLACKLISTED_BLOCKS = BUILDER.comment("Blacklisted ores (Only work for ores). Example: [\"iron_ore\", \"coal_ore\"])").define("blacklistedOres", new ArrayList<>());

        BUILDER.pop();
        BUILDER.push("ToolInfuser");
        TOOL_INFUSER_ENERGY_CAPACITY = BUILDER.comment("Tool infuser energy base capacity (in FE)").defineInRange("energyCapacity",1_000_000L, 1, Long.MAX_VALUE);
        TOOL_INFUSER_MIN_PROGRESS = BUILDER.comment("Minimal time to infuse (in ticks)").defineInRange("minProgress",2, 1, Integer.MAX_VALUE);
        TOOL_INFUSER_MAX_PROGRESS = BUILDER.comment("Maximal time to infuse (in ticks)").defineInRange("maxProgress",60, 1, Integer.MAX_VALUE);

        BUILDER.pop();
        BUILDER.push("ItemInfuser");
        ITEM_INFUSER_ENERGY_CAPACITY = BUILDER.comment("Item infuser energy max capacity (in FE)").defineInRange("energyCapacity",100_000L,1, Long.MAX_VALUE);
        ITEM_INFUSER_TIME_TO_INFUSE = BUILDER.comment("Time to infuse item (in ticks)").defineInRange("maxProgress",200, 1, Integer.MAX_VALUE);

        SPEC = BUILDER.build();
    }
}

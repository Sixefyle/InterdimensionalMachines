package be.sixefyle.transdimquarry.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // QUARRY
    public static final ForgeConfigSpec.ConfigValue<Integer> QUARRY_BASE_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> MINING_TIME;
    public static final ForgeConfigSpec.ConfigValue<Double> ORE_FIND_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BLACKLISTED_BLOCKS;

    //TOOL INFUSER
    public static final ForgeConfigSpec.ConfigValue<Integer> TOOL_INFUSER_MAX_PROGRESS;
    public static final ForgeConfigSpec.ConfigValue<Integer> TOOL_INFUSER_MIN_PROGRESS;
    public static final ForgeConfigSpec.ConfigValue<Integer> TOOL_INFUSER_ENERGY_CAPACITY;


    static {
        BUILDER.push("Machines");
        BUILDER.push("Quarry");

        QUARRY_BASE_COST = BUILDER.comment("Base Energy Needed (in FE/t)").define("energyConsumption",32500);
        MINING_TIME = BUILDER.comment("Time To Mine (in ticks)").defineInRange("timeToMine",10, 10, Integer.MAX_VALUE);
        ORE_FIND_CHANCE = BUILDER.comment("Chance To Find Ore (default value is 0.07 and mean 7% chance to find ore)").defineInRange("oreFindChance",0.07, 0,1);
        BLACKLISTED_BLOCKS = BUILDER.comment("Blacklisted Ores (Only work for ores, example: [\"iron_ore\", \"coal_ore\"])").define("blacklistedOres", new ArrayList<>());

        BUILDER.pop();
        BUILDER.push("ToolInfuser");
        TOOL_INFUSER_ENERGY_CAPACITY = BUILDER.comment("Tool Infuser Energy Max Capacity (in FE)").define("energyCapacity",100000000);
        TOOL_INFUSER_MIN_PROGRESS = BUILDER.comment("Minimal Time To Infuse (in ticks)").defineInRange("minProgress",2, 1, Integer.MAX_VALUE);
        TOOL_INFUSER_MAX_PROGRESS = BUILDER.comment("Maximal Time To Infuse (in ticks)").defineInRange("maxProgress",60, 1, Integer.MAX_VALUE);

        SPEC = BUILDER.build();
    }
}

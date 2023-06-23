package be.sixefyle.transdimquarry.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> QUARRY_BASE_COST;
    public static final ForgeConfigSpec.ConfigValue<Integer> MINING_TIME;
    public static final ForgeConfigSpec.ConfigValue<Double> ORE_FIND_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<String>> BLACKLISTED_BLOCKS;

    static {
        BUILDER.push("Transdimensional Machines Config");
        BUILDER.push("Quarry");

        QUARRY_BASE_COST = BUILDER.comment("Base Energy Needed (in FE/t)").define("Energy Consumption",32500);
        MINING_TIME = BUILDER.comment("Time To Mine (in ticks)").defineInRange("Time To Mine",10, 10,200);
        ORE_FIND_CHANCE = BUILDER.comment("Chance To Find Ore (default value is 0.07 and mean 7% chance to find ore)").defineInRange("Ore Find Chance",0.07, 0,1);
        BLACKLISTED_BLOCKS = BUILDER.comment("Blacklisted Ores (Only work for ores, example: [\"iron_ore\", \"coal_ore\"])").define("Blacklisted Ores", new ArrayList<>());

        SPEC = BUILDER.build();
    }
}

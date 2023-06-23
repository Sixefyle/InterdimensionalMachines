package be.sixefyle.transdimquarry.utils;

import be.sixefyle.transdimquarry.config.CommonConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OreUtils {

    private final static List<Block> ores = new ArrayList<>();

    public static boolean checkString(String input) {
        String pattern = "_ore$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        return matcher.find();
    }

    public static boolean isBlackListed(Block block){
        for (String blockName : CommonConfig.BLACKLISTED_BLOCKS.get()) {
            if(block.getDescriptionId().contains(blockName))
                return true;
        }

        return false;
    }

    public static void initRegisteredOreList(){
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        for (Block block : blocks) {
            if(checkString(block.getDescriptionId())){
                if(!isBlackListed(block)){
                    ores.add(block);
                }
            }
        }

        if(!isBlackListed(Blocks.ANCIENT_DEBRIS))
            ores.add(Blocks.ANCIENT_DEBRIS);
    }

    public static List<Block> getOres() {
        return ores;
    }
}

package be.sixefyle.transdimquarry.utils;

import be.sixefyle.transdimquarry.config.CommonConfig;
import net.minecraft.Util;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.OreVeinifier;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OreUtils {

    private final static List<Block> ores = new ArrayList<>();

//    public static boolean checkString(String input) {
//        String pattern = "_ore$";
//        Pattern regex = Pattern.compile(pattern);
//        Matcher matcher = regex.matcher(input);
//        return matcher.find();
//    }

    public static boolean isTaggedForgeOre(BlockState blockState){
        for (TagKey<Block> blockTagKey : blockState.getTags().toList()) {
            if(blockTagKey.toString().contains("forge:ores")){
                return true;
            }
        }
        return false;
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
            if(isTaggedForgeOre(block.defaultBlockState())){
                if(!isBlackListed(block)){
                    ores.add(block);
                }
            }
        }
    }

    public static List<Block> getOres() {
        return ores;
    }
}

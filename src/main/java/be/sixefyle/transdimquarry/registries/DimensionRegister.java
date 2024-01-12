package be.sixefyle.transdimquarry.registries;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class DimensionRegister {
    private final static ResourceLocation COSMIC_BOUNDARY_ID =
            new ResourceLocation(TransdimensionalMachines.MODID, "cosmic_boundary");
    public static final ResourceKey<Level> COSMIC_BOUNDARY_KEY =
            ResourceKey.create(Registries.DIMENSION, COSMIC_BOUNDARY_ID);
    public static final ResourceKey<DimensionType> COSMIC_BOUNDARY_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, COSMIC_BOUNDARY_ID);
    public static final ResourceKey<LevelStem> AETHER_LEVEL_STEM =
            ResourceKey.create(Registries.LEVEL_STEM, COSMIC_BOUNDARY_ID);
    public static void register() {
        System.out.println("Registering dimensions for " + TransdimensionalMachines.MODID);
    }
}

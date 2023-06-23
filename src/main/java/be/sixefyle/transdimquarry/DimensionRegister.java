package be.sixefyle.transdimquarry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionRegister {
    public static final ResourceKey<Level> DIMENSION_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY,
            new ResourceLocation(TransdimensionalMachines.MODID, "mining_dimension"));
    public static final ResourceKey<DimensionType> DIMENSION_TYPE =
            ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, DIMENSION_KEY.location());
    public static void register() {
        System.out.println("Registering Dimensions for " + TransdimensionalMachines.MODID);
    }
}

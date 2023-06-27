package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.blocks.quarry.TransdimQuarryBlockEntity;
import be.sixefyle.transdimquarry.blocks.toolinfuser.TransdimToolInfuserBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>> ALL =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TransdimensionalMachines.MODID);

    public static final RegistryObject<BlockEntityType<TransdimQuarryBlockEntity>> TRANSDIMENSIONAL_QUARRY = ALL.register("transdimensional_quarry",
            () -> BlockEntityType.Builder.of(TransdimQuarryBlockEntity::new, BlockRegister.TRANSDIMENSIONAL_QUARRY.get()).build(null));

    public static final RegistryObject<BlockEntityType<TransdimToolInfuserBlockEntity>> TRANSDIMENSIONAL_TOOL_INFUSER = ALL.register("transdimensional_tool_infuser",
            () -> BlockEntityType.Builder.of(TransdimToolInfuserBlockEntity::new, BlockRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get()).build(null));

}

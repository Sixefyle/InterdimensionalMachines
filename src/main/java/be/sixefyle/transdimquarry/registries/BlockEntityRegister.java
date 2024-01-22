package be.sixefyle.transdimquarry.registries;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.foundry.advancedfoundry.AdvancedFoundryBlockEntity;
import be.sixefyle.transdimquarry.blocks.foundry.cosmicfoundry.CosmicFoundryBlockEntity;
import be.sixefyle.transdimquarry.blocks.foundry.etherealfoundry.EtherealFoundryBlockEntity;
import be.sixefyle.transdimquarry.blocks.foundry.foundry.FoundryBlockEntity;
import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserBlockEntity;
import be.sixefyle.transdimquarry.blocks.multiblock.test.TestMultiblockBlockEntity;
import be.sixefyle.transdimquarry.blocks.quarries.transdimquarry.TransdimQuarryBlockEntity;
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

    public static final RegistryObject<BlockEntityType<ItemInfuserBlockEntity>> ITEM_INFUSER = ALL.register("item_infuser",
            () -> BlockEntityType.Builder.of(ItemInfuserBlockEntity::new, BlockRegister.ITEM_INFUSER.get()).build(null));
    public static final RegistryObject<BlockEntityType<FoundryBlockEntity>> FOUNDRY = ALL.register("foundry",
            () -> BlockEntityType.Builder.of(FoundryBlockEntity::new, BlockRegister.FOUNDRY.get()).build(null));
    public static final RegistryObject<BlockEntityType<AdvancedFoundryBlockEntity>> ADVANCED_FOUNDRY = ALL.register("advanced_foundry",
            () -> BlockEntityType.Builder.of(AdvancedFoundryBlockEntity::new, BlockRegister.ADVANCED_FOUNDRY.get()).build(null));
    public static final RegistryObject<BlockEntityType<CosmicFoundryBlockEntity>> COSMIC_FOUNDRY = ALL.register("cosmic_foundry",
            () -> BlockEntityType.Builder.of(CosmicFoundryBlockEntity::new, BlockRegister.COSMIC_FOUNDRY.get()).build(null));
    public static final RegistryObject<BlockEntityType<EtherealFoundryBlockEntity>> ETHEREAL_FOUNDRY = ALL.register("ethereal_foundry",
            () -> BlockEntityType.Builder.of(EtherealFoundryBlockEntity::new, BlockRegister.ETHEREAL_FOUNDRY.get()).build(null));

    public static final RegistryObject<BlockEntityType<TestMultiblockBlockEntity>> TEST_MB = null; //= ALL.register("test_mb",
            //() -> BlockEntityType.Builder.of(TestMultiblockBlockEntity::new, BlockRegister.TEST_CASING.get()).build(null));
}

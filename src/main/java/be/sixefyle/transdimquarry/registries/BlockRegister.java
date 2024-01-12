package be.sixefyle.transdimquarry.registries;

import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserBlock;
import be.sixefyle.transdimquarry.blocks.quarries.transdimquarry.TransdimQuarryBlock;
import be.sixefyle.transdimquarry.blocks.toolinfuser.TransdimToolInfuserBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.common.Mod;


import java.util.function.Supplier;

import static be.sixefyle.transdimquarry.TransdimensionalMachines.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegister {

    public static final DeferredRegister<Block> ALL = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> TRANSDIMENSIONAL_QUARRY = CreativeTabRegister.add(ALL.register("transdimensional_quarry",
            () -> new TransdimQuarryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))));
//    public static final RegistryObject<Block> DIMENSIONAL_QUARRY = CreativeTabRegister.add(ALL.register("dimensional_quarry",
//            () -> new DimensionalQuarryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))));

    public static final RegistryObject<Block> TRANSDIMENSIONAL_TOOL_INFUSER = CreativeTabRegister.add(ALL.register("transdimensional_tool_infuser",
            () -> new TransdimToolInfuserBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))));
    public static final RegistryObject<Block> ITEM_INFUSER = CreativeTabRegister.add(ALL.register("item_infuser",
            () -> new ItemInfuserBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))));

    public static final RegistryObject<Block> QUANTUMITE_ORE = CreativeTabRegister.add(ALL.register("quantumite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .strength(25f, 100f),
                    UniformInt.of(4, 8))));
    public static final RegistryObject<Block> QUANTUMITE_BLOCK = CreativeTabRegister.add(ALL.register("quantumite_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(25f))));

    @SubscribeEvent
    public static void onRegisterItems(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            ALL.getEntries().forEach( (blockRegistryObject) -> {
                Block block = blockRegistryObject.get();
                Item.Properties properties = new Item.Properties();
                Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
                event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
            });
        }
    }
}
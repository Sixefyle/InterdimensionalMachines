package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.blocks.quarry.TransdimQuarryBlock;
import be.sixefyle.transdimquarry.blocks.toolinfuser.TransdimToolInfuserBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

    public static final RegistryObject<Block> TRANSDIMENSIONAL_QUARRY = ALL.register("transdimensional_quarry",
            () -> new TransdimQuarryBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    public static final RegistryObject<Block> TRANSDIMENSIONAL_TOOL_INFUSER = ALL.register("transdimensional_tool_infuser",
            () -> new TransdimToolInfuserBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
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

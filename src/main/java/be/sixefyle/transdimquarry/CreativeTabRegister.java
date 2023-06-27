package be.sixefyle.transdimquarry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabRegister {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            TransdimensionalMachines.MODID);

    public static RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("transdimensional_tab", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemRegister.QUARRY_FORTUNE_UPGRADE.get()))
                    .title(Component.translatable("transdimensionalmachines.tab"))
                    .displayItems((displayParameters, output) -> {
                        output.accept(ItemRegister.EMPTY_QUARRY_UPGRADE.get());
                        output.accept(ItemRegister.ENERGY_UPGRADE.get());
                        output.accept(ItemRegister.QUARRY_FORTUNE_UPGRADE.get());
                        output.accept(ItemRegister.ORE_FINDER_UPGRADE.get());
                        output.accept(ItemRegister.QUARRY_SILK_UPGRADE.get());
                        output.accept(ItemRegister.QUARRY_SPEED_UPGRADE.get());
                        output.accept(ItemRegister.TRANSDIM_SWORD.get());

                        output.accept(BlockRegister.TRANSDIMENSIONAL_QUARRY.get());
                        output.accept(BlockRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get());
                    })
                    .build());

}

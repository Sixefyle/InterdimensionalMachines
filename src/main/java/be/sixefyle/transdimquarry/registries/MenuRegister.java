package be.sixefyle.transdimquarry.registries;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserMenu;
import be.sixefyle.transdimquarry.blocks.quarries.transdimquarry.TransdimQuarryMenu;
import be.sixefyle.transdimquarry.blocks.toolinfuser.TransdimToolInfuserMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegister {
    public static final DeferredRegister<MenuType<?>> ALL =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TransdimensionalMachines.MODID);

    public static final RegistryObject<MenuType<TransdimQuarryMenu>> TRANSDIMENSIONAL_QUARRY =
        registerMenuType(TransdimQuarryMenu::new, "transdimensional_quarry_menu");

    public static final RegistryObject<MenuType<TransdimToolInfuserMenu>> TRANSDIMENSIONAL_TOOL_INFUSER =
            registerMenuType(TransdimToolInfuserMenu::new, "transdimensional_tool_infuser_menu");

    public static final RegistryObject<MenuType<ItemInfuserMenu>> ITEM_INFUSER =
            registerMenuType(ItemInfuserMenu::new, "item_infuser_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return ALL.register(name, () -> IForgeMenuType.create(factory));
    }

}

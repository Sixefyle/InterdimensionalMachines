package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.screen.TransdimQuarryMenu;
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

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return ALL.register(name, () -> IForgeMenuType.create(factory));
    }

}

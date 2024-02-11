package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.blocks.foundry.advanced.AdvancedFoundryScreen;
import be.sixefyle.transdimquarry.blocks.foundry.cosmic.CosmicFoundryScreen;
import be.sixefyle.transdimquarry.blocks.foundry.ethereal.EtherealFoundryScreen;
import be.sixefyle.transdimquarry.blocks.foundry.foundry.FoundryScreen;
import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserScreen;
import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterMenu;
import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterScreen;
import be.sixefyle.transdimquarry.blocks.soulharverster.advanced.AdvancedSoulHarvesterMenu;
import be.sixefyle.transdimquarry.blocks.soulharverster.cosmic.CosmicSoulHarvesterMenu;
import be.sixefyle.transdimquarry.blocks.soulharverster.ethereal.EtherealSoulHarvesterMenu;
import be.sixefyle.transdimquarry.blocks.toolinfuser.TransdimToolInfuserScreen;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.dimension.CosmicBoundaryRender;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.blocks.quarries.transdimquarry.TransdimQuarryScreen;
import be.sixefyle.transdimquarry.recipes.iteminfuser.ItemInfuserRecipeRegister;
import be.sixefyle.transdimquarry.registries.*;
import be.sixefyle.transdimquarry.utils.OreUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TransdimensionalMachines.MODID)
public class TransdimensionalMachines
{
    public static final String MODID = "transdimensionalmachines";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TransdimensionalMachines()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BlockRegister.ALL.register(modEventBus);
        ItemRegister.ALL.register(modEventBus);
        BlockEntityRegister.ALL.register(modEventBus);
        MenuRegister.ALL.register(modEventBus);
        CreativeTabRegister.CREATIVE_MODE_TABS.register(modEventBus);


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "transdimensional-machines.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        PacketSender.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event){
        OreUtils.initRegisteredOreList();
        ItemInfuserRecipeRegister.register();
    }


    public static ResourceLocation resourceLocation(String path){
        return new ResourceLocation(MODID, path);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(MenuRegister.TRANSDIMENSIONAL_QUARRY.get(), TransdimQuarryScreen::new);
            MenuScreens.register(MenuRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get(), TransdimToolInfuserScreen::new);
            MenuScreens.register(MenuRegister.ITEM_INFUSER.get(), ItemInfuserScreen::new);
            MenuScreens.register(MenuRegister.FOUNDRY.get(), FoundryScreen::new);
            MenuScreens.register(MenuRegister.ADVANCED_FOUNDRY.get(), AdvancedFoundryScreen::new);
            MenuScreens.register(MenuRegister.COSMIC_FOUNDRY.get(), CosmicFoundryScreen::new);
            MenuScreens.register(MenuRegister.ETHEREAL_FOUNDRY.get(), EtherealFoundryScreen::new);

            MenuScreens.register(MenuRegister.SOUL_MANIPULATOR.get(), SoulHarvesterScreen<SoulHarvesterMenu>::new);
            MenuScreens.register(MenuRegister.ADVANCED_SOUL_MANIPULATOR.get(), SoulHarvesterScreen<AdvancedSoulHarvesterMenu>::new);
            MenuScreens.register(MenuRegister.COSMIC_SOUL_MANIPULATOR.get(), SoulHarvesterScreen<CosmicSoulHarvesterMenu>::new);
            MenuScreens.register(MenuRegister.ETHEREAL_SOUL_MANIPULATOR.get(), SoulHarvesterScreen<EtherealSoulHarvesterMenu>::new);
            ItemInfuserRecipeRegister.register();
        }

        @SubscribeEvent
        public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event){
            event.register(DimensionRegister.COSMIC_BOUNDARY_TYPE.location(), new CosmicBoundaryRender());
        }
    }
}

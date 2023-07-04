package be.sixefyle.transdimquarry;

import be.sixefyle.transdimquarry.blocks.toolinfuser.TransdimToolInfuserScreen;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.dimension.CosmicBoundaryRender;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.blocks.quarry.TransdimQuarryScreen;
import be.sixefyle.transdimquarry.utils.OreUtils;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
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

import java.awt.*;

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
    }


    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(MenuRegister.TRANSDIMENSIONAL_QUARRY.get(), TransdimQuarryScreen::new);
            MenuScreens.register(MenuRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get(), TransdimToolInfuserScreen::new);
        }

        @SubscribeEvent
        public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event){
            event.register(DimensionRegister.COSMIC_BOUNDARY_TYPE.location(), new CosmicBoundaryRender());
        }
    }
}

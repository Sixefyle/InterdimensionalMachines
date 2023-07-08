package be.sixefyle.transdimquarry.key;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.items.tools.IModeHandle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class KeyEvent {
    @Mod.EventBusSubscriber(modid = TransdimensionalMachines.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent event) {
            if(KeyBinding.TOOL_SETTINGS_KEY.isDown()) {
                ItemStack itemStack = Minecraft.getInstance().player.getMainHandItem();
                if(itemStack.getItem() instanceof IModeHandle mode){
                    Minecraft.getInstance().setScreen(mode.getScreen(itemStack));
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = TransdimensionalMachines.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.TOOL_SETTINGS_KEY);
        }
    }
}

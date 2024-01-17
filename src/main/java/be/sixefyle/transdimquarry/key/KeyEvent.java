package be.sixefyle.transdimquarry.key;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.items.tools.IModeHandle;
import be.sixefyle.transdimquarry.utils.LevelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
                Player player = Minecraft.getInstance().player;
                Level level = player.level();
                HitResult hitResult = Minecraft.getInstance().hitResult;

                if(hitResult.getType() == HitResult.Type.BLOCK
                        && hitResult instanceof BlockHitResult blockHitResult){
                    BlockState blockState = level.getBlockState(blockHitResult.getBlockPos());
                    if(blockState.hasBlockEntity()) return;
                }

                ItemStack itemStack = player.getMainHandItem();
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

package be.sixefyle.transdimquarry.networking.packet.cts;

import be.sixefyle.transdimquarry.items.tools.IModeHandle;
import be.sixefyle.transdimquarry.items.tools.InfusedTool;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChangeToolModePacket {

    int width, height, speed;


    public ChangeToolModePacket(int speed, int width, int height){
        this.speed = speed;
        this.width = width;
        this.height = height;
    }

    public ChangeToolModePacket(FriendlyByteBuf buf){
        this.speed = buf.readInt();
        this.width =  buf.readInt();
        this.height =  buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(speed);
        buf.writeInt(width);
        buf.writeInt(height);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if(player == null) return;

            ItemStack itemStack = player.getMainHandItem();

            if(itemStack.getItem() instanceof IModeHandle mode){
                mode.onChangeMode(itemStack, speed, width, height);
            }
        });
    }
}

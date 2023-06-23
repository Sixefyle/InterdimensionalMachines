package be.sixefyle.transdimquarry.networking.packet.cts;

import be.sixefyle.transdimquarry.blocks.entity.TransdimQuarryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchQuarryStatePacket {

    private final BlockPos quarryPos;

    public SwitchQuarryStatePacket(BlockPos pos){
        this.quarryPos = pos;
    }

    public SwitchQuarryStatePacket(FriendlyByteBuf buf){
        this.quarryPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBlockPos(quarryPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel world = player.getLevel();

            TransdimQuarryBlockEntity blockEntity = (TransdimQuarryBlockEntity) world.getBlockEntity(quarryPos);
            if(blockEntity == null) return;

            blockEntity.setWorking(!blockEntity.isWorking());
            blockEntity.setProgress(0);
        });
    }
}

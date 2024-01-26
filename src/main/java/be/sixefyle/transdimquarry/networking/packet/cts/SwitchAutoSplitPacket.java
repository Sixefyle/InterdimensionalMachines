package be.sixefyle.transdimquarry.networking.packet.cts;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundry;
import be.sixefyle.transdimquarry.blocks.quarries.transdimquarry.TransdimQuarryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchAutoSplitPacket {

    private final BlockPos pos;

    public SwitchAutoSplitPacket(BlockPos pos){
        this.pos = pos;
    }

    public SwitchAutoSplitPacket(FriendlyByteBuf buf){
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBlockPos(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel world = player.serverLevel();

            BaseFoundry blockEntity = (BaseFoundry) world.getBlockEntity(pos);
            if(blockEntity == null) return;

            blockEntity.setAutoSplit(!blockEntity.isAutoSplit());
        });
    }
}

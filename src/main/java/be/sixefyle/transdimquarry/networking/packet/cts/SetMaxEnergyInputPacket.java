package be.sixefyle.transdimquarry.networking.packet.cts;

import be.sixefyle.transdimquarry.blocks.BaseEnergyContainerBlockEntity;
import be.sixefyle.transdimquarry.blocks.IEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetMaxEnergyInputPacket {

    private final BlockPos blockpos;
    private final long newAmount;

    public SetMaxEnergyInputPacket(BlockPos pos, long newAmount){
        this.blockpos = pos;
        this.newAmount = newAmount;
    }

    public SetMaxEnergyInputPacket(FriendlyByteBuf buf){
        this.blockpos = buf.readBlockPos();
        this.newAmount = buf.readLong();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBlockPos(blockpos);
        buf.writeLong(newAmount);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel world = player.serverLevel();

            if(world.getBlockEntity(blockpos) instanceof BaseEnergyContainerBlockEntity blockEntity){
               blockEntity.setEnergyNeeded(newAmount);
               blockEntity.setEnergyCapacity((long)(newAmount * 1.1));
            }
        });
    }
}

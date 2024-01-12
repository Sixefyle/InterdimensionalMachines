package be.sixefyle.transdimquarry.networking.packet.cts;

import be.sixefyle.transdimquarry.blocks.IEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetMaxEnergyInputPacket {

    private final BlockPos blockpos;
    private final int newAmount;

    public SetMaxEnergyInputPacket(BlockPos pos, int newAmount){
        this.blockpos = pos;
        this.newAmount = newAmount;
    }

    public SetMaxEnergyInputPacket(FriendlyByteBuf buf){
        this.blockpos = buf.readBlockPos();
        this.newAmount = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBlockPos(blockpos);
        buf.writeInt(newAmount);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel world = player.serverLevel();

            if(world.getBlockEntity(blockpos) instanceof IEnergyHandler energyHandler){
                energyHandler.setMaxEnergyInput(newAmount);
            }
        });
    }
}

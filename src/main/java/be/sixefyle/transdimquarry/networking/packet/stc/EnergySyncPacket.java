package be.sixefyle.transdimquarry.networking.packet.stc;

import be.sixefyle.transdimquarry.blocks.BaseEnergyContainerBlockEntity;
import be.sixefyle.transdimquarry.blocks.quarries.QuarryBaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnergySyncPacket {
    private final long energy;
    private final BlockPos pos;

    public EnergySyncPacket(long energy, BlockPos pos) {
        this.energy = energy;
        this.pos = pos;
    }

    public EnergySyncPacket(FriendlyByteBuf buf) {
        this.energy = buf.readLong();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(energy);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BaseEnergyContainerBlockEntity baseEnergyContainerBlockEntity) {
                baseEnergyContainerBlockEntity.setEnergy(energy);
            }
        });
        return true;
    }
}

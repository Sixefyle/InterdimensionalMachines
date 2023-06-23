package be.sixefyle.transdimquarry.networking;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.networking.packet.cts.SwitchQuarryStatePacket;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketSender {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {

        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TransdimensionalMachines.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SwitchQuarryStatePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SwitchQuarryStatePacket::new)
                .encoder(SwitchQuarryStatePacket::toBytes)
                .consumerMainThread(SwitchQuarryStatePacket::handle)
                .add();


        net.messageBuilder(EnergySyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EnergySyncPacket::new)
                .encoder(EnergySyncPacket::toBytes)
                .consumerMainThread(EnergySyncPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}

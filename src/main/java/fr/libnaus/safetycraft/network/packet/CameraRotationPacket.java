package fr.libnaus.safetycraft.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CameraRotationPacket {
    private final float yaw;
    private final float pitch;

    public CameraRotationPacket(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static void encode(CameraRotationPacket packet, FriendlyByteBuf buf) {
        buf.writeFloat(packet.yaw);
        buf.writeFloat(packet.pitch);
    }

    public static CameraRotationPacket decode(FriendlyByteBuf buf) {
        return new CameraRotationPacket(buf.readFloat(), buf.readFloat());
    }

    public static void handle(CameraRotationPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                player.setYRot(packet.yaw);
                player.setXRot(packet.pitch);
            }
        });
        context.setPacketHandled(true);
    }
}

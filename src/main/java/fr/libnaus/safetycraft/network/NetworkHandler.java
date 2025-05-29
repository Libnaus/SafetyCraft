package fr.libnaus.safetycraft.network;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.network.packet.CameraRotationPacket;
import fr.libnaus.safetycraft.network.packet.CameraTeleportPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(Safetycraft.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void registerPackets() {
        INSTANCE.registerMessage(packetId++, CameraTeleportPacket.class,
                CameraTeleportPacket::encode,
                CameraTeleportPacket::decode,
                CameraTeleportPacket::handle);

        INSTANCE.registerMessage(packetId++, CameraRotationPacket.class,
                CameraRotationPacket::encode,
                CameraRotationPacket::decode,
                CameraRotationPacket::handle);
    }
}

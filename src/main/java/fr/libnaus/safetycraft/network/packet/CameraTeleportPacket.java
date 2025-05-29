package fr.libnaus.safetycraft.network.packet;

import fr.libnaus.safetycraft.blocks.CameraBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CameraTeleportPacket {
    private final BlockPos cameraPos;
    private final boolean activate;

    public CameraTeleportPacket(BlockPos cameraPos, boolean activate) {
        this.cameraPos = cameraPos;
        this.activate = activate;
    }

    public static void encode(CameraTeleportPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.cameraPos);
        buf.writeBoolean(packet.activate);
    }

    public static CameraTeleportPacket decode(FriendlyByteBuf buf) {
        return new CameraTeleportPacket(buf.readBlockPos(), buf.readBoolean());
    }

    public static void handle(CameraTeleportPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (packet.activate) {
                    double x = packet.cameraPos.getX() + 0.5;
                    double y = packet.cameraPos.getY() + 1.5;
                    double z = packet.cameraPos.getZ() + 0.5;

//                    player.teleportTo(x, y, z);

                    if (player.level().getBlockState(packet.cameraPos).getBlock() instanceof CameraBlock) {
                        var state = player.level().getBlockState(packet.cameraPos);
                        if (state.hasProperty(BlockStateProperties.FACING)) {
                            var facing = state.getValue(BlockStateProperties.FACING);
                            float yaw = switch (facing) {
                                case NORTH -> 180f;
                                case SOUTH -> 0f;
                                case WEST -> 90f;
                                case EAST -> -90f;
                                default -> player.getYRot();
                            };
                            float pitch = switch (facing) {
                                case UP -> -90f;
                                case DOWN -> 90f;
                                default -> 0f;
                            };

//                            player.setYRot(yaw);
//                            player.setXRot(pitch);
                        }
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}

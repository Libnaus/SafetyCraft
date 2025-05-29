package fr.libnaus.safetycraft.client;

import fr.libnaus.safetycraft.network.NetworkHandler;
import fr.libnaus.safetycraft.network.packet.CameraRotationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CameraViewHandler {
    private static boolean cameraActive = false;
    private static BlockPos cameraPos = null;
    private static Direction cameraDirection = Direction.NORTH;
    private static float cameraYaw = 0f;
    private static float cameraPitch = 0f;

    public static void activateCameraView(BlockPos pos) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {

            cameraActive = true;
            setCameraPos(pos);

            BlockState state = mc.level.getBlockState(pos);
            if (state.hasProperty(BlockStateProperties.FACING)) {
                cameraDirection = state.getValue(BlockStateProperties.FACING);
                switch (cameraDirection) {
                    case NORTH -> { cameraYaw = 180f; cameraPitch = 0f; }
                    case SOUTH -> { cameraYaw = 0f; cameraPitch = 0f; }
                    case WEST -> { cameraYaw = 90f; cameraPitch = 0f; }
                    case EAST -> { cameraYaw = -90f; cameraPitch = 0f; }
                    case UP -> { cameraYaw = 0f; cameraPitch = -90f; }
                    case DOWN -> { cameraYaw = 0f; cameraPitch = 90f; }
                }
            }

            NetworkHandler.INSTANCE.sendToServer(new CameraRotationPacket(cameraYaw, cameraPitch));
        }
    }


    public static void deactivateCameraView() {
        if (cameraActive) {
            cameraActive = false;
            setCameraPos(null);
        }
    }

    public static boolean isCameraActive() {
        return cameraActive;
    }

    private static boolean isCeilingCamera() {
        return cameraDirection == Direction.UP;
    }

    private static boolean isFloorCamera() {
        return cameraDirection == Direction.DOWN;
    }

    public static void rotateCameraLeft() {
        if (cameraActive) {
            cameraYaw -= 5f;
            if (!isCeilingCamera() && !isFloorCamera()) {
                cameraYaw = Math.max(cameraYaw, -180f);
            }
            updatePlayerRotation();
        }
    }

    public static void rotateCameraRight() {
        if (cameraActive) {
            cameraYaw += 5f;
            if (!isCeilingCamera() && !isFloorCamera()) {
                cameraYaw = Math.min(cameraYaw, 180f);
            }
            updatePlayerRotation();
        }
    }

    public static void rotateCameraUp() {
        if (!cameraActive) return;

        if (isCeilingCamera()) {
            cameraPitch = Math.max(cameraPitch - 5f, -90f);
        } else if (isFloorCamera()) {
            cameraPitch = Math.max(cameraPitch - 5f, 0f);
        } else {
            cameraPitch = Math.max(cameraPitch - 5f, -60f);
        }

        updatePlayerRotation();
    }

    public static void rotateCameraDown() {
        if (!cameraActive) return;

        if (isCeilingCamera()) {
            cameraPitch = Math.min(cameraPitch + 5f, 0f);
        } else if (isFloorCamera()) {
            cameraPitch = Math.min(cameraPitch + 5f, 90f);
        } else {
            cameraPitch = Math.min(cameraPitch + 5f, 60f);
        }

        updatePlayerRotation();
    }

    private static void updatePlayerRotation() {
        NetworkHandler.INSTANCE.sendToServer(new CameraRotationPacket(cameraYaw, cameraPitch));
    }

    public static BlockPos getCameraPos() {
        return cameraPos;
    }

    public static void setCameraPos(BlockPos cameraPos) {
        CameraViewHandler.cameraPos = cameraPos;
    }

    public static float getCameraYaw() {
        return cameraYaw;
    }

    public static float getCameraPitch() {
        return cameraPitch;
    }
}
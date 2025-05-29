package fr.libnaus.safetycraft.client.key;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Safetycraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CameraKeyHandler {
    public static KeyMapping CAMERA_LEFT;
    public static KeyMapping CAMERA_RIGHT;
    public static KeyMapping CAMERA_UP;
    public static KeyMapping CAMERA_DOWN;
    public static KeyMapping CAMERA_EXIT;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        CAMERA_LEFT = new KeyMapping(
                "key.safetycraft.camera_left",
                GLFW.GLFW_KEY_J,
                "key.categories.safetycraft"
        );

        CAMERA_RIGHT = new KeyMapping(
                "key.safetycraft.camera_right",
                GLFW.GLFW_KEY_L,
                "key.categories.safetycraft"
        );

        CAMERA_UP = new KeyMapping(
                "key.safetycraft.camera_up",
                GLFW.GLFW_KEY_I,
                "key.categories.safetycraft"
        );

        CAMERA_DOWN = new KeyMapping(
                "key.safetycraft.camera_down",
                GLFW.GLFW_KEY_K,
                "key.categories.safetycraft"
        );

        CAMERA_EXIT = new KeyMapping(
                "key.safetycraft.camera_exit",
                GLFW.GLFW_KEY_ESCAPE,
                "key.categories.safetycraft"
        );

        event.register(CAMERA_LEFT);
        event.register(CAMERA_RIGHT);
        event.register(CAMERA_UP);
        event.register(CAMERA_DOWN);
        event.register(CAMERA_EXIT);
    }

    @Mod.EventBusSubscriber(modid = "safetycraft", value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && CameraViewHandler.isCameraActive()) {

                while (CAMERA_LEFT.consumeClick()) {
                    CameraViewHandler.rotateCameraLeft();
                }

                while (CAMERA_RIGHT.consumeClick()) {
                    CameraViewHandler.rotateCameraRight();
                }

                while (CAMERA_UP.consumeClick()) {
                    CameraViewHandler.rotateCameraUp();
                }

                while (CAMERA_DOWN.consumeClick()) {
                    CameraViewHandler.rotateCameraDown();
                }

                while (CAMERA_EXIT.consumeClick()) {
                    CameraViewHandler.deactivateCameraView();
                }
            }
        }
    }
}
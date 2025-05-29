package fr.libnaus.safetycraft.client.hud;

import fr.libnaus.safetycraft.client.CameraViewHandler;
import fr.libnaus.safetycraft.client.key.CameraKeyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CameraHUD {

    public static void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (CameraViewHandler.isCameraActive()) {
            Minecraft mc = Minecraft.getInstance();
            int x = 10;
            int y = 10;

            for (String control : getCamerasKey()) {
                guiGraphics.drawString(mc.font, control, x, y, 0xFFFFFF);
                y += 10;
            }
        }
    }

    private static String @NotNull [] getCamerasKey() {
        String rotateRightKey = CameraKeyHandler.CAMERA_RIGHT.getTranslatedKeyMessage().getString();
        String rotateLeftKey = CameraKeyHandler.CAMERA_LEFT.getTranslatedKeyMessage().getString();
        String rotateUpKey = CameraKeyHandler.CAMERA_UP.getTranslatedKeyMessage().getString();
        String rotateDownKey = CameraKeyHandler.CAMERA_DOWN.getTranslatedKeyMessage().getString();
        String exitKey = CameraKeyHandler.CAMERA_EXIT.getTranslatedKeyMessage().getString();

        return new String[]{
                "Camera Controls:",
                "Rotate Left: " + rotateLeftKey,
                "Rotate Right: " + rotateRightKey,
                "Rotate Up: " + rotateUpKey,
                "Rotate Down: " + rotateDownKey,
                "Exit Camera: " + exitKey
        };
    }
}

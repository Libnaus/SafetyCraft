package fr.libnaus.safetycraft.mixin;

import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectRenderHotbar(float p_283031_, GuiGraphics p_282108_, CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive())
            ci.cancel();
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectRenderCrosshair(GuiGraphics p_283501_, CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive())
            ci.cancel();
    }

    @Inject(method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectRenderSelectedItemName(GuiGraphics p_283501_, CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive())
            ci.cancel();
    }
}
package fr.libnaus.safetycraft.mixin;

import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class MixinItemInHandRenderer {
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectRenderItem(CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectRenderArmWithItem(CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive()) {
            ci.cancel();
        }
    }
}

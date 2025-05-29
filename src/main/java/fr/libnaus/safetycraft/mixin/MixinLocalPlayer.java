package fr.libnaus.safetycraft.mixin;

import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {
    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectAiStep(CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive())
            ci.cancel();
    }
}

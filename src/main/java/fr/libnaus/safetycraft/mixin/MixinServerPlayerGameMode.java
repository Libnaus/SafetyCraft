package fr.libnaus.safetycraft.mixin;

import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class MixinServerPlayerGameMode {

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (CameraViewHandler.isCameraActive())
            cir.setReturnValue(false);
    }

}
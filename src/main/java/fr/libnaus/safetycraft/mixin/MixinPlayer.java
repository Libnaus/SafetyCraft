package fr.libnaus.safetycraft.mixin;

import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectInteractOn(Entity p_36158_, InteractionHand p_36159_, CallbackInfoReturnable<InteractionResult> cir) {
        if (CameraViewHandler.isCameraActive()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectInteract(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (CameraViewHandler.isCameraActive()) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(method = "openMenu", at = @At("HEAD"), cancellable = true)
    private void safetycraft$injectOpenMenu(MenuProvider p_36150_, CallbackInfoReturnable<OptionalInt> cir) {
        if (CameraViewHandler.isCameraActive()) {
            cir.setReturnValue(null);
        }
    }
}
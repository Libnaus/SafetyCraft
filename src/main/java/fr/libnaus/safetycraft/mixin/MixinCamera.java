package fr.libnaus.safetycraft.mixin;

import fr.libnaus.safetycraft.blocks.CameraBlock;
import fr.libnaus.safetycraft.client.CameraViewHandler;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Shadow
    private float xRot;
    @Shadow
    private float yRot;

    @Inject(method = "setup", at = @At("TAIL"))
    private void safetycraft$injectSetupCamera(BlockGetter getter, Entity entity, boolean isThirdPerson, boolean isInverseView, float partialTicks, CallbackInfo ci) {
        if (CameraViewHandler.isCameraActive()) {
            BlockPos camPos = CameraViewHandler.getCameraPos();
            if (camPos != null) {
                BlockState blockState = getter.getBlockState(camPos);
                if (blockState.getBlock() instanceof CameraBlock) {
                    Direction facing = blockState.getValue(CameraBlock.FACING);
                    double x = camPos.getX() + 0.5;
                    double y = camPos.getY() + 0.5;
                    double z = camPos.getZ() + 0.5;

                    switch (facing) {
                        case NORTH:
                            z -= 0.5;
                            break;
                        case SOUTH:
                            z += 0.5;
                            break;
                        case WEST:
                            x -= 0.5;
                            break;
                        case EAST:
                            x += 0.5;
                            break;
                    }

                    this.setPosition(x, y, z);
                    this.yRot = CameraViewHandler.getCameraYaw();
                    this.xRot = CameraViewHandler.getCameraPitch();
                }
            }
        }
    }
}

package fr.libnaus.safetycraft.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.libnaus.safetycraft.blocks.entity.ComputerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ComputerBlockEntityRenderer implements BlockEntityRenderer<ComputerBlockEntity> {
    public ComputerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ComputerBlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                tile.getBlockState(),
                poseStack,
                buffer,
                combinedLight,
                combinedOverlay
        );
    }
}

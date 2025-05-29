package fr.libnaus.safetycraft.items;

import fr.libnaus.safetycraft.blocks.CameraBlock;
import fr.libnaus.safetycraft.blocks.entity.ComputerBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RemoteControlItem extends Item {

    private static final String CAMERA_LIST_TAG = "CameraList";
    private static final String CAMERA_POS_TAG = "CameraPos";
    private static final String CAMERA_ID_TAG = "CameraId";

    public RemoteControlItem() {
        super(new Item.Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);

        if (player == null) return InteractionResult.FAIL;

        if (state.getBlock() instanceof CameraBlock) {
            if (addCameraToList(stack, pos)) {
                player.displayClientMessage(
                        Component.literal("Caméra ajoutée à l'appareil de liaison!")
                                .withStyle(ChatFormatting.GREEN),
                        true
                );
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(
                        Component.literal("Cette caméra est déjà enregistrée!")
                                .withStyle(ChatFormatting.RED),
                        true
                );
                return InteractionResult.FAIL;
            }
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ComputerBlockEntity computerEntity) {
                List<BlockPos> cameras = getCameraList(stack);
                if (!cameras.isEmpty()) {
                    computerEntity.setLinkedCameras(cameras);
                    player.displayClientMessage(
                            Component.literal(cameras.size() + " caméra(s) liée(s) à l'ordinateur!")
                                    .withStyle(ChatFormatting.GREEN),
                            true
                    );
                    clearCameraList(stack);
                    return InteractionResult.SUCCESS;
                } else {
                    player.displayClientMessage(
                            Component.literal("Aucune caméra enregistrée sur cet appareil!")
                                    .withStyle(ChatFormatting.RED),
                            true
                    );
                    return InteractionResult.FAIL;
                }
            }
        }

        return InteractionResult.PASS;
    }

    private boolean addCameraToList(ItemStack stack, BlockPos cameraPos) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag cameraList = tag.getList(CAMERA_LIST_TAG, Tag.TAG_COMPOUND);

        for (int i = 0; i < cameraList.size(); i++) {
            CompoundTag cameraTag = cameraList.getCompound(i);
            BlockPos existingPos = BlockPos.of(cameraTag.getLong(CAMERA_POS_TAG));
            if (existingPos.equals(cameraPos)) {
                return false;
            }
        }

        CompoundTag newCamera = new CompoundTag();
        newCamera.putLong(CAMERA_POS_TAG, cameraPos.asLong());
        newCamera.putInt(CAMERA_ID_TAG, cameraList.size() + 1);
        cameraList.add(newCamera);

        tag.put(CAMERA_LIST_TAG, cameraList);
        return true;
    }

    private List<BlockPos> getCameraList(ItemStack stack) {
        List<BlockPos> cameras = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(CAMERA_LIST_TAG)) {
            ListTag cameraList = tag.getList(CAMERA_LIST_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < cameraList.size(); i++) {
                CompoundTag cameraTag = cameraList.getCompound(i);
                BlockPos pos = BlockPos.of(cameraTag.getLong(CAMERA_POS_TAG));
                cameras.add(pos);
            }
        }
        return cameras;
    }

    private void clearCameraList(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(CAMERA_LIST_TAG);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.literal("Permet de lier des caméras à un ordinateur")
                .withStyle(ChatFormatting.GRAY));

        List<BlockPos> cameras = getCameraList(stack);
        if (!cameras.isEmpty()) {
            tooltip.add(Component.literal("Caméras enregistrées: " + cameras.size())
                    .withStyle(ChatFormatting.BLUE));

            for (int i = 0; i < Math.min(cameras.size(), 5); i++) {
                BlockPos pos = cameras.get(i);
                tooltip.add(Component.literal("  #" + (i + 1) + ": " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
                        .withStyle(ChatFormatting.DARK_GRAY));
            }

            if (cameras.size() > 5) {
                tooltip.add(Component.literal("  ... et " + (cameras.size() - 5) + " autre(s)")
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Component.literal("Aucune caméra enregistrée")
                    .withStyle(ChatFormatting.DARK_GRAY));
        }

        tooltip.add(Component.literal("Clic droit sur une caméra pour l'enregistrer")
                .withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("Clic droit sur un ordinateur pour transférer")
                .withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return !getCameraList(stack).isEmpty();
    }
}

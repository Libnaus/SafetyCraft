package fr.libnaus.safetycraft.blocks.entity;

import fr.libnaus.safetycraft.blocks.CameraBlock;
import fr.libnaus.safetycraft.init.ModBlockEntities;
import fr.libnaus.safetycraft.menu.ComputerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ComputerBlockEntity extends BlockEntity implements MenuProvider {

    private static final String LINKED_CAMERAS_TAG = "LinkedCameras";
    private static final String CAMERA_POS_TAG = "CameraPos";

    List<BlockPos> linkedCameras = new ArrayList<>();

    public ComputerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTER.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.getGameTime() % 100 == 0) {
            validateLinkedCameras();
        }
    }

    public void setLinkedCameras(List<BlockPos> cameras) {
        this.linkedCameras.clear();
        this.linkedCameras.addAll(cameras);
        setChanged();
    }

    public void addLinkedCamera(BlockPos cameraPos) {
        if (!linkedCameras.contains(cameraPos)) {
            linkedCameras.add(cameraPos);
            setChanged();
        }
    }

    public void removeLinkedCamera(BlockPos cameraPos) {
        linkedCameras.remove(cameraPos);
        setChanged();
    }

    public void clearLinkedCameras() {
        linkedCameras.clear();
        setChanged();
    }

    public List<CameraInfo> getCameras() {
        List<CameraInfo> cameras = new ArrayList<>();

        if (level != null) {
            int cameraIndex = 1;
            for (BlockPos pos : new ArrayList<>(linkedCameras)) {
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof CameraBlock) {
                    cameras.add(new CameraInfo(cameraIndex++, pos.immutable()));
                } else {
                    linkedCameras.remove(pos);
                    setChanged();
                }
            }
        }

        return cameras;
    }

    private void validateLinkedCameras() {
        if (level == null) return;

        boolean changed = false;
        List<BlockPos> toRemove = new ArrayList<>();

        for (BlockPos pos : linkedCameras) {
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof CameraBlock)) {
                toRemove.add(pos);
                changed = true;
            }
        }

        if (changed) {
            linkedCameras.removeAll(toRemove);
            setChanged();
        }
    }

    public int getCameraCount() {
        return getCameras().size();
    }

    public CameraInfo getNearestCamera() {
        List<CameraInfo> cameras = getCameras();
        if (cameras.isEmpty()) return null;

        CameraInfo nearest = cameras.get(0);
        double nearestDistance = worldPosition.distSqr(nearest.getPos());

        for (CameraInfo camera : cameras) {
            double distance = worldPosition.distSqr(camera.getPos());
            if (distance < nearestDistance) {
                nearest = camera;
                nearestDistance = distance;
            }
        }

        return nearest;
    }

    public CameraInfo getCameraById(int id) {
        List<CameraInfo> cameras = getCameras();
        for (CameraInfo camera : cameras) {
            if (camera.getId() == id) {
                return camera;
            }
        }
        return null;
    }

    public List<BlockPos> getLinkedCameraPositions() {
        return new ArrayList<>(linkedCameras);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        linkedCameras.clear();

        if (tag.contains(LINKED_CAMERAS_TAG)) {
            ListTag cameraList = tag.getList(LINKED_CAMERAS_TAG, Tag.TAG_COMPOUND);
            for (int i = 0; i < cameraList.size(); i++) {
                CompoundTag cameraTag = cameraList.getCompound(i);
                BlockPos pos = BlockPos.of(cameraTag.getLong(CAMERA_POS_TAG));
                linkedCameras.add(pos);
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag cameraList = new ListTag();
        for (BlockPos pos : linkedCameras) {
            CompoundTag cameraTag = new CompoundTag();
            cameraTag.putLong(CAMERA_POS_TAG, pos.asLong());
            cameraList.add(cameraTag);
        }
        tag.put(LINKED_CAMERAS_TAG, cameraList);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.safetycraft.computer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ComputerMenu(i, inventory, this.getBlockPos());
    }

    public static class CameraInfo {
        public final int id;
        public final BlockPos pos;

        public CameraInfo(int id, BlockPos pos) {
            this.id = id;
            this.pos = pos;
        }

        public BlockPos getPos() {
            return pos;
        }

        public int getId() {
            return id;
        }

        public double getDistanceTo(BlockPos otherPos) {
            return Math.sqrt(pos.distSqr(otherPos));
        }

        public String getDisplayName() {
            return String.format("Camera #%02d (X:%d Y:%d Z:%d)", id, pos.getX(), pos.getY(), pos.getZ());
        }

        @Override
        public String toString() {
            return String.format("CameraInfo{id=%d, pos=%s}", id, pos.toShortString());
        }
    }
}
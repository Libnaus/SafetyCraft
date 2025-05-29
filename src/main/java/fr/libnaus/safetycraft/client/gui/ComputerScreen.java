package fr.libnaus.safetycraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.blocks.entity.ComputerBlockEntity;
import fr.libnaus.safetycraft.client.CameraViewHandler;
import fr.libnaus.safetycraft.menu.ComputerMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class ComputerScreen extends AbstractContainerScreen<ComputerMenu> {

    private static final int BACKGROUND_COLOR = 0xFF1E1E1E;
    private static final int HEADER_COLOR = 0xFF2D2D30;
    private static final int BORDER_COLOR = 0xFF404040;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SUCCESS_COLOR = 0xFF4CAF50;
    private static final int WARNING_COLOR = 0xFFFF9800;
    private static final int ACCENT_COLOR = 0xFF2196F3;

    private ComputerBlockEntity blockEntity;

    private final int imageWidth = 300;
    private final int imageHeight = 220;

    private int currentPage = 0;
    private static final int CAMERAS_PER_PAGE = 6;
    private static final int CAMERAS_PER_ROW = 2;

    private Button prevPageButton;
    private Button nextPageButton;
    private final List<Button> cameraButtons = new ArrayList<>();

    public ComputerScreen(ComputerMenu menu, net.minecraft.world.entity.player.Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.titleLabelX = -1000;
        this.inventoryLabelX = -1000;
        this.inventoryLabelY = -1000;

        BlockPos blockPos = menu.getPos();
        BlockEntity be = Minecraft.getInstance().level.getBlockEntity(blockPos);
        if (be instanceof ComputerBlockEntity computerBE) {
            this.blockEntity = computerBE;
        }
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        initializeNavigationButtons();
        updateCameraDisplay();
    }

    private void initializeNavigationButtons() {
        int buttonY = topPos + imageHeight - 35;

        prevPageButton = Button.builder(
                Component.literal("<"),
                button -> {
                    if (currentPage > 0) {
                        currentPage--;
                        updateCameraDisplay();
                    }
                }
        ).bounds(leftPos + 20, buttonY, 30, 20).build();
        addRenderableWidget(prevPageButton);

        nextPageButton = Button.builder(
                Component.literal(">"),
                button -> {
                    List<ComputerBlockEntity.CameraInfo> cameras = blockEntity.getCameras();
                    int maxPages = getMaxPages(cameras.size());
                    if (currentPage < maxPages - 1) {
                        currentPage++;
                        updateCameraDisplay();
                    }
                }
        ).bounds(leftPos + imageWidth - 50, buttonY, 30, 20).build();
        addRenderableWidget(nextPageButton);

        addRenderableWidget(Button.builder(
                Component.literal("X"),
                button -> onClose()
        ).bounds(leftPos + imageWidth - 25, topPos + 5, 20, 20).build());
    }

    private void updateCameraDisplay() {
        cameraButtons.forEach(this::removeWidget);
        cameraButtons.clear();

        List<ComputerBlockEntity.CameraInfo> cameras = blockEntity.getCameras();

        if (cameras.isEmpty()) {
            return;
        }

        int startIndex = currentPage * CAMERAS_PER_PAGE;
        int endIndex = Math.min(startIndex + CAMERAS_PER_PAGE, cameras.size());

        for (int i = startIndex; i < endIndex; i++) {
            ComputerBlockEntity.CameraInfo camera = cameras.get(i);

            int buttonIndex = i - startIndex;
            int row = buttonIndex / CAMERAS_PER_ROW;
            int col = buttonIndex % CAMERAS_PER_ROW;

            int buttonWidth = 110;
            int buttonHeight = 20;
            int spacing = 15;
            int yOffset = 5;

            int totalWidth = CAMERAS_PER_ROW * buttonWidth + (CAMERAS_PER_ROW - 1) * spacing;
            int startX = leftPos + (imageWidth - totalWidth) / 2;

            int x = startX + col * (buttonWidth + spacing);
            int y = topPos + 80 + row * (buttonHeight + yOffset);

            Button cameraButton = Button.builder(
                    Component.literal("Camera " + String.format("%02d", camera.getId())),
                    button -> {
                        onClose();
                        CameraViewHandler.activateCameraView(camera.getPos());
                    }
            ).bounds(x, y, buttonWidth, buttonHeight).build();

            cameraButtons.add(cameraButton);
            addRenderableWidget(cameraButton);
        }

        int maxPages = getMaxPages(cameras.size());
        prevPageButton.active = currentPage > 0;
        nextPageButton.active = currentPage < maxPages - 1;
    }

    private int getMaxPages(int totalCameras) {
        return Math.max(1, (int) Math.ceil((double) totalCameras / CAMERAS_PER_PAGE));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        renderMainInterface(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltips(guiGraphics, mouseX, mouseY);
    }

    private void renderMainInterface(GuiGraphics guiGraphics) {
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, BACKGROUND_COLOR);

        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + 30, HEADER_COLOR);
        drawBorder(guiGraphics, leftPos, topPos, imageWidth, imageHeight, BORDER_COLOR);

        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                "Système de Sécurité",
                leftPos + imageWidth / 2,
                topPos + 11,
                TEXT_COLOR
        );

        renderStatusSection(guiGraphics);
        renderCameraSection(guiGraphics);
        renderPaginationInfo(guiGraphics);
    }

    private void renderStatusSection(GuiGraphics guiGraphics) {
        int statusY = topPos + 40;
        List<ComputerBlockEntity.CameraInfo> cameras = blockEntity.getCameras();

        String statusText;
        int statusColor;

        if (cameras.isEmpty()) {
            statusText = "Aucune caméra connectée";
            statusColor = WARNING_COLOR;
        } else {
            statusText = String.format("%d caméra(s) opérationnelle(s)", cameras.size());
            statusColor = SUCCESS_COLOR;
        }

        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                statusText,
                leftPos + imageWidth / 2,
                statusY,
                statusColor
        );
    }

    private void renderCameraSection(GuiGraphics guiGraphics) {
        List<ComputerBlockEntity.CameraInfo> cameras = blockEntity.getCameras();

        if (cameras.isEmpty()) {
            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    "Utilisez l'Outil de Liaison Caméra",
                    leftPos + imageWidth / 2,
                    topPos + 120,
                    0xFF888888
            );
            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    "pour connecter des caméras",
                    leftPos + imageWidth / 2,
                    topPos + 135,
                    0xFF888888
            );
        } else {
            int cameraAreaY = topPos + 70;
            int cameraAreaHeight = 110;
            drawBorder(guiGraphics, leftPos + 10, cameraAreaY, imageWidth - 20, cameraAreaHeight, BORDER_COLOR);
        }
    }

    private void renderPaginationInfo(GuiGraphics guiGraphics) {
        List<ComputerBlockEntity.CameraInfo> cameras = blockEntity.getCameras();
        if (cameras.size() > CAMERAS_PER_PAGE) {
            int maxPages = getMaxPages(cameras.size());
            String pageInfo = String.format("Page %d/%d", currentPage + 1, maxPages);

            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    pageInfo,
                    leftPos + imageWidth / 2,
                    topPos + imageHeight - 25,
                    TEXT_COLOR
            );
        }
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        List<ComputerBlockEntity.CameraInfo> cameras = blockEntity.getCameras();
        int startIndex = currentPage * CAMERAS_PER_PAGE;
        int endIndex = Math.min(startIndex + CAMERAS_PER_PAGE, cameras.size());

        for (int i = 0; i < cameraButtons.size() && (startIndex + i) < endIndex; i++) {
            Button button = cameraButtons.get(i);
            ComputerBlockEntity.CameraInfo camera = cameras.get(startIndex + i);

            if (button.isHoveredOrFocused()) {
                List<Component> tooltip = List.of(
                        Component.literal("Caméra #" + String.format("%02d", camera.getId())),
                        Component.literal("Position: " + camera.getPos().getX() + ", " + camera.getPos().getY() + ", " + camera.getPos().getZ()),
                        Component.literal("Distance: " + String.format("%.1f", camera.getDistanceTo(blockEntity.getBlockPos())) + " blocs"),
                        Component.literal("Cliquez pour voir le flux vidéo")
                );
                guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {}

    private void drawBorder(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.fill(x, y, x + width, y + 1, color);
        guiGraphics.fill(x, y + height - 1, x + width, y + height, color);
        guiGraphics.fill(x, y, x + 1, y + height, color);
        guiGraphics.fill(x + width - 1, y, x + width, y + height, color);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
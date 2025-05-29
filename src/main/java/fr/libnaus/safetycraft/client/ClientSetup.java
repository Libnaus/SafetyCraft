package fr.libnaus.safetycraft.client;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.client.gui.ComputerScreen;
import fr.libnaus.safetycraft.client.hud.CameraHUD;
import fr.libnaus.safetycraft.init.ModBlockEntities;
import fr.libnaus.safetycraft.init.ModBlocks;
import fr.libnaus.safetycraft.init.ModMenuTypes;
import fr.libnaus.safetycraft.renderer.ComputerBlockEntityRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.xml.validation.Validator;

@Mod.EventBusSubscriber(modid = Safetycraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.COMPUTER.get(), ComputerScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.LAPTOP.get(), RenderType.cutout());
        });
    }

    @SubscribeEvent
    public void onClientModInitializer(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(ModBlockEntities.COMPUTER.get(), ComputerBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "camera_hud", CameraHUD::render);
    }
}

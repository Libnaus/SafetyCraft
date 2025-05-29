package fr.libnaus.safetycraft;

import fr.libnaus.safetycraft.init.*;
import fr.libnaus.safetycraft.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Safetycraft.MODID)
public class Safetycraft {

    public static final String MODID = "safetycraft";

    public Safetycraft() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModCreativeModeTabs.TABS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModMenuTypes.MENUS.register(bus);

        bus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::registerPackets);
    }
}
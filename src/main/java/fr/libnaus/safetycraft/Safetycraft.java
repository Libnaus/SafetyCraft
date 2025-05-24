package fr.libnaus.safetycraft;

import fr.libnaus.safetycraft.init.ModBlocks;
import fr.libnaus.safetycraft.init.ModCreativeModeTabs;
import fr.libnaus.safetycraft.init.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Safetycraft.MODID)
public class Safetycraft {

    public static final String MODID = "safetycraft";

    public Safetycraft() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModCreativeModeTabs.TABS.register(bus);
    }
}

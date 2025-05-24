package fr.libnaus.safetycraft.init;

import fr.libnaus.safetycraft.Safetycraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Safetycraft.MODID);

    private static final List<Supplier<Item>> CREATIVE_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> SAFETYCRAFT_TAB = TABS.register("safetycraft_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> ModBlocks.CAMERA.get().asItem().getDefaultInstance())
                    .title(Component.translatable("itemGroup.safetycraft.safetycraft_tab"))
                    .displayItems((parameters, output) -> {
                        CREATIVE_TAB_ITEMS.forEach(item -> output.accept(item.get()));
                    })
                    .build());

    public static void addItemToTab(Item item) {
        CREATIVE_TAB_ITEMS.add(() -> item);
    }
}

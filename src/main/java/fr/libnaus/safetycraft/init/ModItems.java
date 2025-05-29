package fr.libnaus.safetycraft.init;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.items.RemoteControlItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Safetycraft.MODID);

    public static final RegistryObject<Item> REMOTE_CONTROL_ITEM = register("remote_control", RemoteControlItem::new);

    /**
     * Registers an item with the given name and supplier, and also registers its corresponding BlockItem if applicable.
     *
     * @param name The name of the item.
     * @param item The supplier for the item instance.
     * @param <T>  The type of the item.
     * @return A RegistryObject representing the registered item.
     */
    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        RegistryObject<T> toReturn = ITEMS.register(name, item);
        return toReturn;
    }
}

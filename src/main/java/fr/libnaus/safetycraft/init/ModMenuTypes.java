package fr.libnaus.safetycraft.init;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.blocks.entity.ComputerBlockEntity;
import fr.libnaus.safetycraft.menu.ComputerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Safetycraft.MODID);

    public static final RegistryObject<MenuType<ComputerMenu>> COMPUTER =
            MENUS.register("laptop_menu", () ->
                    IForgeMenuType.create((windowId, inventory, data) ->
                            new ComputerMenu(
                                    windowId, inventory, data.readBlockPos()
                            )));
}
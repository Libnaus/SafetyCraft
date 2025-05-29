package fr.libnaus.safetycraft.init;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.blocks.CameraBlock;
import fr.libnaus.safetycraft.blocks.LaptopBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Safetycraft.MODID);

    public static final RegistryObject<Block> CAMERA = register("camera", CameraBlock::new);
    public static final RegistryObject<Block> LAPTOP = register("laptop", LaptopBlock::new);

    /**
     * Registers a block with the given name and supplier, and also registers its corresponding BlockItem.
     *
     * @param name  The name of the block.
     * @param block The supplier for the block instance.
     * @param <T>   The type of the block.
     * @return A RegistryObject representing the registered block.
     */
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    /**
     * Registers a BlockItem for the given block and adds it to the creative mode tab.
     *
     * @param name  The name of the block item.
     * @param block The RegistryObject representing the block.
     * @param <T>   The type of the block.
     */
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> {
            BlockItem blockItem = new BlockItem(block.get(), new Item.Properties());
            ModCreativeModeTabs.addItemToTab(blockItem);
            return blockItem;
        });
    }
}
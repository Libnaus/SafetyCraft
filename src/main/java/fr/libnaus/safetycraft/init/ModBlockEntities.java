package fr.libnaus.safetycraft.init;

import fr.libnaus.safetycraft.Safetycraft;
import fr.libnaus.safetycraft.blocks.entity.ComputerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Safetycraft.MODID);

    public static final RegistryObject<BlockEntityType<ComputerBlockEntity>> COMPUTER =
            BLOCK_ENTITIES.register("laptop", () ->
                    BlockEntityType.Builder.of(ComputerBlockEntity::new, ModBlocks.LAPTOP.get()).build(null));
}
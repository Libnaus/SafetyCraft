package fr.libnaus.safetycraft.menu;

import fr.libnaus.safetycraft.init.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ComputerMenu extends AbstractContainerMenu {

    private final BlockPos pos;

    public ComputerMenu(int id, Inventory playerInv, FriendlyByteBuf data) {
        this(id, playerInv, data.readBlockPos());
    }

    public ComputerMenu(int id, Inventory playerInv, BlockPos pos) {
        super(ModMenuTypes.COMPUTER.get(), id);
        this.pos = pos;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public BlockPos getPos() {
        return pos;
    }
}

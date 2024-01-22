package be.sixefyle.transdimquarry.blocks.foundry.foundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundry;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FoundryBlockEntity extends BaseFoundry {
    public FoundryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.FOUNDRY.get(), pos, state);

        setInputSlotAmount(2);
        setMaxProgress(80);
        setCookMult(4);
        setEnergyNeeded(80);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Foundry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new FoundryMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, FoundryBlockEntity foundryBlockEntity) {
        foundryBlockEntity.onTick(level, blockPos);
    }
}

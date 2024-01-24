package be.sixefyle.transdimquarry.blocks.foundry.cosmicfoundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundry;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CosmicFoundryBlockEntity extends BaseFoundry {
    public CosmicFoundryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.COSMIC_FOUNDRY.get(), pos, state);

        setInputSlotAmount(6);
        setCookMult(16);
        setInputCostReductionChance(0.01);
        setMaxProgress(getMaxProgress() / 4);

        setEnergyCapacity(getEnergyCapacity() * 8);
        setEnergyNeeded(getNeededEnergy() * 16);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Cosmic Foundry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new CosmicFoundryMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CosmicFoundryBlockEntity foundryBlockEntity) {
        foundryBlockEntity.onTick(level, blockPos);
    }
}

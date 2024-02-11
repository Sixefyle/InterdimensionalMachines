package be.sixefyle.transdimquarry.blocks.foundry.cosmic;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundry;
import be.sixefyle.transdimquarry.config.CommonConfig;
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

        setCookMult(CommonConfig.COSMIC_FOUNDRY_COOK_MULTIPLIER.get());
        setInputCostReductionChance(CommonConfig.COSMIC_FOUNDRY_INPUT_REDUCTION_CHANCE.get());
        setMaxProgress(CommonConfig.COSMIC_FOUNDRY_SMELT_TIME.get());
        setEnergyNeeded(CommonConfig.COSMIC_FOUNDRY_ENERGY_COST.get());
        setEnergyCapacity(CommonConfig.COSMIC_FOUNDRY_ENERGY_CAPACITY.get());
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

package be.sixefyle.transdimquarry.blocks.foundry.advanced;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundry;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedFoundryBlockEntity extends BaseFoundry {
    public AdvancedFoundryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.ADVANCED_FOUNDRY.get(), pos, state);

        setInputSlotAmount(4);

        setCookMult(CommonConfig.ADVANCED_FOUNDRY_COOK_MULTIPLIER.get());
        setInputCostReductionChance(CommonConfig.ADVANCED_FOUNDRY_INPUT_REDUCTION_CHANCE.get());
        setMaxProgress(CommonConfig.ADVANCED_FOUNDRY_SMELT_TIME.get());
        setEnergyNeeded(CommonConfig.ADVANCED_FOUNDRY_ENERGY_COST.get());
        setEnergyCapacity(CommonConfig.ADVANCED_FOUNDRY_ENERGY_CAPACITY.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Advanced Foundry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new AdvancedFoundryMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AdvancedFoundryBlockEntity foundryBlockEntity) {
        foundryBlockEntity.onTick(level, blockPos);
    }
}
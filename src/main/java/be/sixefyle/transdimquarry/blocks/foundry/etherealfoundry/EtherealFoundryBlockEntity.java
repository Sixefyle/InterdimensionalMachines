package be.sixefyle.transdimquarry.blocks.foundry.etherealfoundry;

import be.sixefyle.transdimquarry.blocks.foundry.BaseFoundry;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EtherealFoundryBlockEntity extends BaseFoundry {
    public EtherealFoundryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.ETHEREAL_FOUNDRY.get(), pos, state);

        setInputSlotAmount(8);

        setCookMult(CommonConfig.ETHEREAL_FOUNDRY_COOK_MULTIPLIER.get());
        setInputCostReductionChance(CommonConfig.ETHEREAL_FOUNDRY_INPUT_REDUCTION_CHANCE.get());
        setMaxProgress(CommonConfig.ETHEREAL_FOUNDRY_SMELT_TIME.get());
        setEnergyNeeded(CommonConfig.ETHEREAL_FOUNDRY_ENERGY_COST.get());
        setEnergyCapacity(CommonConfig.ETHEREAL_FOUNDRY_ENERGY_CAPACITY.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Ethereal Foundry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new EtherealFoundryMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EtherealFoundryBlockEntity foundryBlockEntity) {
        foundryBlockEntity.onTick(level, blockPos);
    }
}

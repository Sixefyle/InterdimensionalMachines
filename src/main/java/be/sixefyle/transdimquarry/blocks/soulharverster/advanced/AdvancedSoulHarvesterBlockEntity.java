package be.sixefyle.transdimquarry.blocks.soulharverster.advanced;

import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterBlockEntity;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedSoulHarvesterBlockEntity extends SoulHarvesterBlockEntity {
    public AdvancedSoulHarvesterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.ADVANCED_SOUL_MANIPULATOR.get(), pos, state, 8, 150_000);

        setSoulBottleSlotsAmount(2);
        setMaxProgress(200);
        setEnergyNeeded(50_000);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Advanced Soul Manipulator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new AdvancedSoulHarvesterMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, AdvancedSoulHarvesterBlockEntity blockEntity) {
        blockEntity.onTick(level, blockPos);
    }
}

package be.sixefyle.transdimquarry.blocks.soulharverster.ethereal;

import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterBlockEntity;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EtherealSoulHarvesterBlockEntity extends SoulHarvesterBlockEntity {
    public EtherealSoulHarvesterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.ETHEREAL_SOUL_MANIPULATOR.get(), pos, state, 23, 5_000_000);

        setSoulBottleSlotsAmount(8);
        setMaxProgress(100);
        setEnergyNeeded(500_000);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Ethereal Soul Manipulator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new EtherealSoulHarvesterMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, EtherealSoulHarvesterBlockEntity blockEntity) {
        blockEntity.onTick(level, blockPos);
    }
}

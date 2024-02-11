package be.sixefyle.transdimquarry.blocks.soulharverster.cosmic;

import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterBlockEntity;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CosmicSoulHarvesterBlockEntity extends SoulHarvesterBlockEntity {
    public CosmicSoulHarvesterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.COSMIC_SOUL_MANIPULATOR.get(), pos, state, 13, 500_000);

        setSoulBottleSlotsAmount(4);
        setMaxProgress(150);
        setEnergyNeeded(150_000);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Cosmic Soul Manipulator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new CosmicSoulHarvesterMenu(id, inv, this, getBaseData());
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, CosmicSoulHarvesterBlockEntity blockEntity) {
        blockEntity.onTick(level, blockPos);
    }
}

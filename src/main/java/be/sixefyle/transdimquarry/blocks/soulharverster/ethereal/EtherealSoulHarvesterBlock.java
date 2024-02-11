package be.sixefyle.transdimquarry.blocks.soulharverster.ethereal;

import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterBlock;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EtherealSoulHarvesterBlock extends SoulHarvesterBlock {

    public EtherealSoulHarvesterBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegister.ETHEREAL_SOUL_MANIPULATOR.get().create(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegister.ETHEREAL_SOUL_MANIPULATOR.get(),
                EtherealSoulHarvesterBlockEntity::tick);
    }
}

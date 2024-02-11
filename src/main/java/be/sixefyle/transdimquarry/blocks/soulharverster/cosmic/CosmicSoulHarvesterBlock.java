package be.sixefyle.transdimquarry.blocks.soulharverster.cosmic;

import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterBlock;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CosmicSoulHarvesterBlock extends SoulHarvesterBlock {

    public CosmicSoulHarvesterBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityRegister.COSMIC_SOUL_MANIPULATOR.get().create(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegister.COSMIC_SOUL_MANIPULATOR.get(),
                CosmicSoulHarvesterBlockEntity::tick);
    }
}

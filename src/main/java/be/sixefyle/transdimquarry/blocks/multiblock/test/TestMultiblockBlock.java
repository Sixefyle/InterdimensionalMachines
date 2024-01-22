package be.sixefyle.transdimquarry.blocks.multiblock.test;

import be.sixefyle.transdimquarry.blocks.multiblock.BaseMultiblockBlock;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TestMultiblockBlock extends BaseMultiblockBlock {
    public TestMultiblockBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return BlockEntityRegister.TEST_MB.get().create(pPos, pState);
    }
}

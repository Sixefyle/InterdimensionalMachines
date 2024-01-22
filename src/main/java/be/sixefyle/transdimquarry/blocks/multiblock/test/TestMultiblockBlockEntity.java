package be.sixefyle.transdimquarry.blocks.multiblock.test;

import be.sixefyle.transdimquarry.blocks.multiblock.BaseMultiblockBlockEntity;
import be.sixefyle.transdimquarry.blocks.multiblock.CasingType;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class TestMultiblockBlockEntity extends BaseMultiblockBlockEntity {

    public TestMultiblockBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.TEST_MB.get(), pos, state, 0, 0);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("test");
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return null;
    }

    @Override
    public CasingType getCasingType(BlockState state) {
        if(state.is(BlockRegister.TEST_CASING.get())){
            return CasingType.FRAME;
        }

        return CasingType.INVALIDE;
    }
}

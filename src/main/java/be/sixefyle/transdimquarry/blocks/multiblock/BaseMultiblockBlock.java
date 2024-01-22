package be.sixefyle.transdimquarry.blocks.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMultiblockBlock extends BaseEntityBlock {
    public BaseMultiblockBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {

        if(state.hasBlockEntity()
                && (level.getBlockEntity(pos) instanceof BaseMultiblockBlockEntity blockEntity
                && level.getBlockEntity(neighbor) instanceof BaseMultiblockBlockEntity newBlockEntity)) {
            if(blockEntity.verifyMultiblock()){
                newBlockEntity.setMaster(blockEntity);
            }
        }
        super.onNeighborChange(state, level, pos, neighbor);
    }


    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        if(pState.hasBlockEntity()
                && (pLevel.getBlockEntity(pPos) instanceof BaseMultiblockBlockEntity blockEntity)
                && blockEntity.getMaster() == null) {
            blockEntity.setMaster(blockEntity);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pState.hasBlockEntity()
                && (pLevel.getBlockEntity(pPos) instanceof BaseMultiblockBlockEntity blockEntity)){
            pPlayer.sendSystemMessage(Component.literal(String.format("Master: %s", blockEntity.getBlockPos())));
            return InteractionResult.SUCCESS;
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pPos, BlockState pState);
}

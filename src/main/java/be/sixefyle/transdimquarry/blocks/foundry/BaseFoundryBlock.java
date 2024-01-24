package be.sixefyle.transdimquarry.blocks.foundry;

import be.sixefyle.transdimquarry.blocks.iteminfuser.ItemInfuserBlockEntity;
import be.sixefyle.transdimquarry.enums.EnumColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseFoundryBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected BaseFoundryBlock(Properties pProperties) {
        super(pProperties);

        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if(state.getBlock() != newState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof ItemInfuserBlockEntity itemInfuserBlockEntity){
                itemInfuserBlockEntity.dropInventory();
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> tooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, tooltip, pFlag);
        BaseFoundry foundry = (BaseFoundry) newBlockEntity(BlockPos.ZERO, this.defaultBlockState());

        if(foundry == null || foundry.INPUT_SLOT == null) return;

        tooltip.add(EnumColor.GREEN.getColoredComponent(String.format("Input amount: %d slots", foundry.INPUT_SLOT.length)));
        tooltip.add(EnumColor.GREEN.getColoredComponent(String.format("Smelting speed: %d ticks ", foundry.getMaxProgress())));
        tooltip.add(EnumColor.GREEN.getColoredComponent(String.format("Smelt %d items at once ", foundry.getCookMult())));

        if(foundry.getInputCostReductionChance() > 0){
            tooltip.add(Component.empty());
            tooltip.add(EnumColor.PURPLE.getColoredComponent("Have a small chance to use"));
            tooltip.add(EnumColor.PURPLE.getColoredComponent("only 1 item on smelting"));
        }

        tooltip.add(Component.empty());
        tooltip.add(EnumColor.GRAY.getColoredComponent("Like a furnace but better"));
    }
}

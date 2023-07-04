package be.sixefyle.transdimquarry.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class TransdimExcavator extends InfusedTool {
    int energyCostPerBlock = 1000;

    static final int maxVeinBroke = 256;
    static final int defaultVeinSize = 16;
    int currentBrokenVeinBlock = 0;

    public TransdimExcavator() {
        super(new Properties().setNoRepair().fireResistant(), 1_000_000, 1000, 500_000);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState p_41450_) {
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if(!(livingEntity instanceof Player player)) return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);

        if(blockState.is(BlockTags.LOGS) || player.isShiftKeyDown()){
            mineVein(blockPos, blockState.getBlock(), level, player.getMainHandItem());
            currentBrokenVeinBlock = 0;
        } else {
            mineBlockArea(blockPos, level, livingEntity, player.getMainHandItem());
        }

        return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
    }

    private void mineBlockArea(BlockPos blockPos, Level level, LivingEntity livingEntity, ItemStack tool) {
        BlockHitResult trace = getPlayerPOVHitResult(level, (Player) livingEntity, ClipContext.Fluid.NONE);
        Direction facing = trace.getDirection();

        BlockPos topLeft = blockPos.relative(facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.UP, getMineHeight(tool) - 2).relative(facing.getAxis() == Direction.Axis.Y ? Direction.WEST : facing.getCounterClockWise(), getMineWidth(tool)/2);
        BlockPos bottomRight = blockPos.relative(facing.getAxis() == Direction.Axis.Y ? Direction.SOUTH : Direction.DOWN, 1).relative(facing.getAxis() == Direction.Axis.Y ? Direction.EAST : facing.getClockWise(), getMineWidth(tool)/2);
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(topLeft, bottomRight);

        for (BlockPos currentBlockPos : iterable) {
            BlockState blockState = level.getBlockState(currentBlockPos);
            if(blockState.is(Blocks.BEDROCK)) continue;

            List<ItemStack> drops = blockState
                    .getDrops(new LootParams.Builder((ServerLevel) level)
                            .withParameter(LootContextParams.TOOL, tool)
                            .withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                            .withOptionalParameter(LootContextParams.BLOCK_STATE, blockState));

            drops.forEach(itemStack -> Block.popResource(level, currentBlockPos, itemStack));
            level.removeBlock(currentBlockPos, false);
        }
    }

    public void mineVein(BlockPos currentPos, Block blockType, Level level, ItemStack tool){
        if(!(level instanceof ServerLevel)) return;

        BlockPos topLeft = currentPos.offset(1,1,1);
        BlockPos bottomRight = currentPos.offset(-1,-1,-1);
        Iterable<BlockPos> blocks = BlockPos.betweenClosed(topLeft, bottomRight);
        Iterator<BlockPos> iterator = blocks.iterator();

        while(currentBrokenVeinBlock < getMaxVeinSize(tool) && iterator.hasNext()){
            BlockPos blockPos = iterator.next();

            BlockState blockState = level.getBlockState(blockPos);
            if(!blockState.is(blockType)) continue;

            currentBrokenVeinBlock++;
            level.removeBlock(blockPos, false);
            List<ItemStack> drops = blockState
                    .getDrops(new LootParams.Builder((ServerLevel) level)
                            .withParameter(LootContextParams.TOOL, tool)
                            .withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                            .withOptionalParameter(LootContextParams.BLOCK_STATE, blockState));

            drops.forEach(itemStack -> Block.popResource(level, blockPos, itemStack));

            mineVein(blockPos, blockType, level, tool);
        }
    }

    public int getInfusedLevel(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("level")) {
            itemStack.getTag().putInt("level", 0);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("level") : 0;
    }

    public int getMaxVeinSize(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("vein_size")) {
            itemStack.getTag().putInt("vein_size", defaultVeinSize);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("vein_size") : defaultVeinSize;
    }

    public int getMineWidth(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("mine_width")) {
            itemStack.getTag().putInt("mine_width", 3);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("mine_width") : 3;
    }

    public int getMineHeight(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("mine_height")) {
            itemStack.getTag().putInt("mine_height", 3);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("mine_height") : 3;
    }

    public int getFortunePower(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("fortune_power")) {
            itemStack.getTag().putInt("fortune_power", 0);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("fortune_power") : 0;
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState p_41426_) {
        return getEnergyStorage(itemStack).getEnergyStored() > energyCostPerBlock ? 20 : 10;
    }

    @Override
    public void onInfuseLevelUp(ItemStack itemStack) {
        if(!itemStack.hasTag())
            return;

        int maxVeinSize = getMaxVeinSize(itemStack);
        if(maxVeinSize < maxVeinBroke){
            itemStack.getTag().putInt("vein_size", maxVeinSize + 1);
        } else {
            int mineWidth = getMineWidth(itemStack);
            int mineHeight = getMineHeight(itemStack);

            if(mineHeight < 9){
                if(mineWidth - mineHeight == 0){
                    itemStack.getTag().putInt("mine_width", mineWidth + 1);
                } else {
                    itemStack.getTag().putInt("mine_height", mineHeight + 1);
                }
            } else {
                int fortunePower = getFortunePower(itemStack);
                if(fortunePower < 10){
                    itemStack.getTag().putInt("fortune_power", fortunePower + 1);
                }
            }
        }

        itemStack.getTag().putInt("level", getInfusedLevel(itemStack) + 1);
    }

    @Override
    public int getInfusedEnergyNeeded(ItemStack itemStack) {
        return getBaseInfusedEnergyNeeded() + (getInfusedLevel(itemStack) * 175_000);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        if(itemStack.getTag() != null){
            components.add(Component.empty());
            components.add(Component.literal("Tool Stats:"));
            components.add(Component.literal(String.format("Level: %d", getInfusedLevel(itemStack))));
            components.add(Component.literal(String.format("Max vein size: %d", getMaxVeinSize(itemStack))));
            components.add(Component.literal(String.format("Mining width: %d, height: %d", getMineWidth(itemStack), getMineHeight(itemStack))));
        }
    }
}

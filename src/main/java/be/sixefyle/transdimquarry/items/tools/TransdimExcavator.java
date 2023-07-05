package be.sixefyle.transdimquarry.items.tools;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

            LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.TOOL, tool)
                    .withParameter(LootContextParams.ORIGIN, blockPos.getCenter())
                    .withOptionalParameter(LootContextParams.BLOCK_STATE, blockState);
            List<ItemStack> drops = blockState.getDrops(context);

            drops.forEach(itemStack -> Block.popResource(level, currentBlockPos, itemStack));
            level.removeBlock(currentBlockPos, false);
            getEnergyStorage(tool).extractEnergy(getMineEnergyCost(tool), false);
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
            LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.TOOL, tool)
                    .withParameter(LootContextParams.ORIGIN, blockPos.getCenter())
                    .withOptionalParameter(LootContextParams.BLOCK_STATE, blockState);

            List<ItemStack> drops = blockState.getDrops(context);

            drops.forEach(itemStack -> Block.popResource(level, blockPos, itemStack));
            level.removeBlock(blockPos, false);
            getEnergyStorage(tool).extractEnergy(getMineEnergyCost(tool), false);

            mineVein(blockPos, blockType, level, tool);
        }
    }

    @Override
    public void addInfusedEnergy(ItemStack itemStack, int amount) {
        if(!isMaxed(itemStack))
            super.addInfusedEnergy(itemStack, amount);
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

    public int getMiningSpeed(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("mining_speed")) {
            itemStack.getTag().putInt("mining_speed", 10);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("mining_speed") : 10;
    }

    public int getMineEnergyCost(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("energy_cost")) {
            itemStack.getTag().putInt("energy_cost", energyCostPerBlock);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("energy_cost") : energyCostPerBlock;
    }


    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState p_41426_) {
        return getEnergyStorage(itemStack).getEnergyStored() > getMineEnergyCost(itemStack) ? getMiningSpeed(itemStack) : 1;
    }

    /**
     *
     * @param itemStack
     * @return true if vein size is not maxed out
     */
    public boolean increaseVeinSize(ItemStack itemStack){
        int maxVeinSize = getMaxVeinSize(itemStack);
        if(maxVeinSize < maxVeinBroke){
            itemStack.getTag().putInt("vein_size", maxVeinSize + 8);
            return false;
        }
        return true;
    }

    public boolean increaseMineEnergyCost(ItemStack itemStack){
        int energyCost = getMineEnergyCost(itemStack);
        if (energyCost > 250){
            itemStack.getTag().putInt("energy_cost", energyCost - 10);
            return false;
        }
        return true;
    }

    public boolean increaseMiningSpeed(ItemStack itemStack){
        int miningSpeed = getMiningSpeed(itemStack);
        if(miningSpeed < 30){
            itemStack.getTag().putInt("mining_speed", miningSpeed + 1);
            return false;
        }
        return true;
    }

    public boolean increaseMineArea(ItemStack itemStack){
        int mineWidth = getMineWidth(itemStack);
        int mineHeight = getMineHeight(itemStack);

        if (mineHeight < 9) {
            if (mineWidth - mineHeight == 0) {
                itemStack.getTag().putInt("mine_width", mineWidth + 1);
            } else {
                itemStack.getTag().putInt("mine_height", mineHeight + 1);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onInfuseLevelUp(ItemStack itemStack) {
        if(!itemStack.hasTag() || (itemStack.getTag().getBoolean("is_maxed")))
            return;

        if(increaseMineEnergyCost(itemStack)){
            if(increaseMiningSpeed(itemStack)){
                if(increaseVeinSize(itemStack)){
                    if(increaseMineArea(itemStack)){
                        itemStack.getTag().putBoolean("is_maxed", true);
                    }
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

        components.add(Component.empty());
        if(Screen.hasShiftDown()){
            components.add(Component.literal("Tool Stats:"));
            components.add(Component.literal(String.format("Infused Level: %d", getInfusedLevel(itemStack))));
            components.add(Component.literal(String.format("§eEnergy per block: %d§7/250 FE", getMineEnergyCost(itemStack))));
            components.add(Component.literal(String.format("§cMining speed: %d§7/30", getMiningSpeed(itemStack))));
            components.add(Component.literal(String.format("§aMax vein size: %d§7/256", getMaxVeinSize(itemStack))));
            components.add(Component.literal(String.format("§9Mining width: %d§7/9§9, height: %d§7/9", getMineWidth(itemStack), getMineHeight(itemStack))));
        } else if(Screen.hasControlDown()){
            components.add(Component.literal("§7This tool can also mine, dig and chop trees"));
            components.add(Component.literal("§6- §eWhile used on tree the tool will automatically use"));
            components.add(Component.literal("  §ethe vein miner to chop the whole tree (using Max vein size)"));
            components.add(Component.literal("§2- §aElse it will mine in area (Using Mining width & height)"));
            components.add(Component.literal("  §aif you press SHIFT while mining it will use the vein"));
            components.add(Component.literal("  §aminer instead of area!"));
        } else {
            components.add(Component.literal("§7Hold §aSHIFT§7 to show tool stats"));
            components.add(Component.literal("§7Hold §eCTRL§7 to show tool info"));
        }
    }
}

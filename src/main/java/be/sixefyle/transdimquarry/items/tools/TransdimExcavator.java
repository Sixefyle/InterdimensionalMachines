package be.sixefyle.transdimquarry.items.tools;

import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.items.tools.screen.TransdimExcavatorScreen;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class TransdimExcavator extends InfusedTool implements IModeHandle {
    private static final int MIN_COST_PER_BLOCK = 250;
    private static final int MAX_MINING_SPEED = 128;
    private static final int MAX_VEIN_SIZE = 256;
    private static final int MAX_WIDTH = 9;
    private static final int MAX_HEIGHT = 9;

    static final int energyCostPerBlock = 1000;
    static final int defaultVeinSize = 16;
    int currentBrokenVeinBlock = 0;

    public TransdimExcavator() {
        super(new Properties().setNoRepair().fireResistant(), 5_000_000, energyCostPerBlock, 500_000);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState p_41450_) {
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if(!(livingEntity instanceof Player player)) return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);

        if(blockState.is(BlockTags.LOGS) || player.isShiftKeyDown()){
            currentBrokenVeinBlock = 0;
            mineVein(blockPos, blockState.getBlock(), level, player.getMainHandItem());
        } else {
            mineBlockArea(blockPos, level, livingEntity, player.getMainHandItem());
        }

        return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
    }

    private void mineBlockArea(BlockPos blockPos, Level level, LivingEntity livingEntity, ItemStack tool) {
        BlockHitResult trace = getPlayerPOVHitResult(level, (Player) livingEntity, ClipContext.Fluid.NONE);
        Direction facing = trace.getDirection();

        int height = getMineHeight(tool);
        boolean shouldStartFromBottom = height > 1;
        double width = getMineWidth(tool)/2.0;
        BlockPos topLeft = blockPos.relative(facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : Direction.UP, shouldStartFromBottom ? height - 2 : 0).relative(facing.getAxis() == Direction.Axis.Y ? Direction.WEST : facing.getCounterClockWise(), (int) width);
        width = Math.ceil(width - 1);

        BlockPos bottomRight = blockPos.relative(facing.getAxis() == Direction.Axis.Y ? Direction.SOUTH : Direction.DOWN, shouldStartFromBottom ? 1 : 0).relative(facing.getAxis() == Direction.Axis.Y ? Direction.EAST : facing.getClockWise(), (int) width);
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(topLeft, bottomRight);

        for (BlockPos currentBlockPos : iterable) {
            BlockState blockState = level.getBlockState(currentBlockPos);
            if(blockState.is(Blocks.BEDROCK) || blockState.is(Blocks.AIR)) continue;

            LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.TOOL, tool)
                    .withParameter(LootContextParams.ORIGIN, blockPos.getCenter())
                    .withOptionalParameter(LootContextParams.BLOCK_STATE, blockState);
            List<ItemStack> drops = blockState.getDrops(context);

            drops.forEach(itemStack -> Block.popResource(level, livingEntity.blockPosition(), itemStack));
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
    public void addInfusedEnergy(ItemStack itemStack, long amount) {
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

    public int getMaxMineWidth(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("max_mine_width")) {
            itemStack.getTag().putInt("max_mine_width", 3);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("max_mine_width") : 3;
    }

    public int getMineWidth(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("mine_width")) {
            itemStack.getTag().putInt("mine_width", getMaxMineWidth(itemStack));
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("mine_width") : getMaxMineWidth(itemStack);
    }

    public void setMineWidth(ItemStack itemStack, int amount){
        amount = Math.min(Math.abs(amount), getMaxMineWidth(itemStack));
        itemStack.getTag().putInt("mine_width", amount);
    }

    public int getMaxMineHeight(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("max_mine_height")) {
            itemStack.getTag().putInt("max_mine_height", 3);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("max_mine_height") : 3;
    }

    public int getMineHeight(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("mine_height")) {
            itemStack.getTag().putInt("mine_height", getMaxMineHeight(itemStack));
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("mine_height") : getMaxMineHeight(itemStack);
    }

    public void setMineHeight(ItemStack itemStack, int amount){
        amount = Math.min(Math.abs(amount), getMaxMineHeight(itemStack));
        itemStack.getTag().putInt("mine_height", amount);
    }

    public int getMaxMiningSpeed(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("max_mining_speed")) {
            itemStack.getTag().putInt("max_mining_speed", 10);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("max_mining_speed") : 10;
    }

    public int getMineSpeed(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("mining_speed")) {
            itemStack.getTag().putInt("mining_speed", getMaxMiningSpeed(itemStack));
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("mining_speed") : getMaxMiningSpeed(itemStack);
    }

    public void setMineSpeed(ItemStack itemStack, int amount){
        amount = Math.min(Math.abs(amount), getMaxMiningSpeed(itemStack));
        itemStack.getTag().putInt("mining_speed", amount);
    }

    /**
     * The energy cost will depend on the percentage of speed used with the max speed of the tool
     * @param itemStack
     * @return energy cost with mine speed multiplicator
     */
    public int getMineEnergyCost(ItemStack itemStack){
        double usagePercentage = (double) getMineSpeed(itemStack) / getMaxMiningSpeed(itemStack);
        return (int)(getBaseMineEnergyCost(itemStack) * usagePercentage);
    }

    public int getMineEnergyCost(ItemStack itemStack, double perc){
        return (int)(getBaseMineEnergyCost(itemStack) * perc);
    }

    public int getBaseMineEnergyCost(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("energy_cost")) {
            itemStack.getTag().putInt("energy_cost", energyCostPerBlock);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("energy_cost") : energyCostPerBlock;
    }


    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState p_41426_) {
        //return getMineSpeed(itemStack);
        return getEnergyStorage(itemStack).getEnergyStored() > getMineEnergyCost(itemStack) ? getMineSpeed(itemStack) : 1;
    }

    /**
     *
     * @param itemStack
     * @return false if vein size is not maxed out
     */
    public boolean increaseVeinSize(ItemStack itemStack){
        int maxVeinSize = getMaxVeinSize(itemStack);
        if(maxVeinSize < MAX_VEIN_SIZE){
            itemStack.getTag().putInt("vein_size", maxVeinSize + 8);
            return false;
        }
        return true;
    }

    public boolean increaseMineEnergyCost(ItemStack itemStack){
        int energyCost = getMineEnergyCost(itemStack);
        if (energyCost > MIN_COST_PER_BLOCK){
            itemStack.getTag().putInt("energy_cost", energyCost - 10);
            return false;
        }
        return true;
    }

    public boolean increaseMiningSpeed(ItemStack itemStack){
        int miningSpeed = getMaxMiningSpeed(itemStack);
        if(miningSpeed < MAX_MINING_SPEED){
            itemStack.getTag().putInt("max_mining_speed", miningSpeed + 2);
            return false;
        }
        return true;
    }

    public boolean increaseMineArea(ItemStack itemStack){
        int mineWidth = getMaxMineWidth(itemStack);
        int mineHeight = getMaxMineHeight(itemStack);

        if (mineHeight < MAX_HEIGHT) {
            if (mineWidth - mineHeight == 0) {
                itemStack.getTag().putInt("max_mine_width", mineWidth + 1);
            } else {
                itemStack.getTag().putInt("max_mine_height", mineHeight + 1);
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
    public double getNeededInfusedEnergy(ItemStack itemStack) {
        return getBaseNeededInfusedEnergy() + (getInfusedLevel(itemStack) * 175_000);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        //tooltip.add(Component.empty());

        if(Screen.hasShiftDown()){
            tooltip.add(EnumColor.GRAY.getColoredComponent("Tool Details:"));
            tooltip.add(EnumColor.PURPLE.getColoredComponent("Infused Level: ")
                    .append(EnumColor.GRAY.getColoredComponent(String.valueOf(getInfusedLevel(itemStack)))));
            tooltip.add(EnumColor.TEAL.getColoredComponent("Energy Per Block: ")
                    .append(EnumColor.GRAY.getColoredComponent(String.valueOf(getBaseMineEnergyCost(itemStack))))
                    .append(EnumColor.DARK_GRAY.getColoredComponent("->" + MIN_COST_PER_BLOCK + " FE")));
            tooltip.add(EnumColor.RED.getColoredComponent("Mining Speed: ")
                    .append(EnumColor.GRAY.getColoredComponent(String.valueOf(getMaxMiningSpeed(itemStack))))
                    .append(EnumColor.DARK_GRAY.getColoredComponent("->" + MAX_MINING_SPEED)));
            tooltip.add(EnumColor.YELLOW.getColoredComponent("Max Vein Size: ")
                    .append(EnumColor.GRAY.getColoredComponent(String.valueOf(getMaxVeinSize(itemStack))))
                    .append(EnumColor.DARK_GRAY.getColoredComponent("->" + MAX_VEIN_SIZE)));
            tooltip.add(EnumColor.BLUE.getColoredComponent("Mining")
                    .append(EnumColor.BLUE.getColoredComponent(" Width: "))
                    .append(EnumColor.GRAY.getColoredComponent(String.valueOf(getMaxMineWidth(itemStack))))
                    .append(EnumColor.DARK_GRAY.getColoredComponent("->" + MAX_WIDTH)));
            tooltip.add(EnumColor.BLUE.getColoredComponent("        Height: ")
                    .append(EnumColor.GRAY.getColoredComponent(String.valueOf(getMaxMineHeight(itemStack))))
                    .append(EnumColor.DARK_GRAY.getColoredComponent("->" + MAX_HEIGHT)));
        } else if(Screen.hasControlDown()){
            tooltip.add(Component.literal("§7This tool can also mine, dig and chop trees"));
            tooltip.add(Component.literal("§6- §eWhile used on tree the tool will automatically use"));
            tooltip.add(Component.literal("  §ethe vein miner to chop the whole tree (using Max vein size)"));
            tooltip.add(Component.literal("§2- §aElse it will mine in area (Using Mining width & height)"));
            tooltip.add(Component.literal("  §aif you press SHIFT while mining it will use the vein"));
            tooltip.add(Component.literal("  §aminer instead of area!"));
        } else {
            super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

            tooltip.add(EnumColor.GRAY.getColoredComponent("Hold [")
                    .append(EnumColor.PURPLE.getColoredComponent("SHIFT"))
                    .append(EnumColor.GRAY.getColoredComponent("] to show tool details")));
            tooltip.add(EnumColor.GRAY.getColoredComponent("Hold [")
                    .append(EnumColor.YELLOW.getColoredComponent("CTRL"))
                    .append(EnumColor.GRAY.getColoredComponent("] to show tool info")));
        }
    }

    @Override
    public void onChangeMode(ItemStack itemStack, Object... params) {
        setMineSpeed(itemStack, (int) params[0]);
        setMineWidth(itemStack, (int) params[1]);
        setMineHeight(itemStack, (int) params[2]);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Screen getScreen(ItemStack itemStack) {
        return new TransdimExcavatorScreen(itemStack);
    }
}

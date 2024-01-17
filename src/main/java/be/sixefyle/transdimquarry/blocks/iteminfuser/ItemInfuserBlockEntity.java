package be.sixefyle.transdimquarry.blocks.iteminfuser;

import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.items.CalibratorItem;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
import be.sixefyle.transdimquarry.recipes.iteminfuser.ItemInfuserRecipe;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemInfuserBlockEntity extends TransDimMachine {
    public static final int CONTAINER_SIZE = 4;
    public static final long ENERGY_CAPACITY = CommonConfig.ITEM_INFUSER_ENERGY_CAPACITY.get();
    public static final short CALIBRATOR_SLOT = 0;
    public static final short HARMONIZATION_MATRIX_SLOT = 1;
    public static final short INPUT_SLOT = 2;
    public static final short OUTPUT_SLOT = 3;

    public ItemInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.ITEM_INFUSER.get(), pos, state, CONTAINER_SIZE, ENERGY_CAPACITY);
        setMaxProgress(CommonConfig.ITEM_INFUSER_TIME_TO_INFUSE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Item Infuser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new ItemInfuserMenu(id, inventory, this, this.getBaseData());
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if(slot == CALIBRATOR_SLOT) {
            return isCalibrator(itemStack);
        } else if(slot == OUTPUT_SLOT){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        if(direction == null) return false;
        Direction blockDirection = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        if(slot == CALIBRATOR_SLOT && direction.get2DDataValue() == (blockDirection.get2DDataValue() + 1) % 4) {
            return isCalibrator(itemStack);
        } else if(slot == INPUT_SLOT && direction.get2DDataValue() == (blockDirection.get2DDataValue() + 2) % 4){
            return true;
        } else if(slot == HARMONIZATION_MATRIX_SLOT && direction.get2DDataValue() == (blockDirection.get2DDataValue() + 3) % 4){
            return true;
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        if(slot == OUTPUT_SLOT && direction.equals(Direction.UP)) {
            return true;
        }
        return super.canTakeItemThroughFace(slot, itemStack, direction);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        super.setItem(slot, itemStack);

        ItemStack input = getItem(INPUT_SLOT);
        ItemStack harmonizationMatrix = getItem(HARMONIZATION_MATRIX_SLOT);

        if(!input.isEmpty() && !harmonizationMatrix.isEmpty()){
            ItemInfuserRecipe recipe = ItemInfuserRecipe.getRecipe(input, harmonizationMatrix);
            if(recipe != null){
                setEnergyNeeded(recipe.getEnergyCost());
            }
        } else {
            setEnergyNeeded(0);
        }
    }

    public boolean isCalibrator(ItemStack itemStack){
        return itemStack.getItem() instanceof CalibratorItem;
    }

    public boolean canUseCalibrator(ItemInfuserRecipe recipe, ItemStack calibrator){
        if(recipe == null || calibrator == null) return false;
        return (calibrator.getMaxDamage() - calibrator.getDamageValue()) >= recipe.getCalibratorDurabilityCost();
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ItemInfuserBlockEntity blockEntity) {
        blockEntity.onTick(level, blockPos);

        if(level.isClientSide) return;

        if(!blockPos.equals(blockEntity.getBlockPos())) return;

        ItemStack calibrator = blockEntity.getItem(CALIBRATOR_SLOT);
        ItemStack input = blockEntity.getItem(INPUT_SLOT);

        if(!calibrator.isEmpty() && !input.isEmpty()){
            ItemStack harmonizationMatrix = blockEntity.getItem(HARMONIZATION_MATRIX_SLOT);
            ItemInfuserRecipe recipe = ItemInfuserRecipe.getRecipe(input, harmonizationMatrix);
            if(recipe != null){
                if(blockEntity.getEnergy() >= blockEntity.getNeededEnergy() && blockEntity.canUseCalibrator(recipe, calibrator)){
                    blockEntity.getEnergyStorage().extractEnergy(blockEntity.getNeededEnergy(), false);
                    blockEntity.setProgress(blockEntity.getProgress() + 1);
                    if(blockEntity.getProgress() >= blockEntity.getMaxProgress()){
                        blockEntity.resetProgress();
                        blockEntity.getItem(INPUT_SLOT).shrink(recipe.getInput().getCount());
                        blockEntity.getItem(HARMONIZATION_MATRIX_SLOT).shrink(recipe.getHarmonizationMatrix().getCount());

                        calibrator.setDamageValue(calibrator.getDamageValue() + recipe.getCalibratorDurabilityCost());
                        if(calibrator.getDamageValue() >= calibrator.getMaxDamage()){
                            calibrator.shrink(1);
                        }

                        ItemStack output = blockEntity.getItem(OUTPUT_SLOT);

                        if(output.isEmpty()){
                            blockEntity.setItem(OUTPUT_SLOT, recipe.getOutput());
                        } else if(!output.isEmpty() && output.is(recipe.getOutput().getItem())){
                            output.grow(recipe.getOutput().getCount());
                        }
                    }
                }
            }
        } else {
            blockEntity.resetProgress();
        }
    }
}

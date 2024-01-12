package be.sixefyle.transdimquarry.blocks.iteminfuser;

import be.sixefyle.transdimquarry.blocks.BaseEnergyContainerBlockEntity;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
import be.sixefyle.transdimquarry.customrecipes.iteminfuser.ItemInfuserRecipe;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemInfuserBlockEntity extends BaseEnergyContainerBlockEntity {
    public static final int CONTAINER_SIZE = 4;
    public static final int ENERGY_CAPACITY = 100_000;
    public static final short CALIBRATOR_SLOT = 0;
    public static final short HARMONIZATION_MATRIX_SLOT = 1;
    public static final short INPUT_SLOT = 2;
    public static final short OUTPUT_SLOT = 3;

    public ItemInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.ITEM_INFUSER.get(), pos, state, CONTAINER_SIZE, ENERGY_CAPACITY);
        setMaxProgress(200);
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
            return itemStack.is(ItemRegister.CALIBRATOR.get());
        } else if(slot == OUTPUT_SLOT){
            return false;
        } else {
            return true;
        }
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

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, ItemInfuserBlockEntity blockEntity) {
        if(level.isClientSide) return;

        if(blockEntity.getEnergy() > 0){
            PacketSender.sendToClients(new EnergySyncPacket(blockEntity.getEnergy(), blockPos));
        }

        ItemStack calibrator = blockEntity.getItem(CALIBRATOR_SLOT);
        ItemStack input = blockEntity.getItem(INPUT_SLOT);

        if(!calibrator.isEmpty() && !input.isEmpty()){
            ItemStack harmonizationMatrix = blockEntity.getItem(HARMONIZATION_MATRIX_SLOT);
            ItemInfuserRecipe recipe = ItemInfuserRecipe.getRecipe(input, harmonizationMatrix);
            if(recipe != null){
                if(blockEntity.getNeededEnergy() != recipe.getEnergyCost()){
                    blockEntity.setEnergyNeeded(recipe.getEnergyCost());
                }

                if(blockEntity.getEnergy() >= blockEntity.getNeededEnergy()){
                    blockEntity.setProgress(blockEntity.getProgress() + 1);
                    if(blockEntity.getProgress() >= blockEntity.getMaxProgress()){
                        blockEntity.setProgress(0);
                        blockEntity.getItem(INPUT_SLOT).shrink(recipe.getInput().getCount());
                        blockEntity.getItem(HARMONIZATION_MATRIX_SLOT).shrink(recipe.getHarmonizationMatrix().getCount());

                        calibrator.setDamageValue(calibrator.getDamageValue() + recipe.getCalibratorDurabilityCost());
                        if(calibrator.getDamageValue() >= calibrator.getMaxDamage()){
                            calibrator.shrink(1);
                        }

                        ItemStack output = blockEntity.getItem(OUTPUT_SLOT);

                        if(output.isEmpty()){
                            blockEntity.setItem(OUTPUT_SLOT, recipe.getOutput().copy());
                        } else if(!output.isEmpty() && output.is(recipe.getOutput().getItem())){
                            output.grow(recipe.getOutput().getCount());
                        }
                    }
                }
            }
        } else {
            blockEntity.setProgress(0);
        }
    }
}

package be.sixefyle.transdimquarry.blocks.toolinfuser;

import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import be.sixefyle.transdimquarry.networking.PacketSender;
import be.sixefyle.transdimquarry.networking.packet.stc.EnergySyncPacket;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.items.tools.InfusedTool;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TransdimToolInfuserBlockEntity extends TransDimMachine {

    public static final int CONTAINER_SIZE = 1;

    public TransdimToolInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.TRANSDIMENSIONAL_TOOL_INFUSER.get(), pos, state, CONTAINER_SIZE, CommonConfig.TOOL_INFUSER_ENERGY_CAPACITY.get());
        setMaxProgress(CommonConfig.TOOL_INFUSER_MAX_PROGRESS.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Trans-dimensional Tool Infuser");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TransdimToolInfuserMenu(id, inventory, this, this.getBaseData());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TransdimToolInfuserBlockEntity blockEntity) {
        if(level.isClientSide) return;

        if(!pos.equals(blockEntity.getBlockPos())) return;

        PacketSender.sendToClients(new EnergySyncPacket(blockEntity.getEnergy(), pos));

        setChanged(level, pos, state);

        long energyCost = blockEntity.getNeededEnergy();
        ItemStack itemStack = blockEntity.getItem(0);
        if(!itemStack.isEmpty() && energyCost > 0 && blockEntity.getEnergy() >= energyCost){
            boolean isItemMaxed = itemStack.hasTag() && itemStack.getTag().getBoolean("is_maxed");
            blockEntity.setProgress(blockEntity.getProgress() + 1);
            if(!isItemMaxed && blockEntity.getProgress() >= blockEntity.getMaxProgress()){
                blockEntity.getEnergyStorage().extractEnergy(energyCost, false);

                if(!itemStack.isEmpty() && itemStack.getItem() instanceof InfusedTool tool) {
                    if(!itemStack.hasTag())
                        itemStack.setTag(new CompoundTag());

                    tool.addInfusedEnergy(itemStack, energyCost);
                }

                blockEntity.resetProgress();
                blockEntity.setMaxProgress(Math.max(CommonConfig.TOOL_INFUSER_MIN_PROGRESS.get(), blockEntity.getMaxProgress() - 5));
            }
        } else {
            blockEntity.resetProgress();
            blockEntity.setMaxProgress(CommonConfig.TOOL_INFUSER_MAX_PROGRESS.get());
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return itemStack.getItem() instanceof InfusedTool;
    }
}

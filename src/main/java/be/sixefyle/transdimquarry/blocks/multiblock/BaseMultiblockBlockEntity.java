package be.sixefyle.transdimquarry.blocks.multiblock;

import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseMultiblockBlockEntity extends TransDimMachine {

    private boolean isMaster;
    private BaseMultiblockBlockEntity master;
    private VoxelCuboid minCuboid;
    private VoxelCuboid maxCuboid;

    public BaseMultiblockBlockEntity(BlockEntityType<?> be, BlockPos pos, BlockState state, int containerSize, long energyCapacity) {
        super(be, pos, state, containerSize, energyCapacity);
        minCuboid = new VoxelCuboid(3,3,3);
        maxCuboid = new VoxelCuboid(9,6,9);
    }

    @Override
    protected abstract Component getDefaultName();

    @Override
    protected abstract AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_);

    public abstract CasingType getCasingType(BlockState state);

    public boolean verifyMultiblock(){
        if(getMaster() == null) return false;

        BaseMultiblockBlockEntity currentMaster;

        if (!isMaster()){
            currentMaster = getMaster();
        } else {
            currentMaster = this;
        }

        return MultiblockValidator.isValid(currentMaster);
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public BaseMultiblockBlockEntity getMaster() {
        return master;
    }

    public void setMaster(BaseMultiblockBlockEntity master) {
        this.master = master;
    }

    public VoxelCuboid getMinCuboid() {
        return minCuboid;
    }

    public void setMinCuboid(VoxelCuboid minCuboid) {
        this.minCuboid = minCuboid;
    }

    public VoxelCuboid getMaxCuboid() {
        return maxCuboid;
    }

    public void setMaxCuboid(VoxelCuboid maxCuboid) {
        this.maxCuboid = maxCuboid;
    }
}

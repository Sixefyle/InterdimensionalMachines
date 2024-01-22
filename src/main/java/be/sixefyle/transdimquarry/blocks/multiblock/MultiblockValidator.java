package be.sixefyle.transdimquarry.blocks.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class MultiblockValidator {

    public static VoxelCuboid getSize(BaseMultiblockBlockEntity blockEntity){
        BlockPos pos = blockEntity.getBlockPos();
        Level level = blockEntity.getLevel();

        if(level == null) return null;

        int x = 0;
        int y = 0;
        int z = 0;
        while(blockEntity.getMinCuboid().length() >= x
                && blockEntity.getMaxCuboid().height() < x
                && blockEntity.getCasingType(level.getBlockState(pos.offset(1,0,0))) != CasingType.INVALIDE){
            x++;
            while(blockEntity.getMinCuboid().height() >= y
                    && blockEntity.getMaxCuboid().height() < y
                    && blockEntity.getCasingType(level.getBlockState(pos.offset(0,1,0))) != CasingType.INVALIDE){
                y++;
                while(blockEntity.getMinCuboid().width() >= z
                        && blockEntity.getMaxCuboid().height() < z
                        && blockEntity.getCasingType(level.getBlockState(pos.offset(0,0,1))) != CasingType.INVALIDE){
                    z++;
                }
            }
        }

        return new VoxelCuboid(x, y, z);
    }

    public static boolean isValid(BaseMultiblockBlockEntity blockEntity){
        VoxelCuboid size = getSize(blockEntity);

        return size.greaterOrEqual(blockEntity.getMaxCuboid());
    }
}

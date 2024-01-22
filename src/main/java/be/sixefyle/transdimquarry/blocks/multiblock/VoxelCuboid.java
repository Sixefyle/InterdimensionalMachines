package be.sixefyle.transdimquarry.blocks.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

//Credit to Mekanism: https://github.com/mekanism/Mekanism/blob/1.20.x/src/main/java/mekanism/common/lib/math/voxel/VoxelCuboid.java#L7
public class VoxelCuboid {

    private BlockPos minPos;
    private BlockPos maxPos;

    public VoxelCuboid(BlockPos minPos, BlockPos maxPos) {
        this.minPos = minPos;
        this.maxPos = maxPos;
    }

    public VoxelCuboid(int length, int height, int width) {
        this(BlockPos.ZERO, new BlockPos(length - 1, height - 1, width - 1));
    }

    public int length() {
        return maxPos.getX() - minPos.getX() + 1;
    }

    public int width() {
        return maxPos.getZ() - minPos.getZ() + 1;
    }

    public int height() {
        return maxPos.getY() - minPos.getY() + 1;
    }

    public BlockPos getMinPos() {
        return minPos;
    }

    public BlockPos getMaxPos() {
        return maxPos;
    }

    public void setMinPos(BlockPos minPos) {
        this.minPos = minPos;
    }

    public void setMaxPos(BlockPos maxPos) {
        this.maxPos = maxPos;
    }

    public BlockPos getCenter() {
        return new BlockPos((minPos.getX() + maxPos.getX()) / 2,
                (minPos.getY() + maxPos.getY()) / 2,
                (minPos.getZ() + maxPos.getZ()) / 2);
    }

    public Direction getSide(BlockPos pos) {
        if (pos.getX() == minPos.getX()) {
            return Direction.WEST;
        } else if (pos.getX() == maxPos.getX()) {
            return Direction.EAST;
        } else if (pos.getY() == minPos.getY()) {
            return Direction.DOWN;
        } else if (pos.getY() == maxPos.getY()) {
            return Direction.UP;
        } else if (pos.getZ() == minPos.getZ()) {
            return Direction.NORTH;
        } else if (pos.getZ() == maxPos.getZ()) {
            return Direction.SOUTH;
        }
        return null;
    }

    public boolean isOnSide(BlockPos pos) {
        return getWallRelative(pos).isWall();
    }

    public boolean isOnEdge(BlockPos pos) {
        return getWallRelative(pos).isOnEdge();
    }

    public boolean isOnCorner(BlockPos pos) {
        return getWallRelative(pos).isOnCorner();
    }

    public WallRelative getWallRelative(BlockPos pos) {
        int matches = getMatches(pos);
        if (matches >= 3) {
            return WallRelative.CORNER;
        } else if (matches == 2) {
            return WallRelative.EDGE;
        } else if (matches == 1) {
            return WallRelative.SIDE;
        }
        return WallRelative.INVALID;
    }

    public int getMatches(BlockPos pos) {
        int matches = 0;
        if (pos.getX() == minPos.getX()) {
            matches++;
        }
        if (pos.getX() == maxPos.getX()) {
            matches++;
        }
        if (pos.getY() == minPos.getY()) {
            matches++;
        }
        if (pos.getY() == maxPos.getY()) {
            matches++;
        }
        if (pos.getZ() == minPos.getZ()) {
            matches++;
        }
        if (pos.getZ() == maxPos.getZ()) {
            matches++;
        }
        return matches;
    }

    public boolean greaterOrEqual(VoxelCuboid other) {
        return length() >= other.length() && width() >= other.width() && height() >= other.height();
    }

    public CuboidRelative getRelativeLocation(BlockPos pos) {
        if (pos.getX() > minPos.getX() && pos.getX() < maxPos.getX() &&
                pos.getY() > minPos.getY() && pos.getY() < maxPos.getY() &&
                pos.getZ() > minPos.getZ() && pos.getZ() < maxPos.getZ()) {
            return CuboidRelative.INSIDE;
        } else if (pos.getX() < minPos.getX() || pos.getX() > maxPos.getX() ||
                pos.getY() < minPos.getY() || pos.getY() > maxPos.getY() ||
                pos.getZ() < minPos.getZ() || pos.getZ() > maxPos.getZ()) {
            return CuboidRelative.OUTSIDE;
        }
        return CuboidRelative.WALLS;
    }

    public enum CuboidRelative {
        INSIDE,
        OUTSIDE,
        WALLS;

        public boolean isWall() {
            return this == WALLS;
        }
    }

    public enum WallRelative {
        SIDE,
        EDGE,
        CORNER,
        INVALID;

        public boolean isWall() {
            return this != INVALID;
        }

        public boolean isOnEdge() {
            return this == EDGE || this == CORNER;
        }

        public boolean isOnCorner() {
            return this == CORNER;
        }
    }
}

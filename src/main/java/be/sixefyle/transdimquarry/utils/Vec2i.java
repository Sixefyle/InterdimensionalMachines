package be.sixefyle.transdimquarry.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class Vec2i {
    public static final Vec2i ZERO = new Vec2i(0, 0);
    public static final Vec2i ONE = new Vec2i(1, 1);
    public static final Vec2i UNIT_X = new Vec2i(1, 0);
    public static final Vec2i NEG_UNIT_X = new Vec2i(-1, 0);
    public static final Vec2i UNIT_Y = new Vec2i(0, 1);
    public static final Vec2i NEG_UNIT_Y = new Vec2i(0, -1);
    public static final Vec2i MAX = new Vec2i(Integer.MAX_VALUE, Integer.MAX_VALUE);
    public static final Vec2i MIN = new Vec2i(Integer.MIN_VALUE, Integer.MIN_VALUE);
    public final int x;
    public final int y;

    public Vec2i(int pX, int pY) {
        this.x = pX;
        this.y = pY;
    }

    public Vec2i scale(int pFactor) {
        return new Vec2i(this.x * pFactor, this.y * pFactor);
    }

    public int dot(Vec2i pOther) {
        return this.x * pOther.x + this.y * pOther.y;
    }

    public Vec2i add(Vec2i pOther) {
        return new Vec2i(this.x + pOther.x, this.y + pOther.y);
    }

    public Vec2i add(int pValue) {
        return new Vec2i(this.x + pValue, this.y + pValue);
    }

    public boolean equals(Vec2i pOther) {
        return this.x == pOther.x && this.y == pOther.y;
    }

    public Vec2 normalized() {
        float f = Mth.sqrt(this.x * this.x + this.y * this.y);
        return f < 1.0E-4F ? Vec2.ZERO : new Vec2(this.x / f, this.y / f);
    }

    public int length() {
        return (int) Mth.sqrt(this.x * this.x + this.y * this.y);
    }

    public int lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public int distanceToSqr(Vec2i pOther) {
        int f = pOther.x - this.x;
        int f1 = pOther.y - this.y;
        return f * f + f1 * f1;
    }

    public Vec2i negated() {
        return new Vec2i(-this.x, -this.y);
    }
}

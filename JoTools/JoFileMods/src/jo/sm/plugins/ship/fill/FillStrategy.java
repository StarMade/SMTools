package jo.sm.plugins.ship.fill;

import java.util.Comparator;

import jo.vecmath.Point3i;

public class FillStrategy implements Comparator<Point3i>
{
    public static final int MINUS = 0;
    public static final int PLUS = 1;
    public static final int CENTER = 2;
    public static final int OUTSIDE = 3;
    
    public static final int X = 0x01;
    public static final int Y = 0x02;
    public static final int Z = 0x04;

    private int mStrategy;
    private int mAxis;
    
    public FillStrategy(int strategy, int axis)
    {
        mStrategy = strategy;
        mAxis = axis;
    }
    
    @Override
    public int compare(Point3i o1, Point3i o2)
    {
        int v1 = 0;
        int v2 = 0;
        if (mAxis == X)
        {
            v1 = o1.x;
            v2 = o2.x;
        }
        else if (mAxis == Y)
        {
            v1 = o1.y;
            v2 = o2.y;
        }
        else if (mAxis == Z)
        {
            v1 = o1.z;
            v2 = o2.z;
        }
        if (mStrategy == MINUS)
        {
            if (v1 < v2)
                return -1;
            else if (v1 > v2)
                return 1;
            return 0;
        }
        if (mStrategy == PLUS)
        {
            if (v1 < v2)
                return 1;
            else if (v1 > v2)
                return -1;
            return 0;
        }
        if (mStrategy == CENTER)
        {
            v1 = Math.abs(v1);
            v2 = Math.abs(v2);
            if (v1 < v2)
                return -1;
            else if (v1 > v2)
                return 1;
            return 0;
        }
        if (mStrategy == OUTSIDE)
        {
            v1 = Math.abs(v1);
            v2 = Math.abs(v2);
            if (v1 < v2)
                return 1;
            else if (v1 > v2)
                return -1;
            return 0;
        }
        return 0;
    }

}

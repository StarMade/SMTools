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
    private Point3i mLower;
    private Point3i mUpper;
    
    public FillStrategy(int strategy, int axis, Point3i lower, Point3i upper)
    {
        mStrategy = strategy;
        mAxis = axis;
        mLower = lower;
        mUpper = upper;
    }
    
//    private Set<Point3i> done = new HashSet<Point3i>();
    
    @Override
    public int compare(Point3i o1, Point3i o2)
    {
        int weight1 = 0;
        int weight2 = 0;
        if ((mAxis&X) != 0)
        {
            weight1 += getWeight(o1.x, mLower.x, mUpper.x);
            weight2 += getWeight(o2.x, mLower.x, mUpper.x);
        }
        if ((mAxis&Y) != 0)
        {
            weight1 += getWeight(o1.y, mLower.y, mUpper.y);
            weight2 += getWeight(o2.y, mLower.y, mUpper.y);
        }
        if ((mAxis&Z) != 0)
        {
            weight1 += getWeight(o1.z, mLower.z, mUpper.z);
            weight2 += getWeight(o2.z, mLower.z, mUpper.z);
        }
//        if (!done.contains(o1))
//        {
//            System.out.println(o1+" weight of "+weight1+" axis="+Integer.toHexString(mAxis)+", low="+mLower+", high="+mUpper);
//            done.add(o1);
//        }
//        if (!done.contains(o2))
//        {
//            System.out.println(o2+" weight of "+weight2+" axis="+Integer.toHexString(mAxis)+", low="+mLower+", high="+mUpper);
//            done.add(o2);
//        }
        return weight1 - weight2;
    }

    // the higher the value the less desirable this position is
    private int getWeight(int v, int low, int high)
    {
        if (mStrategy == MINUS)
            return v - low;
        if (mStrategy == PLUS)
            return high - v;
        if (mStrategy == CENTER)
            return Math.abs(v);
        if (mStrategy == OUTSIDE)
            return Math.min(v - low, high - v);
        return 0;
    }
    
}

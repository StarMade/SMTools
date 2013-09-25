package jo.sm.data;

import java.util.Iterator;

import jo.vecmath.Point3i;

public class CubeIterator implements Iterator<Point3i>
{
    private Point3i mLower;
    private Point3i mUpper;
    private Point3i mDelta;
    private Point3i mNext;
    
    public CubeIterator(Point3i lower, Point3i upper)
    {
    	if ((lower == null) || (upper == null))
    		mNext = null;
    	else
    	{
	        mLower = new Point3i(lower);
	        mUpper = new Point3i(upper);
	        mDelta = new Point3i(1, 1, 1);
	        mNext = new Point3i(lower);
    	}
    }

    public CubeIterator(Point3i lower, Point3i upper, Point3i delta)
    {
    	if ((lower == null) || (upper == null))
    		mNext = null;
    	else
    	{
	        mLower = new Point3i(lower);
	        mUpper = new Point3i(upper);
	        mDelta = new Point3i(delta);
	        mNext = new Point3i(lower);
    	}
    }

    @Override
    public boolean hasNext()
    {
        return mNext != null;
    }

    @Override
    public Point3i next()
    {
    	if (mNext == null)
    		return null;
        Point3i next = new Point3i(mNext);
        mNext.x += mDelta.x;
        if (mNext.x > mUpper.x)
        {
            mNext.x = mLower.x;
            mNext.y += mDelta.y;
            if (mNext.y > mUpper.y)
            {
                mNext.y = mLower.y;
                mNext.z += mDelta.z;
                if (mNext.z > mUpper.z)
                    mNext = null;
            }
        }
        return next;
    }

    @Override
    public void remove()
    {
    }

}

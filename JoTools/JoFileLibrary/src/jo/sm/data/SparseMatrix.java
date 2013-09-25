package jo.sm.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.vecmath.Point3i;
import jo.vecmath.Point3s;

public class SparseMatrix<T>
{
    private Map<Integer,Map<Integer,Map<Integer,T>>> mMatrix;
    private Point3i		mLower;
    private Point3i		mUpper;

    public SparseMatrix()
    {
        mMatrix = new HashMap<Integer, Map<Integer,Map<Integer,T>>>();
        mLower = null;
        mUpper = null;
    }
    
    public SparseMatrix(SparseMatrix<T> original)
    {
        this();
        set(original);
        mLower = new Point3i();
        mUpper = new Point3i();
        original.getBounds(mLower, mUpper);
    }
    
    public void addAll(SparseMatrix<T> original)
    {
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            set(p, original.get(p));
        }
    }
    
    public void set(SparseMatrix<T> original)
    {
        mMatrix.clear();
        addAll(original);
    }
    
    public void set(int x, int y, int z, T val)
    {
        Map<Integer,Map<Integer,T>> xrow = mMatrix.get(x);
        if (xrow == null)
        {
            xrow = new HashMap<Integer, Map<Integer,T>>();
            mMatrix.put(x, xrow);
        }
        Map<Integer,T> yrow = xrow.get(y);
        if (yrow == null)
        {
            yrow = new HashMap<Integer, T>();
            xrow.put(y,  yrow);
        }
        if (val == null)
            yrow.remove(z);
        else
            yrow.put(z,  val);
        if (mLower == null)
        	mLower = new Point3i(x, y, z);
        else
        {
        	mLower.x = Math.min(mLower.x, x);
        	mLower.y = Math.min(mLower.y, y);
        	mLower.z = Math.min(mLower.z, z);
        }
        if (mUpper == null)
        	mUpper = new Point3i(x, y, z);
        else
        {
        	mUpper.x = Math.max(mUpper.x, x);
        	mUpper.y = Math.max(mUpper.y, y);
        	mUpper.z = Math.max(mUpper.z, z);
        }
    }
    
    public T get(int x, int y, int z)
    {
        Map<Integer,Map<Integer,T>> xrow = mMatrix.get(x);
        if (xrow == null)
            return null;
        Map<Integer,T> yrow = xrow.get(y);
        if (yrow == null)
            return null;
        return yrow.get(z);        
    }
    
    public boolean contains(int x, int y, int z)
    {
        return get(x, y, z) != null;
    }
    
    public void set(Point3i v, T val)
    {
        set(v.x, v.y, v.z, val);
    }
    
    public T get(Point3i v)
    {
        return get(v.x, v.y, v.z);
    }
    
    public T get(Point3s v)
    {
        return get(v.x, v.y, v.z);
    }
    
    public boolean contains(Point3i v)
    {
        return get(v.x, v.y, v.z) != null;
    }
    
    public void getBounds(Point3i lower, Point3i upper)
    {
    	if (mLower != null)
    		lower.set(mLower);
    	if (mUpper != null)
    		upper.set(mUpper);
    }
    
    public Point3i find(T val)
    {
        for (Integer x : mMatrix.keySet())
        {
            Map<Integer,Map<Integer,T>> xrow = mMatrix.get(x);
            for (Integer y : xrow.keySet())
            {
                Map<Integer,T> yrow = xrow.get(y);
                for (Integer z : yrow.keySet())
                	if (yrow.get(z) == val)
                		return new Point3i(x, y, z);
            }
        }
        return null;
    }

    public Iterator<Point3i> iterator()
    {
        return new CubeIterator(mLower, mUpper);
    }
    
    public Iterator<Point3i> iteratorNonNull()
    {
        List<Point3i> points = new ArrayList<Point3i>();
        for (Iterator<Point3i> i = iterator(); i.hasNext(); )
        {
        	Point3i p = i.next();
        	if (contains(p))
        		points.add(p);
        }
        return points.iterator();
    }
    
    public int size()
    {
        int size = 0;
        for (Iterator<Point3i> i = iteratorNonNull(); i.hasNext(); i.next())
            size++;
        return size;
    }
}

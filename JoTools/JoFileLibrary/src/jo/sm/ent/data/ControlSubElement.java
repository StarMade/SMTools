package jo.sm.ent.data;

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Point3i;

public class ControlSubElement
{
    private short       mVal;
    private List<Point3i>   mVals;
    
    public ControlSubElement()
    {
        mVals = new ArrayList<Point3i>();
    }
    
    public short getVal()
    {
        return mVal;
    }
    public void setVal(short val)
    {
        mVal = val;
    }
    public List<Point3i> getVals()
    {
        return mVals;
    }
    public void setVals(List<Point3i> vals)
    {
        mVals = vals;
    }
}

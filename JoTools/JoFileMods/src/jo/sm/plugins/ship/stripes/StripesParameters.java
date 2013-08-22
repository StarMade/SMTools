package jo.sm.plugins.ship.stripes;

import jo.sm.data.BlockTypes;

public class StripesParameters
{
    private short   mColor1;
    private short   mColor2;
    private boolean mXAxis;
    private int     mXWidth1;
    private int     mXWidth2;
    private boolean mYAxis;
    private int     mYWidth1;
    private int     mYWidth2;
    private boolean mZAxis;
    private int     mZWidth1;
    private int     mZWidth2;
    
    public StripesParameters()
    {
        mColor1 = BlockTypes.HULL_COLOR_BLACK_ID;
        mColor2 = BlockTypes.HULL_COLOR_WHITE_ID;
        mXAxis = true;
        mXWidth1 = 1;
        mXWidth2 = 1;
        mYAxis = false;
        mYWidth1 = 1;
        mYWidth2 = 1;
        mZAxis = false;
        mZWidth1 = 1;
        mZWidth2 = 1;
    }
    
    public short getColor1()
    {
        return mColor1;
    }
    public void setColor1(short color1)
    {
        mColor1 = color1;
    }
    public short getColor2()
    {
        return mColor2;
    }
    public void setColor2(short color2)
    {
        mColor2 = color2;
    }
    public boolean isXAxis()
    {
        return mXAxis;
    }
    public void setXAxis(boolean xAxis)
    {
        mXAxis = xAxis;
    }
    public int getXWidth1()
    {
        return mXWidth1;
    }
    public void setXWidth1(int xWidth1)
    {
        mXWidth1 = xWidth1;
    }
    public int getXWidth2()
    {
        return mXWidth2;
    }
    public void setXWidth2(int xWidth2)
    {
        mXWidth2 = xWidth2;
    }
    public boolean isYAxis()
    {
        return mYAxis;
    }
    public void setYAxis(boolean yAxis)
    {
        mYAxis = yAxis;
    }
    public int getYWidth1()
    {
        return mYWidth1;
    }
    public void setYWidth1(int yWidth1)
    {
        mYWidth1 = yWidth1;
    }
    public int getYWidth2()
    {
        return mYWidth2;
    }
    public void setYWidth2(int yWidth2)
    {
        mYWidth2 = yWidth2;
    }
    public boolean isZAxis()
    {
        return mZAxis;
    }
    public void setZAxis(boolean zAxis)
    {
        mZAxis = zAxis;
    }
    public int getZWidth1()
    {
        return mZWidth1;
    }
    public void setZWidth1(int zWidth1)
    {
        mZWidth1 = zWidth1;
    }
    public int getZWidth2()
    {
        return mZWidth2;
    }
    public void setZWidth2(int zWidth2)
    {
        mZWidth2 = zWidth2;
    }
}

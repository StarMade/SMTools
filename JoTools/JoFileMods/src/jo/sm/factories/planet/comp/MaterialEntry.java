package jo.sm.factories.planet.comp;

public class MaterialEntry
{
    public static final int ABSOLUTE = 0;
    public static final int RELATIVE = 1;
    
    private short   mBlockID;
    private int     mPercent;
    private int     mLowValue;
    private int     mLowStrategy;
    private int     mHighValue;
    private int     mHighStrategy;
    
    public short getBlockID()
    {
        return mBlockID;
    }
    public void setBlockID(short blockID)
    {
        mBlockID = blockID;
    }
    public int getPercent()
    {
        return mPercent;
    }
    public void setPercent(int percent)
    {
        mPercent = percent;
    }
    public int getLowValue()
    {
        return mLowValue;
    }
    public void setLowValue(int lowValue)
    {
        mLowValue = lowValue;
    }
    public int getLowStrategy()
    {
        return mLowStrategy;
    }
    public void setLowStrategy(int lowStrategy)
    {
        mLowStrategy = lowStrategy;
    }
    public int getHighValue()
    {
        return mHighValue;
    }
    public void setHighValue(int highValue)
    {
        mHighValue = highValue;
    }
    public int getHighStrategy()
    {
        return mHighStrategy;
    }
    public void setHighStrategy(int highStrategy)
    {
        mHighStrategy = highStrategy;
    }
}

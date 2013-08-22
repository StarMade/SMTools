package jo.sm.plugins.ship.scale;


public class ScaleParameters
{
    private int     mXScale;
    private int     mYScale;
    private int     mZScale;
    
    public ScaleParameters()
    {
        mXScale = 1;
        mYScale = 1;
        mZScale = 1;
    }

    public int getXScale()
    {
        return mXScale;
    }

    public void setXScale(int xScale)
    {
        mXScale = xScale;
    }

    public int getYScale()
    {
        return mYScale;
    }

    public void setYScale(int yScale)
    {
        mYScale = yScale;
    }

    public int getZScale()
    {
        return mZScale;
    }

    public void setZScale(int zScale)
    {
        mZScale = zScale;
    }

}

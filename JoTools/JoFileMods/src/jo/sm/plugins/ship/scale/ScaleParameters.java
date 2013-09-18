package jo.sm.plugins.ship.scale;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Scale up object", shortDescription="Increase the size of the object by a even factor")
public class ScaleParameters
{
	@Description(displayName="Width", shortDescription="Scale along X axis, port/starboard")
    private int     mXScale;
	@Description(displayName="Height", shortDescription="Scale along Y axis, dorsal/ventral")
    private int     mYScale;
	@Description(displayName="Length", shortDescription="Scale along Z axis, fore/aft")
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

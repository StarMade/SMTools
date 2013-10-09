package jo.sm.plugins.ship.scale;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Scale model", shortDescription="Cange the size of the object")
public class ScaleParameters
{
	@Description(displayName="Width", shortDescription="Scale along X axis, port/starboard")
    private float     mXScale;
	@Description(displayName="Height", shortDescription="Scale along Y axis, dorsal/ventral")
    private float     mYScale;
	@Description(displayName="Length", shortDescription="Scale along Z axis, fore/aft")
    private float     mZScale;
    
    public ScaleParameters()
    {
        mXScale = 1;
        mYScale = 1;
        mZScale = 1;
    }

    public float getXScale()
    {
        return mXScale;
    }

    public void setXScale(float xScale)
    {
        mXScale = xScale;
    }

    public float getYScale()
    {
        return mYScale;
    }

    public void setYScale(float yScale)
    {
        mYScale = yScale;
    }

    public float getZScale()
    {
        return mZScale;
    }

    public void setZScale(float zScale)
    {
        mZScale = zScale;
    }

}

package jo.sm.plugins.planet.gen;

import jo.sm.ui.act.plugin.Description;



@Description(displayName="Giant's Causeway", shortDescription="Create a hexagonal slab like surface.")
public class VolcanoParameters
{
    @Description(displayName="", shortDescription="Planetary radius")
    private int     mPlanetRadius;
    @Description(displayName="", shortDescription="Tallest point above baseline")
    private int     mPlanetHeight;
    @Description(displayName="", shortDescription="Thickest point beneath baseline")
    private int     mPlanetDepth;
    @Description(displayName="", shortDescription="Radius of Caldera")
    private int     mCalderaRadius;
    @Description(displayName="", shortDescription="Depth of Caldera")
    private int     mCalderaDepth;
    @Description(displayName="", shortDescription="Should bottom be rounded (or flat)")
    private boolean mRoundedBottom;
    
    public VolcanoParameters()
    {
        mPlanetRadius = 250;
        mPlanetHeight = 127;
        mCalderaRadius = 16;
        mCalderaDepth = 32;
        mPlanetDepth = 32;
        mRoundedBottom = true;
    }

    public int getPlanetRadius()
    {
        return mPlanetRadius;
    }

    public void setPlanetRadius(int planetRadius)
    {
        mPlanetRadius = planetRadius;
    }

    public int getPlanetHeight()
    {
        return mPlanetHeight;
    }

    public void setPlanetHeight(int planetHeight)
    {
        mPlanetHeight = planetHeight;
    }

    public int getPlanetDepth()
    {
        return mPlanetDepth;
    }

    public void setPlanetDepth(int planetDepth)
    {
        mPlanetDepth = planetDepth;
    }

    public int getCalderaRadius()
    {
        return mCalderaRadius;
    }

    public void setCalderaRadius(int calderaRadius)
    {
        mCalderaRadius = calderaRadius;
    }

    public int getCalderaDepth()
    {
        return mCalderaDepth;
    }

    public void setCalderaDepth(int calderaDepth)
    {
        mCalderaDepth = calderaDepth;
    }

    public boolean isRoundedBottom()
    {
        return mRoundedBottom;
    }

    public void setRoundedBottom(boolean roundedBottom)
    {
        mRoundedBottom = roundedBottom;
    }

}

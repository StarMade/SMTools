package jo.sm.plugins.planet.gen;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;



@Description(displayName="Giant's Causeway", shortDescription="Create a hexagonal slab like surface.")
public class VolcanoParameters
{
    @Description(displayName="", shortDescription="Planetary radius")
    private int     mPlanetRadius;
    @Description(displayName="", shortDescription="Tallest point above baseline (-ve for below)")
    private int     mPlanetHeight;
    @Description(displayName="", shortDescription="Radius of Caldera")
    private int     mCalderaRadius;
    @Description(displayName="", shortDescription="Depth of Caldera")
    private int     mCalderaDepth;
    @Description(displayName="", shortDescription="Block type to fill with")
    private short mFillWith;

    public VolcanoParameters()
    {
        mPlanetRadius = 250;
        mPlanetHeight = 127;
        mCalderaRadius = 16;
        mCalderaDepth = 32;
        mFillWith = BlockTypes.TERRAIN_ROCK_ID;
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

    public short getFillWith()
    {
        return mFillWith;
    }

    public void setFillWith(short fillWith)
    {
        mFillWith = fillWith;
    }
}

package jo.sm.plugins.planet.gen;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;



@Description(displayName="Dome", shortDescription="Create a dome shaped surface.")
public class DomeParameters
{
    @Description(displayName="", shortDescription="Planetary radius")
    private int     mPlanetRadius;
    @Description(displayName="", shortDescription="Tallest point above baseline (-ve for below)")
    private int     mPlanetHeight;
    @Description(displayName="", shortDescription="Domed out (false) or in (true)")
    private boolean mConcave;
    @Description(displayName="", shortDescription="Block type to fill with")
    private short mFillWith;
    
    public DomeParameters()
    {
        mPlanetRadius = 250;
        mPlanetHeight = 32;
        mConcave = true;
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

    public boolean isConcave()
    {
        return mConcave;
    }

    public void setConcave(boolean concave)
    {
        mConcave = concave;
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

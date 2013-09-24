package jo.sm.plugins.planet.gen;

import jo.sm.ui.act.plugin.Description;



@Description(displayName="Giant's Causeway", shortDescription="Create a hexagonal slab like surface.")
public class UndulatingParameters
{
    @Description(displayName="", shortDescription="Planetary radius")
    private int     mPlanetRadius;
    @Description(displayName="", shortDescription="Tallest point above baseline")
    private int     mPlanetHeight;
    @Description(displayName="", shortDescription="Thickest point beneath baseline")
    private int     mPlanetDepth;
    @Description(displayName="", shortDescription="Should bottom mirror top")
    private boolean mReflexive;
    
    public UndulatingParameters()
    {
        mPlanetRadius = 250;
        mPlanetHeight = 32;
        mPlanetDepth = 32;
        mReflexive = false;
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

    public boolean isReflexive()
    {
        return mReflexive;
    }

    public void setReflexive(boolean reflexive)
    {
        mReflexive = reflexive;
    }
}

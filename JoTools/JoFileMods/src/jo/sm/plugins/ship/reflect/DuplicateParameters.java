package jo.sm.plugins.ship.reflect;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Force Symmetry",shortDescription="The model, or selection, will be reflected and duplicated along the axis specified above.")
public class DuplicateParameters
{
    @Description(displayName="Port/Starboard",shortDescription="Reflect along the X axis")
    private int     mXReflect;
    @Description(displayName="Dorsal/Ventral",shortDescription="Reflect along the Y axis")
    private int     mYReflect;
    @Description(displayName="Fore/Aft",shortDescription="Reflect along the Z axis")
    private int     mZReflect;
    
    public DuplicateParameters()
    {
    }

    public int getXReflect()
    {
        return mXReflect;
    }

    public void setXReflect(int xReflect)
    {
        mXReflect = xReflect;
    }

    public int getYReflect()
    {
        return mYReflect;
    }

    public void setYReflect(int yReflect)
    {
        mYReflect = yReflect;
    }

    public int getZReflect()
    {
        return mZReflect;
    }

    public void setZReflect(int zReflect)
    {
        mZReflect = zReflect;
    }
}

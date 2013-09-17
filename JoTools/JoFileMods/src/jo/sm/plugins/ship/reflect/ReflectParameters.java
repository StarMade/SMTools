package jo.sm.plugins.ship.reflect;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Reflect Model",shortDescription="The model, or selection, will be reflected along the axis specified above.")
public class ReflectParameters
{
    @Description(displayName="Port/Starboard",shortDescription="Flip the X coordinate on all blocks")
    private boolean     mXReflect;
    @Description(displayName="Dorsal/Ventral",shortDescription="Flip the Y coordinate on all blocks")
    private boolean     mYReflect;
    @Description(displayName="Fore/Aft",shortDescription="Flip the Z coordinate on all blocks")
    private boolean     mZReflect;
    
    public ReflectParameters()
    {
    }

	public boolean isXReflect()
	{
		return mXReflect;
	}

	public void setXReflect(boolean xReflect)
	{
		mXReflect = xReflect;
	}

	public boolean isYReflect()
	{
		return mYReflect;
	}

	public void setYReflect(boolean yReflect)
	{
		mYReflect = yReflect;
	}

	public boolean isZReflect()
	{
		return mZReflect;
	}

	public void setZReflect(boolean zReflect)
	{
		mZReflect = zReflect;
	}
}

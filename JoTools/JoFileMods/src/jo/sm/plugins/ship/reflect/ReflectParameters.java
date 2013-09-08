package jo.sm.plugins.ship.reflect;


public class ReflectParameters
{
    private boolean     mXReflect;
    private boolean     mYReflect;
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

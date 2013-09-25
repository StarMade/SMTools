package jo.sm.plugins.planet.select;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Select Specific Region",shortDescription="")
public class SelectSpecificParameters
{
	@Description(displayName="Start X")
    private int mLowX;
	@Description(displayName="Start Y")
    private int mLowY;
	@Description(displayName="Start Z")
    private int mLowZ;
	@Description(displayName="End X")
    private int mHighX;
	@Description(displayName="End Y")
    private int mHighY;
	@Description(displayName="End Z")
    private int mHighZ;
    
    public SelectSpecificParameters()
    {
        mLowX = 0;
        mLowY = 0;
        mLowZ = 0;
        mHighX = 16;
        mHighY = 16;
        mHighZ = 16;
    }

	public int getLowX()
	{
		return mLowX;
	}

	public void setLowX(int lowX)
	{
		mLowX = lowX;
	}

	public int getLowY()
	{
		return mLowY;
	}

	public void setLowY(int lowY)
	{
		mLowY = lowY;
	}

	public int getLowZ()
	{
		return mLowZ;
	}

	public void setLowZ(int lowZ)
	{
		mLowZ = lowZ;
	}

	public int getHighX()
	{
		return mHighX;
	}

	public void setHighX(int highX)
	{
		mHighX = highX;
	}

	public int getHighY()
	{
		return mHighY;
	}

	public void setHighY(int highY)
	{
		mHighY = highY;
	}

	public int getHighZ()
	{
		return mHighZ;
	}

	public void setHighZ(int highZ)
	{
		mHighZ = highZ;
	}
}

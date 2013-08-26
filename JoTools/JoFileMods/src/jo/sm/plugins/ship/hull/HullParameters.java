package jo.sm.plugins.ship.hull;


public class HullParameters
{
	public static final int OPEN_FRAME = 0;
	public static final int NEEDLE = 1;
	public static final int CONE = 2;
	public static final int CYLINDER = 3;
	public static final int BOX = 4;
	public static final int SPHERE = 5;
	public static final int DISC = 6;
	public static final int IRREGULAR = 7;
	
	private int	mType;
	private int	mVolume;
	private int	mCenterX;
	private int	mCenterY;
	private int	mCenterZ;
    
    public HullParameters()
    {
    	mType = SPHERE;
    	mVolume = 2000;
    	mCenterX = 8;
    	mCenterY = 8;
    	mCenterZ = 8;
    }

	public int getType()
	{
		return mType;
	}

	public void setType(int type)
	{
		mType = type;
	}

	public int getVolume()
	{
		return mVolume;
	}

	public void setVolume(int volume)
	{
		mVolume = volume;
	}

	public int getCenterX()
	{
		return mCenterX;
	}

	public void setCenterX(int centerX)
	{
		mCenterX = centerX;
	}

	public int getCenterY()
	{
		return mCenterY;
	}

	public void setCenterY(int centerY)
	{
		mCenterY = centerY;
	}

	public int getCenterZ()
	{
		return mCenterZ;
	}

	public void setCenterZ(int centerZ)
	{
		mCenterZ = centerZ;
	}
}

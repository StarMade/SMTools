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
	private int	mSizeX;
	private int	mSizeY;
	private int	mSizeZ;
	private int	mCenterX;
	private int	mCenterY;
	private int	mCenterZ;
    
    public HullParameters()
    {
    	mType = SPHERE;
    	mSizeX = 20;
    	mSizeY = 10;
    	mSizeZ = 40;
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

	public int getSizeX()
	{
		return mSizeX;
	}

	public void setSizeX(int sizeX)
	{
		mSizeX = sizeX;
	}

	public int getSizeY()
	{
		return mSizeY;
	}

	public void setSizeY(int sizeY)
	{
		mSizeY = sizeY;
	}

	public int getSizeZ()
	{
		return mSizeZ;
	}

	public void setSizeZ(int sizeZ)
	{
		mSizeZ = sizeZ;
	}
}

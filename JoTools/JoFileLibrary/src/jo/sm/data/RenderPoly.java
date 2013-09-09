package jo.sm.data;


import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class RenderPoly
{
    public static final int XP = 0;
    public static final int XM = 1;
    public static final int YP = 2;
    public static final int YM = 3;
    public static final int ZP = 4;
    public static final int ZM = 5;
    public static final int XPYP = 6;
    public static final int XPYM = 7;
    public static final int XMYP = 8;
    public static final int XMYM = 9;
    public static final int YPZP = 10;
    public static final int YPZM = 11;
    public static final int YMZP = 12;
    public static final int YMZM = 13;
    public static final int ZPXP = 14;
    public static final int ZPXM = 15;
    public static final int ZMXP = 16;
    public static final int ZMXM = 17;
    
    public static final int SQUARE = 0;
    public static final int TRI1 = 1;
    public static final int TRI2 = 2;
    public static final int TRI3 = 3;
    public static final int TRI4 = 4;
    public static final int RECTANGLE = 5;

    private int             mType;
    private int             mNormal;
    private Block           mBlock;
    private Point3i			mPosition;
    private Point3i[]       mModelPoints;

    public Block getBlock()
    {
        return mBlock;
    }

    public void setBlock(Block block)
    {
        mBlock = block;
    }

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    public Point3i[] getModelPoints()
    {
        return mModelPoints;
    }

    public void setModelPoints(Point3i[] modelPoints)
    {
        mModelPoints = modelPoints;
    }

    public int getNormal()
    {
        return mNormal;
    }

    public void setNormal(int normal)
    {
        mNormal = normal;
    }

	public Point3i getPosition()
	{
		return mPosition;
	}

	public void setPosition(Point3i position)
	{
		mPosition = position;
	}
}

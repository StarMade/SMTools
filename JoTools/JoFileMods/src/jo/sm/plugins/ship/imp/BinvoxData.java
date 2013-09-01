package jo.sm.plugins.ship.imp;

public class BinvoxData
{
    private byte[] mVoxels;
    private int    mDepth, mHeight, mWidth;
    private int    mSize;
    private double mTX, mTY, mTZ;
    private double mScale;
    public byte[] getVoxels()
    {
        return mVoxels;
    }
    public void setVoxels(byte[] voxels)
    {
        mVoxels = voxels;
    }
    public int getDepth()
    {
        return mDepth;
    }
    public void setDepth(int depth)
    {
        mDepth = depth;
    }
    public int getHeight()
    {
        return mHeight;
    }
    public void setHeight(int height)
    {
        mHeight = height;
    }
    public int getWidth()
    {
        return mWidth;
    }
    public void setWidth(int width)
    {
        mWidth = width;
    }
    public int getSize()
    {
        return mSize;
    }
    public void setSize(int size)
    {
        mSize = size;
    }
    public double getTX()
    {
        return mTX;
    }
    public void setTX(double tX)
    {
        mTX = tX;
    }
    public double getTY()
    {
        return mTY;
    }
    public void setTY(double tY)
    {
        mTY = tY;
    }
    public double getTZ()
    {
        return mTZ;
    }
    public void setTZ(double tZ)
    {
        mTZ = tZ;
    }
    public double getScale()
    {
        return mScale;
    }
    public void setScale(double scale)
    {
        mScale = scale;
    }

}

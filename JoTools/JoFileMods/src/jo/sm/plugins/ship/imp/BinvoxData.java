package jo.sm.plugins.ship.imp;

import java.io.DataInputStream;


public class BinvoxData
{
    private boolean[][][]  mVoxels;
    private int    mZSpan;
    private int    mXSpan; 
    private int    mYSpan;
    private int    mSize;
    private double mTX, mTY, mTZ;
    private double mScale;
    private DataInputStream mInput;
    private boolean         mDone;
    private int             mEndIndex;

    public int getZSpan()
    {
        return mZSpan;
    }
    public void setZSpan(int depth)
    {
        mZSpan = depth;
    }
    public int getXSpan()
    {
        return mXSpan;
    }
    public void setXSPan(int height)
    {
        mXSpan = height;
    }
    public int getYSpan()
    {
        return mYSpan;
    }
    public void setYSpan(int width)
    {
        mYSpan = width;
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
    public boolean[][][] getVoxels()
    {
        return mVoxels;
    }
    public void setVoxels(boolean[][][] voxels)
    {
        mVoxels = voxels;
    }
    public DataInputStream getInput()
    {
        return mInput;
    }
    public void setInput(DataInputStream input)
    {
        mInput = input;
    }
    public boolean isDone()
    {
        return mDone;
    }
    public void setDone(boolean done)
    {
        mDone = done;
    }
    public int getEndIndex()
    {
        return mEndIndex;
    }
    public void setEndIndex(int endIndex)
    {
        mEndIndex = endIndex;
    }

}

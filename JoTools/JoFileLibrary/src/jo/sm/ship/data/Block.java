package jo.sm.ship.data;

import jo.sm.ui.BlockTypeColors;

public class Block
{
    private short   mBlockID;
    private boolean mActive;
    private short   mHitPoints;
    private short   mOrientation;
    
    public Block()
    {
    	mHitPoints = 100;
    }
    
    public Block(short id)
    {
        this();
        setBlockID(id);
    }
    
    public Block(Block b)
    {
        mBlockID = b.mBlockID;
        mActive = b.mActive;
        mHitPoints = b.mHitPoints;
        mOrientation = b.mOrientation;
    }
    
    public short getBlockID()
    {
        return mBlockID;
    }
    public void setBlockID(short blockID)
    {
        mBlockID = blockID;
        if (BlockTypeColors.BLOCK_HITPOINTS.containsKey(mBlockID))
            mHitPoints = BlockTypeColors.BLOCK_HITPOINTS.get(mBlockID);
        else
            mHitPoints = 100;
    }
    public boolean isActive()
    {
        return mActive;
    }
    public void setActive(boolean active)
    {
        mActive = active;
    }
    public short getHitPoints()
    {
        return mHitPoints;
    }
    public void setHitPoints(short hitPoints)
    {
        mHitPoints = hitPoints;
    }
    public short getOrientation()
    {
        return mOrientation;
    }
    public void setOrientation(short orientation)
    {
        mOrientation = orientation;
    }
}

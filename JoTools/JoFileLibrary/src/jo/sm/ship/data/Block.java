package jo.sm.ship.data;

import jo.sm.ui.BlockTypeColors;

public class Block
{
    private short   mBlockID;
    private byte    mOrientation;
    
    public Block()
    {
    }
    
    public Block(short id)
    {
        this();
        setBlockID(id);
    }
    
    public Block(Block b)
    {
        mBlockID = b.mBlockID;
        mOrientation = b.mOrientation;
    }
    
    public short getBlockID()
    {
        return mBlockID;
    }
    public void setBlockID(short blockID)
    {
        mBlockID = blockID;
    }
    public boolean isActive()
    {
        return false;
    }
    public void setActive(boolean active)
    {
    }
    public short getHitPoints()
    {
        if (BlockTypeColors.BLOCK_HITPOINTS.containsKey(mBlockID))
            return BlockTypeColors.BLOCK_HITPOINTS.get(mBlockID);
        else
        	return 100;
    }
    public void setHitPoints(short hitPoints)
    {
    }
    public short getOrientation()
    {
        return mOrientation;
    }
    public void setOrientation(short orientation)
    {
        mOrientation = (byte)orientation;
    }
}

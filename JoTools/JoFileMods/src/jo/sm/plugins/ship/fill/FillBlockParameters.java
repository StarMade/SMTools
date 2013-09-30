package jo.sm.plugins.ship.fill;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

@Description(displayName="Fill Blocks",shortDescription="Fill selected region/interior of the ship with a specific block.")
public class FillBlockParameters
{
	@Description(displayName="Block", shortDescription="Type of block to fill the region with")
    private short mBlockID;
    @Description(displayName="Quantity", shortDescription="How many blocks to fill (0 for all)")
    private int mTotal;
	@Description(displayName="Strategy", shortDescription="Approach to deciding which area to fill first")
    private int mStrategy;
	@Description(displayName="Axis", shortDescription="Which access to apply the strategy along")
    private int mAxis;
    
    public FillBlockParameters()
    {
        mBlockID = BlockTypes.THRUSTER_ID;
        mTotal = 200;
        mStrategy = FillStrategy.CENTER;
        mAxis = FillStrategy.Z;
    }

    public short getBlockID()
    {
        return mBlockID;
    }

    public void setBlockID(short blockID)
    {
        mBlockID = blockID;
    }

    public int getTotal()
    {
        return mTotal;
    }

    public void setTotal(int total)
    {
        mTotal = total;
    }

    public int getStrategy()
    {
        return mStrategy;
    }

    public void setStrategy(int strategy)
    {
        mStrategy = strategy;
    }

    public int getAxis()
    {
        return mAxis;
    }

    public void setAxis(int axis)
    {
        mAxis = axis;
    }
}

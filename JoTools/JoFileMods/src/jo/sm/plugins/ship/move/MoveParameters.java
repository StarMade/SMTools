package jo.sm.plugins.ship.move;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Move Ship's Core")
public class MoveParameters
{
	@Description(displayName="Starboard/Port", shortDescription="How far along X axis")
    private int     mXMove;
	@Description(displayName="Dorsal/Ventral", shortDescription="How far along Y axis")
    private int     mYMove;
	@Description(displayName="Fore/Aft", shortDescription="How far along Z axis")
    private int     mZMove;
    
    public MoveParameters()
    {
        mXMove = 0;
        mYMove = 0;
        mZMove = 0;
    }

    public int getXMove()
    {
        return mXMove;
    }

    public void setXMove(int xMove)
    {
        mXMove = xMove;
    }

    public int getYMove()
    {
        return mYMove;
    }

    public void setYMove(int yMove)
    {
        mYMove = yMove;
    }

    public int getZMove()
    {
        return mZMove;
    }

    public void setZMove(int zMove)
    {
        mZMove = zMove;
    }

}

package jo.sm.plugins.ship.move;


public class MoveParameters
{
    private int     mXMove;
    private int     mYMove;
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

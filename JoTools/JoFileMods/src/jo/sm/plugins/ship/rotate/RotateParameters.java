package jo.sm.plugins.ship.rotate;


public class RotateParameters
{
    private int     mXRotate;
    private int     mYRotate;
    private int     mZRotate;
    
    public RotateParameters()
    {
        mXRotate = 0;
        mYRotate = 0;
        mZRotate = 0;
    }

    public int getXRotate()
    {
        return mXRotate;
    }

    public void setXRotate(int xRotate)
    {
        mXRotate = xRotate;
    }

    public int getYRotate()
    {
        return mYRotate;
    }

    public void setYRotate(int yRotate)
    {
        mYRotate = yRotate;
    }

    public int getZRotate()
    {
        return mZRotate;
    }

    public void setZRotate(int zRotate)
    {
        mZRotate = zRotate;
    }

}

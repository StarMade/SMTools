package jo.sm.plugins.ship.rotate;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Rotate ship around core")
public class RotateParameters
{
	@Description(displayName="Pitch", shortDescription="Around X axis")
    private int     mXRotate;
	@Description(displayName="Yaw", shortDescription="Around Y axis")
    private int     mYRotate;
	@Description(displayName="Roll", shortDescription="Around Z axis")
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

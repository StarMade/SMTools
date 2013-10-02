package jo.sm.plugins.ship.stripes;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

@Description(displayName="Paint With Pattern", shortDescription="Paint a pattern of stripes or checks on object")
public class StripesParameters
{
	@Description(displayName="First Color")
    private short   mColor1;
	@Description(displayName="Second Color")
    private short   mColor2;
	@Description(displayName="Port to Starboard")
    private boolean mXAxis;
	@Description(displayName="First", shortDescription="Width of first color stripe")
    private int     mXWidth1;
	@Description(displayName="Second", shortDescription="Width of second color stripe")
    private int     mXWidth2;
	@Description(displayName="Dorsal to Ventral")
    private boolean mYAxis;
	@Description(displayName="First", shortDescription="Width of first color stripe")
    private int     mYWidth1;
	@Description(displayName="Second", shortDescription="Width of second color stripe")
    private int     mYWidth2;
	@Description(displayName="Fore to Aft")
    private boolean mZAxis;
	@Description(displayName="First", shortDescription="Width of first color stripe")
    private int     mZWidth1;
	@Description(displayName="Second", shortDescription="Width of second color stripe")
    private int     mZWidth2;
    
    public StripesParameters()
    {
        mColor1 = BlockTypes.HULL_COLOR_BLACK_ID;
        mColor2 = BlockTypes.HULL_COLOR_WHITE_ID;
        mXAxis = true;
        mXWidth1 = 1;
        mXWidth2 = 1;
        mYAxis = false;
        mYWidth1 = 1;
        mYWidth2 = 1;
        mZAxis = false;
        mZWidth1 = 1;
        mZWidth2 = 1;
    }
    
    public short getColor1()
    {
        return mColor1;
    }
    public void setColor1(short color1)
    {
        mColor1 = color1;
    }
    public short getColor2()
    {
        return mColor2;
    }
    public void setColor2(short color2)
    {
        mColor2 = color2;
    }
    public boolean isXAxis()
    {
        return mXAxis;
    }
    public void setXAxis(boolean xAxis)
    {
        mXAxis = xAxis;
    }
    public int getXWidth1()
    {
        return mXWidth1;
    }
    public void setXWidth1(int xWidth1)
    {
        mXWidth1 = xWidth1;
    }
    public int getXWidth2()
    {
        return mXWidth2;
    }
    public void setXWidth2(int xWidth2)
    {
        mXWidth2 = xWidth2;
    }
    public boolean isYAxis()
    {
        return mYAxis;
    }
    public void setYAxis(boolean yAxis)
    {
        mYAxis = yAxis;
    }
    public int getYWidth1()
    {
        return mYWidth1;
    }
    public void setYWidth1(int yWidth1)
    {
        mYWidth1 = yWidth1;
    }
    public int getYWidth2()
    {
        return mYWidth2;
    }
    public void setYWidth2(int yWidth2)
    {
        mYWidth2 = yWidth2;
    }
    public boolean isZAxis()
    {
        return mZAxis;
    }
    public void setZAxis(boolean zAxis)
    {
        mZAxis = zAxis;
    }
    public int getZWidth1()
    {
        return mZWidth1;
    }
    public void setZWidth1(int zWidth1)
    {
        mZWidth1 = zWidth1;
    }
    public int getZWidth2()
    {
        return mZWidth2;
    }
    public void setZWidth2(int zWidth2)
    {
        mZWidth2 = zWidth2;
    }
}

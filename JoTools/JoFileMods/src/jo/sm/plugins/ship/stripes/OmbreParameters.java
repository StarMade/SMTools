package jo.sm.plugins.ship.stripes;

import jo.sm.data.BlockTypes;
import jo.sm.ui.act.plugin.Description;

@Description(displayName="Paint With Onbré Pattern", shortDescription="Paint a pattern fading from one color to another.")
public class OmbreParameters
{
	@Description(displayName="First Color")
    private short   mColor1;
	@Description(displayName="Second Color")
    private short   mColor2;
	@Description(displayName="Left to Right")
    private boolean mXAxis;
	@Description(displayName="Top to Bottom")
    private boolean mYAxis;
	@Description(displayName="Fore to Aft")
    private boolean mZAxis;
    
    public OmbreParameters()
    {
        mColor1 = BlockTypes.HULL_COLOR_BLACK_ID;
        mColor2 = BlockTypes.HULL_COLOR_WHITE_ID;
        mXAxis = true;
        mYAxis = false;
        mZAxis = false;
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
    public boolean isYAxis()
    {
        return mYAxis;
    }
    public void setYAxis(boolean yAxis)
    {
        mYAxis = yAxis;
    }
    public boolean isZAxis()
    {
        return mZAxis;
    }
    public void setZAxis(boolean zAxis)
    {
        mZAxis = zAxis;
    }
}

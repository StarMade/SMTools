package jo.sm.plugins.ship.replace;

import jo.sm.data.BlockTypes;

public class ReplaceParameters
{
    private short   mColor1;
    private short   mColor2;
    
    public ReplaceParameters()
    {
        mColor1 = BlockTypes.HULL_COLOR_BLACK_ID;
        mColor2 = BlockTypes.HULL_COLOR_WHITE_ID;
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
}

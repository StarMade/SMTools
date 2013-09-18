package jo.sm.plugins.ship.edit;

import jo.sm.ship.logic.SmoothLogic;
import jo.sm.ui.act.plugin.Description;

@Description(displayName="Smooth Corners", shortDescription="Add wedges and corners to rough edges.")
public class SmoothParameters
{
    @Description(displayName="Scope", shortDescription="What areas to smooth")
    private int  mScope;
    @Description(displayName="Type", shortDescription="What to smooth with")
    private int  mType;
    
    public SmoothParameters()
    {
        mScope = SmoothLogic.EXTERIOR;
        mType = SmoothLogic.EVERYTHING;
    }

    public int getScope()
    {
        return mScope;
    }

    public void setScope(int scope)
    {
        mScope = scope;
    }

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }
}

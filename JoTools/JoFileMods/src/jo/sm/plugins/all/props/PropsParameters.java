package jo.sm.plugins.all.props;

import jo.sm.ui.act.plugin.Description;



@Description(displayName="Properties", shortDescription="Properties affecting the whole application.")
public class PropsParameters
{
    @Description(displayName="Invert X", shortDescription="Invert X Axis on mouse")
    private boolean     mInvertXAxis;
    @Description(displayName="Invert Y", shortDescription="Invert Y Axis on mouse")
    private boolean     mInvertYAxis;
    
    public PropsParameters()
    {
    }

	public boolean isInvertXAxis()
	{
		return mInvertXAxis;
	}

	public void setInvertXAxis(boolean invertXAxis)
	{
		mInvertXAxis = invertXAxis;
	}

	public boolean isInvertYAxis()
	{
		return mInvertYAxis;
	}

	public void setInvertYAxis(boolean invertYAxis)
	{
		mInvertYAxis = invertYAxis;
	}

}

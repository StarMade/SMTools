package jo.sm.plugins.planet.select;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Paste from shape library",shortDescription="Select a shape library and paste into selection")
public class SelectPasteFromParameters
{
	@Description(displayName="Shape")
    private int mShape;
    
    public SelectPasteFromParameters()
    {
    }

	public int getShape()
	{
		return mShape;
	}

	public void setShape(int shape)
	{
		mShape = shape;
	}
}

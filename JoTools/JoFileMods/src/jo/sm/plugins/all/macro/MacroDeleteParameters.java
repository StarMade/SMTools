package jo.sm.plugins.all.macro;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Delete Macro", shortDescription="Delete a recorded macro")
public class MacroDeleteParameters
{
    @Description(displayName="Name", shortDescription="Name of macro", priority=1)
    private String  mName;
    
    public MacroDeleteParameters()
    {
    }

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		mName = name;
	}
}

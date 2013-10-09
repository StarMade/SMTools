package jo.sm.plugins.all.macro;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Record Macro", shortDescription="Record actions as a macro")
public class MacroRecordParameters
{
    @Description(displayName="Name", shortDescription="Name of macro", priority=1)
    private String  mName;
    @Description(displayName="Author", shortDescription="Who are you?", priority=2)
    private String  mAuthor;
    @Description(displayName="Placement", shortDescription="Where the macro should appear", priority=3)
    private String	mPlacement;
    @Description(displayName="Enablement", shortDescription="Type of a object it should appear for", priority=4)
    private String	mEnablement;
    @Description(displayName="Priority", shortDescription="How high up or down the menu to appear", priority=5)
    private int	mPriority;
    @Description(displayName="Description", shortDescription="Long description", priority=6)
    private String  mDescription;
    
    public MacroRecordParameters()
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

	public String getAuthor()
	{
		return mAuthor;
	}

	public void setAuthor(String author)
	{
		mAuthor = author;
	}

	public String getPlacement()
	{
		return mPlacement;
	}

	public void setPlacement(String placement)
	{
		mPlacement = placement;
	}

	public String getEnablement()
	{
		return mEnablement;
	}

	public void setEnablement(String enablement)
	{
		mEnablement = enablement;
	}

	public int getPriority()
	{
		return mPriority;
	}

	public void setPriority(int priority)
	{
		mPriority = priority;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public void setDescription(String description)
	{
		mDescription = description;
	}
}

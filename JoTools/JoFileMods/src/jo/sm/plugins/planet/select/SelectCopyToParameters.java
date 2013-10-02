package jo.sm.plugins.planet.select;

import jo.sm.mods.IBlocksPlugin;
import jo.sm.ui.act.plugin.Description;

@Description(displayName="Copy to shape library",shortDescription="Save the selection into a slot in the shape library")
public class SelectCopyToParameters
{
	@Description(displayName="Name")
    private String mName;
	@Description(displayName="Author")
    private String mAuthor;
	@Description(displayName="Type")
    private int mType;
    
    public SelectCopyToParameters()
    {
        mName = "";
        mAuthor = "";
        mType = IBlocksPlugin.TYPE_ALL;
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

    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }
}

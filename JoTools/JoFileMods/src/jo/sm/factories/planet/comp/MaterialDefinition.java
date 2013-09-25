package jo.sm.factories.planet.comp;

import java.util.ArrayList;
import java.util.List;

public class MaterialDefinition
{
    private String  mAuthor;
    private String  mTitle;
    private String  mDescription;
    private int     mPriority;
    private List<MaterialEntry> mOldBlocks;
    private List<MaterialEntry> mNewBlocks;
    
    public MaterialDefinition()
    {
        mAuthor = "";
        mTitle = "";
        mDescription = "";
        mPriority = 50;
        mOldBlocks = new ArrayList<MaterialEntry>();
        mNewBlocks = new ArrayList<MaterialEntry>();
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public void setAuthor(String author)
    {
        mAuthor = author;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    public List<MaterialEntry> getOldBlocks()
    {
        return mOldBlocks;
    }

    public void setOldBlocks(List<MaterialEntry> oldBlocks)
    {
        mOldBlocks = oldBlocks;
    }

    public List<MaterialEntry> getNewBlocks()
    {
        return mNewBlocks;
    }

    public void setNewBlocks(List<MaterialEntry> newBlocks)
    {
        mNewBlocks = newBlocks;
    }

    public int getPriority()
    {
        return mPriority;
    }

    public void setPriority(int priority)
    {
        mPriority = priority;
    }
}

package jo.sm.factories.all.macro;

import java.io.File;

public class MacroDefinition
{
    private String  mAuthor;
    private String  mTitle;
    private String  mDescription;
    private int     mPriority;
    private File    mScript;
    private int[][] mClassifications;
    
    public MacroDefinition()
    {
        mAuthor = "";
        mTitle = "";
        mDescription = "";
        mPriority = 50;
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

    public int getPriority()
    {
        return mPriority;
    }

    public void setPriority(int priority)
    {
        mPriority = priority;
    }

    public File getScript()
    {
        return mScript;
    }

    public void setScript(File script)
    {
        mScript = script;
    }

    public int[][] getClassifications()
    {
        return mClassifications;
    }

    public void setClassifications(int[][] classifications)
    {
        mClassifications = classifications;
    }
}

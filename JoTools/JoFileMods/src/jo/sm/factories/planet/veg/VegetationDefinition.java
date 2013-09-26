package jo.sm.factories.planet.veg;

import java.util.ArrayList;
import java.util.List;

public class VegetationDefinition
{
    private String  mAuthor;
    private String  mTitle;
    private String  mDescription;
    private int     mPriority;
    private float   mDensity;
    private List<VegetationEntry> mVegetation;
    
    public VegetationDefinition()
    {
        mAuthor = "";
        mTitle = "";
        mDescription = "";
        mPriority = 50;
        mDensity = 1/1000f;
        mVegetation = new ArrayList<VegetationEntry>();
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

    public float getDensity()
    {
        return mDensity;
    }

    public void setDensity(float density)
    {
        mDensity = density;
    }

    public List<VegetationEntry> getVegetation()
    {
        return mVegetation;
    }

    public void setVegetation(List<VegetationEntry> vegetation)
    {
        mVegetation = vegetation;
    }
}

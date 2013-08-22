package jo.sm.ui.logic;

import java.io.File;

import jo.sm.data.Entity;

public class ShipSpec
{
    public static final int BLUEPRINT = 0;
    public static final int DEFAULT_BLUEPRINT = 1;
    public static final int ENTITY = 2;
    public static final int FILE = 3;
    
    private int mClassification;
    private int mType;
    private String mName;
    private Entity mEntity;
    private File mFile;
    
    public String toString()
    {
        return mName;
    }
    
    public int getType()
    {
        return mType;
    }
    public void setType(int type)
    {
        mType = type;
    }
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }

    public Entity getEntity()
    {
        return mEntity;
    }

    public void setEntity(Entity entity)
    {
        mEntity = entity;
    }

    public File getFile()
    {
        return mFile;
    }

    public void setFile(File file)
    {
        mFile = file;
    }

    public int getClassification()
    {
        return mClassification;
    }

    public void setClassification(int classification)
    {
        mClassification = classification;
    }
}

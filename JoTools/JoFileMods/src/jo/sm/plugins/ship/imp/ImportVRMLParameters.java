package jo.sm.plugins.ship.imp;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Import VRML Model", shortDescription="Import a model in VRML format from your disk")
public class ImportVRMLParameters
{
	@Description(displayName="File", shortDescription="Path to VRML model")
    private String  mFile;
	@Description(displayName="Longest Dimension", shortDescription="Scale the model to this size")
    private int   mLongestDimension;
    
    public ImportVRMLParameters()
    {
        mLongestDimension = 100;
    }

    public String getFile()
    {
        return mFile;
    }

    public void setFile(String file)
    {
        mFile = file;
    }

    public int getLongestDimension()
    {
        return mLongestDimension;
    }

    public void setLongestDimension(int longestDimension)
    {
        mLongestDimension = longestDimension;
    }
}

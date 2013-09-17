package jo.sm.plugins.ship.exp;

import jo.sm.ui.act.plugin.Description;



@Description(displayName="Export Images of Object to Disk", shortDescription="This writes fore, aft, dorsal, ventral, port, starboard, and an isometric view"
        + " of the object, plus a contact sheet with everything on it to the given directory.")
public class ExportImagesParameters
{
    @Description(displayName="", shortDescription="Directory to place images")
    private String  mDirectory;
    @Description(displayName="", shortDescription="Prefix to use for image name")
    private String  mName;
    @Description(displayName="", shortDescription="Width in pixels")
    private int     mWidth;
    @Description(displayName="", shortDescription="Height in pixels")
    private int     mHeight;
    
    public ExportImagesParameters()
    {
        mDirectory = System.getProperty("user.home");
        mWidth = 1024;
        mHeight = 768;
    }

	public String getDirectory()
	{
		return mDirectory;
	}

	public void setDirectory(String directory)
	{
		mDirectory = directory;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		mName = name;
	}

	public int getWidth()
	{
		return mWidth;
	}

	public void setWidth(int width)
	{
		mWidth = width;
	}

	public int getHeight()
	{
		return mHeight;
	}

	public void setHeight(int height)
	{
		mHeight = height;
	}
}

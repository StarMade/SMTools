package jo.sm.plugins.ship.exp;




public class ExportImagesParameters
{
    private String  mDirectory;
    private String  mName;
    private int     mWidth;
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

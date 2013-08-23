package jo.sm.plugins.ship.imp;


public class ImportParameters
{
    private String  mFile;
//    private float   mCenterX;
//    private float   mCenterY;
//    private float   mCenterZ;
    private int   mLongestDimension;
    
    public ImportParameters()
    {
//        mCenterX = 0.01f;
//        mCenterY = 0.01f;
//        mCenterZ = 0.01f;
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
//
//    public float getCenterX()
//    {
//        return mCenterX;
//    }
//
//    public void setCenterX(float centerX)
//    {
//        mCenterX = centerX;
//    }
//
//    public float getCenterY()
//    {
//        return mCenterY;
//    }
//
//    public void setCenterY(float centerY)
//    {
//        mCenterY = centerY;
//    }
//
//    public float getCenterZ()
//    {
//        return mCenterZ;
//    }
//
//    public void setCenterZ(float centerZ)
//    {
//        mCenterZ = centerZ;
//    }

    public int getLongestDimension()
    {
        return mLongestDimension;
    }

    public void setLongestDimension(int longestDimension)
    {
        mLongestDimension = longestDimension;
    }
}

package jo.sm.plugins.ship.text;

import jo.sm.data.BlockTypes;

public class TextParameters
{
    private short   mInk;
    private String  mFont;
    private boolean mBold;
    private boolean mItalic;
    private int     mSize;
    private String  mText;
    
    public TextParameters()
    {
        mInk = BlockTypes.HULL_COLOR_BLACK_ID;
        mFont = "Dialog";
        mText = "Jo is Awesome";
        mBold = false;
        mItalic = false;
        mSize = 10;
    }

    public short getInk()
    {
        return mInk;
    }

    public void setInk(short ink)
    {
        mInk = ink;
    }

    public String getText()
    {
        return mText;
    }

    public void setText(String text)
    {
        mText = text;
    }

    public String getFont()
    {
        return mFont;
    }

    public void setFont(String font)
    {
        mFont = font;
    }

    public boolean isBold()
    {
        return mBold;
    }

    public void setBold(boolean bold)
    {
        mBold = bold;
    }

    public boolean isItalic()
    {
        return mItalic;
    }

    public void setItalic(boolean italic)
    {
        mItalic = italic;
    }

    public int getSize()
    {
        return mSize;
    }

    public void setSize(int size)
    {
        mSize = size;
    }
}

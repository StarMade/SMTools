package jo.sm.plugins.ship.text;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class ImagePlugin implements IBlocksPlugin
{
    public static final String NAME = "Image";
    public static final String DESC = "Paint image on the hull.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_PAINT },
        { TYPE_STATION, SUBTYPE_PAINT },
        { TYPE_SHOP, SUBTYPE_PAINT },
        };
    
    private Point3i mStartingPoint;
    private Point3i mAdvanceVector;
    private int mAdvanceLength;
    private Point3i mHeightVector;
    private int mHeightLength;
    private Point3i mDepthVector;
    private int mDepthLength;

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescription()
    {
        return DESC;
    }

    @Override
    public String getAuthor()
    {
        return AUTH;
    }

    @Override
    public Object newParameterBean()
    {
        return new ImageParameters();
    }
	@Override
	public void initParameterBean(SparseMatrix<Block> original, Object params,
			StarMade sm, IPluginCallback cb)
	{
	}

    @Override
    public int[][] getClassifications()
    {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb)
    {
        ImageParameters params = (ImageParameters)p;
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);
        if (cb != null) cb.setStatus("Adding text");
        setupExtent(lower, upper);
        BufferedImage text = setupImage(params, cb);
        if (text == null)
            return null;
        
        if (cb != null) cb.startTask(mAdvanceLength);
        SparseMatrix<Block> modified = new SparseMatrix<Block>(original);
        Point3i advance = new Point3i(mStartingPoint);
        for (int x = 0; x < mAdvanceLength; x++)
        {
            Point3i height = new Point3i(advance);
            for (int y = 0; y < mHeightLength; y++)
            {
                int rgb = text.getRGB(x, y);
                if ((rgb&0xFF000000) != 0)
                {
                    System.out.print("X");
                    short id = mapColor(rgb);
                    Point3i depth = new Point3i(height);
                    for (int z = 0; z < mDepthLength; z++)
                    {
                        if (modified.contains(depth))
                            modified.set(depth, new Block(id));
                        depth.add(mDepthVector);
                    }
                }
                else
                    System.out.print(" ");
                height.add(mHeightVector);
            }
            System.out.println();
            advance.add(mAdvanceVector);
            if (cb != null) cb.workTask(1);
        }
        if (cb != null) cb.endTask();
        return modified;
    }
    
    private static short HULL_IDS[] = BlockTypes.HULL_COLOR_MAP[0];
    private static int HULL_RGBS[] = {
        0x808080, // HULL_COLOR_GREY_ID,
        0xa020f0, // HULL_COLOR_PURPLE_ID,
        0xa52a2a, // HULL_COLOR_BROWN_ID,
        0x000000, // HULL_COLOR_BLACK_ID,
        0xFF0000, // HULL_COLOR_RED_ID,
        0x0000FF, // HULL_COLOR_BLUE_ID,
        0x00FF00, // HULL_COLOR_GREEN_ID,
        0xFFFF00, // HULL_COLOR_YELLOW_ID,
        0xFFFFFF, // HULL_COLOR_WHITE_ID,
    };
    
    private int distance(int rgb1, int rgb2)
    {
        int delta = 0;
        delta += Math.abs(((rgb1>>0)&0xff) - ((rgb2>>0)&0xff));
        delta += Math.abs(((rgb1>>8)&0xff) - ((rgb2>>8)&0xff));
        delta += Math.abs(((rgb1>>16)&0xff) - ((rgb2>>16)&0xff));
        return delta;
    }

    private short mapColor(int rgb)
    {
        int best = 0;
        int value = distance(rgb, HULL_RGBS[best]);
        for (int i = 1; i < HULL_RGBS.length; i++)
        {
            int v = distance(rgb, HULL_RGBS[i]);
            if (v < value)
            {
                best = i;
                value = v;
            }
        }
        return HULL_IDS[best];
    }

    private BufferedImage setupImage(ImageParameters params, IPluginCallback cb)
    {
        BufferedImage picture = null;
        try
        {
            picture = ImageIO.read(new File(params.getFile()));
            BufferedImage img = new BufferedImage(mAdvanceLength, mHeightLength, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics g = img.getGraphics();
            g.drawImage(picture, 0, 0, mAdvanceLength, mHeightLength, 0, 0, picture.getWidth(), picture.getHeight(), null);
            g.dispose();
            return img;
        }
        catch (IOException e)
        {
            cb.setStatus(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void setupExtent(Point3i lower, Point3i upper)
    {
        int xSpan = upper.x - lower.x + 1;
        int ySpan = upper.y - lower.y + 1;
        int zSpan = upper.z - lower.z + 1;
        mStartingPoint = lower;
        mAdvanceLength = Math.max(xSpan, Math.max(ySpan, zSpan));
        if (mAdvanceLength == xSpan)
        {
            mAdvanceVector = new Point3i(1, 0, 0);
            if (ySpan >= zSpan)
            {
                mHeightVector = new Point3i(0, 1, 0);
                mHeightLength = ySpan;
                mDepthVector = new Point3i(0, 0, 1);
                mDepthLength = zSpan;
            }
            else
            {
                mHeightVector = new Point3i(0, 0, 1);
                mHeightLength = zSpan;
                mDepthVector = new Point3i(0, 1, 0);
                mDepthLength = ySpan;
            }
        }
        else if (mAdvanceLength == ySpan)
        {
            mAdvanceVector = new Point3i(0, 1, 0);
            if (xSpan >= zSpan)
            {
                mHeightVector = new Point3i(1, 0, 0);
                mHeightLength = xSpan;
                mDepthVector = new Point3i(0, 0, 1);
                mDepthLength = zSpan;
            }
            else
            {
                mHeightVector = new Point3i(0, 0, 1);
                mHeightLength = zSpan;
                mDepthVector = new Point3i(1, 0, 0);
                mDepthLength = xSpan;
            }
        }
        else if (mAdvanceLength == zSpan)
        {
            mAdvanceVector = new Point3i(0, 0, 1);
            if (xSpan >= ySpan)
            {
                mHeightVector = new Point3i(1, 0, 0);
                mHeightLength = xSpan;
                mDepthVector = new Point3i(0, 1, 0);
                mDepthLength = ySpan;
            }
            else
            {
                mHeightVector = new Point3i(0, 1, 0);
                mHeightLength = ySpan;
                mDepthVector = new Point3i(1, 0, 0);
                mDepthLength = xSpan;
            }
        }
        System.out.println("Advance="+mAdvanceVector+" x "+mAdvanceLength);
        System.out.println("Height="+mHeightVector+" x "+mHeightLength);
        System.out.println("Depth="+mDepthVector+" x "+mDepthLength);
    }
}

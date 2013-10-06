package jo.sm.plugins.ship.imp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ResourceUtils;
import jo.sm.logic.utils.ShortUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ui.BlockTypeColors;
import jo.vecmath.Color3f;
import jo.vecmath.Point2f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Point2fLogic;
import jo.vecmath.logic.Point3fLogic;
import jo.vecmath.logic.Point3iLogic;
import jo.vecmath.logic.ext.Hull3fLogic;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class PlotLogic
{

    public static void mapHull(SparseMatrix<Block> modified, Hull3f hull,
            float scale, Point3i lowerGrid, Point3i upperGrid, IPluginCallback cb)
    {
        Point3i center = Point3iLogic.interpolate(lowerGrid, upperGrid, .5f);
        cb.startTask(hull.getTriangles().size());
        for (Triangle3f t : hull.getTriangles())
        {
            Point3i iA = mapPoint(t.getA(), scale, center);
            Point3i iB = mapPoint(t.getB(), scale, center);
            Point3i iC = mapPoint(t.getC(), scale, center);
            if ((t.getAUV() != null) && (t.getBUV() != null) && (t.getCUV() != null) && (t.getTexture() != null))
            {
            	drawTriangle(modified, iA, iB, iC, t.getAUV(), t.getBUV(), t.getCUV(), t.getTexture());
            }
            else if (t.getColor() != null)
            {
            	short color = mapColor(t.getColor());
            	drawTriangle(modified, iA, iB, iC, color);
            }
            else
            	drawTriangle(modified, iA, iB, iC, BlockTypes.HULL_COLOR_GREY_ID);
            cb.workTask(1);
        }
        ShipLogic.ensureCore(modified);
        cb.endTask();
    }
    
    private static short uvToColor(Point2f uv, BufferedImage img)
    {
    	uv.x -= Math.floor(uv.x);
    	uv.y -= Math.floor(uv.y);
    	int x = (int)MathUtils.interpolate(uv.x, 0, 1, 0, img.getWidth() - 1);
    	int y = (int)MathUtils.interpolate(uv.y, 0, 1, 0, img.getHeight() - 1);
    	int rgb = img.getRGB(x, y);
    	return mapColor(rgb);
    }

	private static Point3i mapPoint(Point3f f, float scale, Point3i center)
    {
        Point3f fA = new Point3f(f);
        fA.scale(scale);
        Point3i iA = new Point3i(fA);
        iA.sub(center);
        iA.x += 8;
        iA.y += 8;
        iA.z += 8;
        return iA;
    }
    
    public static void drawTriangle(SparseMatrix<Block> grid, Point3i a, Point3i b, Point3i c, short color)
    {
        // Brute force and ignorance method
        // Anything smarter seems to leave holes
        doDrawTriangle(grid, a, b, c, color);
        doDrawTriangle(grid, b, c, a, color);
        doDrawTriangle(grid, c, a, b, color);
    }

    private static void doDrawTriangle(SparseMatrix<Block> grid,
            Point3i fulcrum, Point3i target1, Point3i target2, short color)
    {
        List<Point3i> iterpolate = new ArrayList<Point3i>();
        plotLine(target1, target2, iterpolate);
        Set<Point3i> area = new HashSet<Point3i>();
        //System.out.print("  interpolate over");
        for (Point3i i : iterpolate)
        {
            //System.out.print(" "+i);
            plotLine(i, fulcrum, area);
        }
        plotArea(grid, area, color);
    }
    
    public static void drawTriangle(SparseMatrix<Block> grid, Point3i a, Point3i b, Point3i c, Point2f auv, Point2f buv, Point2f cuv, BufferedImage img)
    {
        // Brute force and ignorance method
        // Anything smarter seems to leave holes
        doDrawTriangle(grid, a, b, c, auv, buv, cuv, img);
        doDrawTriangle(grid, b, c, a, buv, cuv, auv, img);
        doDrawTriangle(grid, c, a, b, cuv, auv, buv, img);
    }

    private static void doDrawTriangle(SparseMatrix<Block> grid,
            Point3i fulcrum, Point3i target1, Point3i target2, 
            Point2f fulcrumUV, Point2f target1UV, Point2f target2UV,
            BufferedImage img)
    {
        List<Point3i> iterpolate = new ArrayList<Point3i>();
        plotLine(target1, target2, iterpolate);
        for (int i = 0; i < iterpolate.size(); i++)
        {
        	Point3i p = iterpolate.get(i);
        	Point2f puv = Point2fLogic.interpolate(target1UV, target2UV, i/(float)(iterpolate.size() - 1));
            drawLine(grid, p, fulcrum, puv, fulcrumUV, img);
        }
    }

    private static void plotArea(SparseMatrix<Block> grid, Collection<Point3i> area,
            short color)
    {
        for (Point3i p : area)
        {
            grid.set(p, new Block(color));
        }
    }
    
    public static void drawLine(SparseMatrix<Block> grid, Point3i a, Point3i b, short color)
    {
        List<Point3i> plot = new ArrayList<Point3i>();
        plotLine(a, b, plot);
        plotArea(grid, plot, color);
    }
    
    public static void drawLine(SparseMatrix<Block> grid, Point3i a, Point3i b, Point2f auv, Point2f buv, BufferedImage img)
    {
        List<Point3i> plot = new ArrayList<Point3i>();
        plotLine(a, b, plot);
        for (int i = 0; i < plot.size(); i++)
        {
        	Point3i p = plot.get(i);
        	Point2f uv = Point2fLogic.interpolate(auv, buv, i/(float)(plot.size() - 1));
        	short color = uvToColor(uv, img);
            grid.set(p, new Block(color));
        }
    }
    
    private static void plotLine(Point3i a, Point3i b, Collection<Point3i> plot)
    {
        Point3f vector = new Point3f(b);
        Point3f p = new Point3f(a);
        vector.sub(p);
        float mag = Point3fLogic.mag(vector);
        int steps = (int)mag;
        vector.scale(1/mag);
        Point3i last = null;
        for (int i = 0; i < steps; i++)
        {
            last = new Point3i(p);
            plot.add(last);
            p.add(vector);
        }
        if (!b.equals(last))
            plot.add(new Point3i(b));
    }

    public static float getScale(Hull3f hull, int longestDimension, Point3i lowerGrid, Point3i upperGrid, Point3i offset)
    {
        Point3f lowerModel = new Point3f();
        Point3f upperModel = new Point3f();
        Hull3fLogic.getBounds(hull, lowerModel, upperModel);
        System.out.println("  Model Bounds:"+lowerModel+" -- "+upperModel);
        float longestModel = Math.max(upperModel.x - lowerModel.x, Math.max(upperModel.y - lowerModel.y, upperModel.z - lowerModel.z));
        float scale = longestDimension/longestModel;
        System.out.println("  Scale:"+scale+" ("+longestDimension+"/"+longestModel);
        lowerGrid.x = (int)Math.floor(lowerModel.x*scale);
        lowerGrid.y = (int)Math.floor(lowerModel.y*scale);
        lowerGrid.z = (int)Math.floor(lowerModel.z*scale);
        upperGrid.x = (int)Math.ceil(upperModel.x*scale);
        upperGrid.y = (int)Math.ceil(upperModel.y*scale);
        upperGrid.z = (int)Math.ceil(upperModel.z*scale);
        System.out.println("  Grid Bounds:"+lowerGrid+" -- "+upperGrid);
        return scale;
    }
    
    private static short HULL_IDS[] = null;
    private static int HULL_RGBS[] = null;
    private static long mLastRead = 0;
    
    private static int distance(int rgb1, int rgb2)
    {
        int delta = 0;
        delta += Math.abs(((rgb1>>0)&0xff) - ((rgb2>>0)&0xff));
        delta += Math.abs(((rgb1>>8)&0xff) - ((rgb2>>8)&0xff));
        delta += Math.abs(((rgb1>>16)&0xff) - ((rgb2>>16)&0xff));
        return delta;
    }
    
    private static void loadColors()
    {
    	File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
    	File colorMap = new File(jo_plugins, "color_map.xml");
    	if (colorMap.exists())
    	{
    		if (colorMap.lastModified() <= mLastRead)
    			return;
    		try
			{
				readColorFile(new FileInputStream(colorMap));
	    		mLastRead = colorMap.lastModified();
	    		return;
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
    	}
    	if (HULL_IDS == null)
    		readColorFile(ResourceUtils.loadSystemResourceStream("color_map.xml", PlotLogic.class));
    }
    
    private static void readColorFile(InputStream is)
    {
    	Document doc = XMLUtils.readStream(is);
    	if (doc == null)
    		return;
    	List<Short> hullIDs = new ArrayList<Short>();
    	List<Integer> hullRGBs = new ArrayList<Integer>();
    	for (Node n : XMLUtils.findNodes(doc, "colors/color"))
    	{
    		int rgb = Integer.parseInt(XMLUtils.getAttribute(n, "rgb"), 16);
    		short block;
    		String smBlock = XMLUtils.getAttribute(n, "block");
    		if (BlockTypeColors.mBlockTypes.containsKey(smBlock))
    			block = ShortUtils.parseShort(BlockTypeColors.mBlockTypes.getProperty(smBlock));
    		else
    			block = ShortUtils.parseShort(smBlock);
    		hullIDs.add(block);
    		hullRGBs.add(rgb);
    	}
    	HULL_IDS = ShortUtils.toShortArray(hullIDs.toArray());
    	HULL_RGBS = IntegerUtils.toArray(hullRGBs.toArray());
    }

    public static short mapColor(int rgb)
    {
    	loadColors();
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

    public static short mapColor(Color3f c)
    {
    	int r = (int)(c.x*255);
    	int g = (int)(c.y*255);
    	int b = (int)(c.z*255);
    	int rgb = (r<<16)|(g<<8)|(b<<0);
    	short color = mapColor(rgb);
    	//System.out.println(c+" -> "+r+","+g+","+b+" -> "+Integer.toHexString(rgb)+" -> "+color);
    	return color;
    }
}

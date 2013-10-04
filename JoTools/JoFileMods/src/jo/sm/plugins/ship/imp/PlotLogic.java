package jo.sm.plugins.ship.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Color3f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.Point3fLogic;
import jo.vecmath.logic.Point3iLogic;
import jo.vecmath.logic.ext.Hull3fLogic;

public class PlotLogic
{

    public static void mapHull(SparseMatrix<Block> modified, Hull3f hull,
            float scale, Point3i lowerGrid, Point3i upperGrid, IPluginCallback cb)
    {
        Point3i center = Point3iLogic.interpolate(lowerGrid, upperGrid, .5f);
        cb.startTask(hull.getTriangles().size());
        short color = BlockTypes.HULL_COLOR_GREY_ID;
        for (Triangle3f t : hull.getTriangles())
        {
            Point3i iA = mapPoint(t.getA(), scale, center);
            Point3i iB = mapPoint(t.getB(), scale, center);
            Point3i iC = mapPoint(t.getC(), scale, center);
            if (t.getColor() != null)
            	color = mapColor(t.getColor());
            drawTriangle(modified, iA, iB, iC, color);
            cb.workTask(1);
        }
        modified.set(8, 8, 8, new Block(BlockTypes.CORE_ID));
        cb.endTask();
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
        //System.out.println("Drawing "+a+" "+b+" "+c);
        // find shortest line
        Point3i fulcrum;
        Point3i target1;
        Point3i target2;
        int abDist = dist2(a, b);
        int bcDist = dist2(b, c);
        int caDist = dist2(c, a);
        //System.out.println("  abDist="+abDist);
        //System.out.println("  bcDist="+bcDist);
        //System.out.println("  caDist="+caDist);
        if (abDist < bcDist)
            if (abDist < caDist)
            {
                fulcrum = c;
                target1 = a;
                target2 = b;
            }
            else
            {
                fulcrum = b;
                target1 = c;
                target2 = a;
            }
        else
            if (bcDist < caDist)
            {
                fulcrum = a;
                target1 = b;
                target2 = c;
            }
            else
            {
                fulcrum = b;
                target1 = c;
                target2 = a;
            }
        //System.out.println("  fulcrum="+fulcrum);
        //System.out.println("  target1="+target1);
        //System.out.println("  target2="+target2);
        List<Point3i> iterpolate = new ArrayList<Point3i>();
        plotLine(target1, target2, iterpolate);
        Set<Point3i> area = new HashSet<Point3i>();
        //System.out.print("  interpolate over");
        for (Point3i i : iterpolate)
        {
            //System.out.print(" "+i);
            plotLine(i, fulcrum, area);
        }
        //System.out.println();
        //System.out.println("  area="+area.size());
        //System.out.print("  ");
        for (Point3i p : area)
        {
            grid.set(p, new Block(color));
            //System.out.print(p+" ");
        }
        //System.out.println();
    }
    
//    private void drawLine(SparseMatrix<Block> grid, Point3i a, Point3i b)
//    {
//        List<Point3i> plot = new ArrayList<Point3i>();
//        plotLine(a, b, plot);
//        for (Point3i p : plot)
//            grid.set(p, new Block(BlockTypes.HULL_COLOR_GREY_ID));
//    }
    
    private static void plotLine(Point3i a, Point3i b, Collection<Point3i> plot)
    {
        Point3f vector = new Point3f(b);
        Point3f p = new Point3f(a);
        vector.sub(p);
        float mag = Point3fLogic.mag(vector);
        int steps = (int)mag;
        vector.scale(1/mag);
        for (int i = 0; i < steps; i++)
        {
            plot.add(new Point3i(p));
            p.add(vector);
        }
    }

    private static int dist2(Point3i p1, Point3i p2)
    {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        int dz = p1.z - p2.z;
        return dx*dx + dy*dy + dz*dz;
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
    
    private static int distance(int rgb1, int rgb2)
    {
        int delta = 0;
        delta += Math.abs(((rgb1>>0)&0xff) - ((rgb2>>0)&0xff));
        delta += Math.abs(((rgb1>>8)&0xff) - ((rgb2>>8)&0xff));
        delta += Math.abs(((rgb1>>16)&0xff) - ((rgb2>>16)&0xff));
        return delta;
    }

    public static short mapColor(int rgb)
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

    public static short mapColor(Color3f c)
    {
    	int r = (int)(c.x*255);
    	int g = (int)(c.y*255);
    	int b = (int)(c.z*255);
    	int rgb = (r<<0)|(g<<8)|(b<<16);
    	short color = mapColor(rgb);
    	//System.out.println(c+" -> "+r+","+g+","+b+" -> "+Integer.toHexString(rgb)+" -> "+color);
    	return color;
    }
}

package jo.sm.plugins.ship.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.Point3fLogic;
import jo.vecmath.logic.Point3iLogic;
import jo.vecmath.logic.ext.Hull3fLogic;
import jo.vecmath.logic.ext.Triangle3fLogic;

public class ImportOBJPlugin implements IBlocksPlugin
{
    public static final String NAME = "Import/OBJ";
    public static final String DESC = "Import OBJ file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_FILE, 25 },
        };

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
        return new ImportOBJParameters();
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
        ImportOBJParameters params = (ImportOBJParameters)p;        
        try
        {
            Hull3f hull = readFile(params.getFile());
            System.out.println("Read "+hull.getTriangles().size()+" triangles");
            Point3i lowerGrid = new Point3i();
            Point3i upperGrid = new Point3i();
            Point3i offset = new Point3i();
            float scale = getScale(hull, params, lowerGrid, upperGrid, offset);
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            mapHull(modified, hull, scale, lowerGrid, upperGrid, params.isForceConvex(), cb);
            return modified;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void mapHull(SparseMatrix<Block> modified, Hull3f hull,
            float scale, Point3i lowerGrid, Point3i upperGrid, boolean forceConvex, IPluginCallback cb)
    {
        Point3i center = Point3iLogic.interpolate(lowerGrid, upperGrid, .5f);
        cb.startTask(hull.getTriangles().size());
        for (Triangle3f t : hull.getTriangles())
        {
            Point3i iA = mapPoint(t.getA(), scale, center);
            Point3i iB = mapPoint(t.getB(), scale, center);
            Point3i iC = mapPoint(t.getC(), scale, center);
            drawTriangle(modified, iA, iB, iC);
            cb.workTask(1);
        }
        modified.set(8, 8, 8, new Block(BlockTypes.CORE_ID));
        cb.endTask();
    }
    
    private Point3i mapPoint(Point3f f, float scale, Point3i center)
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
    
    private void drawTriangle(SparseMatrix<Block> grid, Point3i a, Point3i b, Point3i c)
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
            grid.set(p, new Block(BlockTypes.HULL_COLOR_GREY_ID));
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
    
    private void plotLine(Point3i a, Point3i b, Collection<Point3i> plot)
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

    private int dist2(Point3i p1, Point3i p2)
    {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        int dz = p1.z - p2.z;
        return dx*dx + dy*dy + dz*dz;
    }

    private float getScale(Hull3f hull, ImportOBJParameters params, Point3i lowerGrid, Point3i upperGrid, Point3i offset)
    {
        Point3f lowerModel = new Point3f();
        Point3f upperModel = new Point3f();
        Hull3fLogic.getBounds(hull, lowerModel, upperModel);
        System.out.println("  Model Bounds:"+lowerModel+" -- "+upperModel);
        float longestModel = Math.max(upperModel.x - lowerModel.x, Math.max(upperModel.y - lowerModel.y, upperModel.z - lowerModel.z));
        float scale = params.getLongestDimension()/longestModel;
        System.out.println("  Scale:"+scale+" ("+params.getLongestDimension()+"/"+longestModel);
        lowerGrid.x = (int)Math.floor(lowerModel.x*scale);
        lowerGrid.y = (int)Math.floor(lowerModel.y*scale);
        lowerGrid.z = (int)Math.floor(lowerModel.z*scale);
        upperGrid.x = (int)Math.ceil(upperModel.x*scale);
        upperGrid.y = (int)Math.ceil(upperModel.y*scale);
        upperGrid.z = (int)Math.ceil(upperModel.z*scale);
        System.out.println("  Grid Bounds:"+lowerGrid+" -- "+upperGrid);
        return scale;
    }
    
    private Hull3f readFile(String objFile) throws IOException
    {
        BufferedReader rdr = new BufferedReader(new FileReader(new File(objFile)));
        Hull3f hull = new Hull3f();
        List<Point3f> verts = new ArrayList<Point3f>();
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            if (inbuf.startsWith("v "))
            {   // vertex
                StringTokenizer st = new StringTokenizer(inbuf, " ");
                if (st.countTokens() != 4)
                {
                    System.err.println("Unrecognized vertex line '"+inbuf+"'");
                    break;
                }
                st.nextToken(); // skip label
                Point3f v = new Point3f();
                v.x = Float.parseFloat(st.nextToken());
                v.y = Float.parseFloat(st.nextToken());
                v.z = Float.parseFloat(st.nextToken());
                verts.add(v);
            }
            else if (inbuf.startsWith("f "))
            {   // poly
                List<Point3f> points = new ArrayList<Point3f>();
                StringTokenizer st = new StringTokenizer(inbuf, " ");
                st.nextToken(); // skip label
                while (st.hasMoreTokens())
                {
                    String v = st.nextToken();
                    int o = v.indexOf('/');
                    if (o > 0)
                        v = v.substring(0, o);
                    int idx = Integer.parseInt(v);
                    points.add(verts.get(idx - 1));
                }
                for (int third = 2; third < points.size(); third++)
                {
                    Triangle3f tri = new Triangle3f();
                    tri.setA(points.get(0));
                    tri.setB(points.get(third - 1));
                    tri.setC(points.get(third));
                    if (!Triangle3fLogic.isDegenerate(tri))
                    	hull.getTriangles().add(tri);
                }
            }
        }
        rdr.close();
        return hull;
    }
}

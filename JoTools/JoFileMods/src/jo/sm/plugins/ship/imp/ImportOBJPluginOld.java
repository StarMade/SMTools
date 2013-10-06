package jo.sm.plugins.ship.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.Triangle3f;
import jo.vecmath.logic.ext.Hull3fLogic;
import jo.vecmath.logic.ext.Triangle3fLogic;

public class ImportOBJPluginOld implements IBlocksPlugin
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
            float scale = getScale(hull, params, lowerGrid, upperGrid);
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            mapHull(modified, hull, scale, lowerGrid, upperGrid, false/*params.isForceConvex()*/, cb);
            return modified;
        }
        catch (IOException e)
        {
            cb.setError(e);
            return null;
        }
    }

    private void mapHull(SparseMatrix<Block> modified, Hull3f hull,
            float scale, Point3i lowerGrid, Point3i upperGrid, boolean forceConvex, IPluginCallback cb)
    {
        Point3i center = new Point3i();
        center.x = (lowerGrid.x + upperGrid.x)/2;
        center.y = (lowerGrid.x + upperGrid.x)/2;
        center.z = (lowerGrid.x + upperGrid.x)/2;
        cb.startTask((upperGrid.x - lowerGrid.x + 1)*(upperGrid.y - lowerGrid.y + 1));
        for (int x = lowerGrid.x; x <= upperGrid.x; x++)
        {
            for (int y = lowerGrid.y; y <= upperGrid.y; y++)
            {
                Point3f o = new Point3f(x/scale, y/scale, 0);
                Line3f line = new Line3f(o, new Point3f(0, 0, 1));
                List<Point3f> hits = Hull3fLogic.intersections(hull, line);
                if (hits.size() == 0)
                	continue;
                if (forceConvex)
                {
                	float lowZ = hits.get(0).z;
                	float highZ = lowZ;
                	for (int i = 1; i < hits.size(); i++)
                	{
                		float z = hits.get(i).z;
                		lowZ = Math.min(lowZ, z);
                		highZ = Math.max(highZ, z);
                	}
                	int lowHull = (int)(lowZ*scale);
                	int highHull = (int)(highZ*scale);
                	System.out.println("    x="+x+", y="+y+", z="+lowZ+" -- "+highZ+" out of "+hits.size());
                	for (int z = lowHull; z <= highHull; z++)
    		        {
    		            Block b = new Block();
    		            b.setBlockID(BlockTypes.HULL_COLOR_GREY_ID);
    		            modified.set(x - center.x + 8, y - center.y + 8, z - center.z + 8, b);
    		        }
                }
                else
                	concaveHull(modified, scale, lowerGrid, center, x, y, hits);
                if (cb.isPleaseCancel())
                    break;
                cb.workTask(1);
            }
        }
        ShipLogic.ensureCore(modified);
        cb.endTask();
    }

	private void concaveHull(SparseMatrix<Block> modified, float scale,
			Point3i lowerGrid, Point3i center, int x, int y, List<Point3f> hits)
	{
		Collections.sort(hits, new Comparator<Point3f>() {
		    @Override
		    public int compare(Point3f p1, Point3f p2)
		    {
		        return (int)Math.signum(p1.z - p2.z);
		    }
		});
		boolean inside = false;
		int z = lowerGrid.z;
		for (Point3f hit : hits)
		{
		    int hitz = (int)(hit.z*scale);
		    while (z < hitz)
		    {
		        if (!inside)
		        {
		            Block b = new Block();
		            b.setBlockID(BlockTypes.HULL_COLOR_GREY_ID);
		            modified.set(x - center.x + 8, y - center.y + 8, z - center.z + 8, b);
		        }
		        z++;
		    }
		    inside = !inside;
		}
	}

    private float getScale(Hull3f hull, ImportOBJParameters params, Point3i lowerGrid, Point3i upperGrid)
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

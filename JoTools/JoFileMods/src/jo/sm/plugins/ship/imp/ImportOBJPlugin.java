package jo.sm.plugins.ship.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;
import jo.vecmath.ext.Triangle3f;
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
            float scale = PlotLogic.getScale(hull, params.getLongestDimension(), lowerGrid, upperGrid, offset);
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            PlotLogic.mapHull(modified, hull, scale, lowerGrid, upperGrid, cb);
            return modified;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
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

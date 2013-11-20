package jo.sm.plugins.ship.imp;

import java.io.IOException;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.ext.Hull3f;

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
            Hull3f hull = OBJLogic.readFile(params.getFile());
            System.out.println("Read "+hull.getTriangles().size()+" triangles");
            if (hull.getTriangles().size() == 0)
                throw new IllegalArgumentException("OBJ File "+params.getFile()+" has no triangles defined in it.");
            Point3i lowerGrid = new Point3i();
            Point3i upperGrid = new Point3i();
            Point3i offset = new Point3i();
            float scale = PlotLogic.getScale(hull, params.getLongestDimension(), lowerGrid, upperGrid, offset);
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            PlotLogic.mapHull(modified, hull, new Point3f(scale, scale, scale), lowerGrid, upperGrid, cb);
        	ShipLogic.ensureCore(modified);
            return modified;
        }
        catch (IOException e)
        {
            cb.setError(e);
            return null;
        }
    }
}

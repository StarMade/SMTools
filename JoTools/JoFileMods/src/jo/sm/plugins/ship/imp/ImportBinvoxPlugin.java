package jo.sm.plugins.ship.imp;

import java.io.FileInputStream;
import java.io.IOException;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.plugins.ship.move.MovePlugin;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3i;

public class ImportBinvoxPlugin implements IBlocksPlugin
{
    public static final String NAME = "Import/Binvox";
    public static final String DESC = "Import Binvox file";
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
        return new ImportBinvoxParameters();
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
        ImportBinvoxParameters params = (ImportBinvoxParameters)p;
        short color = sm.getSelectedBlockType();
        if (color <= 0)
            color = BlockTypes.HULL_COLOR_GREY_ID;
        try
        {
            FileInputStream fis = new FileInputStream(params.getFile());
            BinvoxData hull = BinvoxLogic.readHeader(fis);
            if (hull == null)
                return null;
            cb.setStatus("Converting "+hull.getYSpan()+"x"+hull.getXSpan()+"x"+hull.getZSpan());
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            mapHull(modified, hull, cb, color);
            cb.setStatus("Centering hull");
            Point3i lower = new Point3i();
            Point3i upper = new Point3i();
            modified.getBounds(lower, upper);
            //System.out.println("Old bounds="+lower+" -- "+upper);
            int dx = (upper.x + lower.x)/2 - 8; 
            int dy = (upper.y + lower.y)/2 - 8; 
            int dz = (upper.z + lower.z)/2 - 8;
            //System.out.println("Move "+dx+","+dy+","+dz);
            modified = MovePlugin.shift(modified, dx, dy, dz, cb);
            // setting core
        	ShipLogic.ensureCore(modified);
            //modified.getBounds(lower, upper);
            //System.out.println("New bounds="+lower+" -- "+upper);
            //Point3i core = MovePlugin.findCore(modified);
            //System.out.println("Core="+core);
            return modified;
        }
        catch (IOException e)
        {
            cb.setError(e);
            return null;
        }
    }

    private void mapHull(SparseMatrix<Block> modified, BinvoxData hull, IPluginCallback cb, short color) throws IOException
    {
        cb.startTask(hull.getZSpan());
        
        for (int x = 0; x < hull.getXSpan(); x++)
        {
            cb.workTask(1);
            for (int z = 0; z < hull.getZSpan(); z++)
                for (int y = 0; y < hull.getYSpan(); y++)
                {
                    if (BinvoxLogic.getVoxel(hull, x, y, z) == false)
                        continue;
                    // if surrounded, skip
                    if (BinvoxLogic.getVoxel(hull, x - 1, y, z)
                            && BinvoxLogic.getVoxel(hull, x + 1, y, z)
                            && BinvoxLogic.getVoxel(hull, x, y - 1, z)
                            && BinvoxLogic.getVoxel(hull, x, y + 1, z)
                            && BinvoxLogic.getVoxel(hull, x, y, z - 1)
                            && BinvoxLogic.getVoxel(hull, x, y, z + 1)) 
                        continue;
                    Block b = new Block();
                    b.setBlockID(color);
                    modified.set(x, y, z, b);
                }
            //System.out.println(modified.size()+" blocks  "+Runtime.getRuntime().freeMemory());
            if (cb.isPleaseCancel())
                break;
            if (x >= 2)
                hull.getVoxels()[x - 2] = null; // free up memory
        }
        cb.endTask();
    }
}

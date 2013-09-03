package jo.sm.plugins.ship.imp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class ImportBinvoxPlugin implements IBlocksPlugin
{
    public static final String NAME = "Import/Binvox";
    public static final String DESC = "Import Binvox file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_GENERATE },
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
    public Object getParameterBean()
    {
        return new ImportBinvoxParameters();
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
        try
        {
            FileInputStream fis = new FileInputStream(params.getFile());
            cb.setStatus("Reading "+params.getFile());
            BinvoxData hull = BinvoxLogic.read(fis);
            fis.close();
            if (hull == null)
                return null;
            cb.setStatus("Dimensions:"+hull.getWidth()+","+hull.getHeight()+","+hull.getDepth());
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            mapHull(modified, hull, cb);
            modified.set(8, 8, 8, new Block(BlockTypes.CORE_ID));
            return modified;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void mapHull(SparseMatrix<Block> modified, BinvoxData hull, IPluginCallback cb)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        BinvoxLogic.getBounds(hull, lower, upper);
        Point3i center = new Point3i();
        center.x = (lower.x + upper.x)/2;
        center.y = (lower.y + upper.y)/2;
        center.z = (lower.z + upper.z)/2;
        cb.startTask((upper.x - lower.x + 1)*(upper.y - lower.y + 1)*(upper.z - lower.z + 1));
        
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
        {
            cb.workTask(1);
            Point3i p = i.next();
            int o = getIndex(p.x, p.y, p.z, hull);
            if (hull.getVoxels()[o++] == 0)
            	continue;
            Block b = new Block();
            b.setBlockID(BlockTypes.HULL_COLOR_GREY_ID);
            modified.set(p.x - center.x + 8, p.y - center.y + 8, p.z - center.z + 8, b);
            if (cb.isPleaseCancel())
                break;
        }
        cb.endTask();
    }

    private int getIndex(int x, int y, int z, BinvoxData hull) 
    { 
        int index = x * hull.getWidth() * hull.getHeight() + z * hull.getWidth() + y;
        return index;
    }
}

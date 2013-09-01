package jo.sm.plugins.ship.imp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.mods.IBlocksPlugin;
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
            Object p)
    {
        ImportBinvoxParameters params = (ImportBinvoxParameters)p;        
        try
        {
            FileInputStream fis = new FileInputStream(params.getFile());
            BinvoxData hull = BinvoxLogic.read(fis);
            fis.close();
            if (hull == null)
                return null;
            System.out.println("Dimensions:"+hull.getWidth()+","+hull.getHeight()+","+hull.getDepth());
            SparseMatrix<Block> modified = new SparseMatrix<Block>();
            mapHull(modified, hull);
            modified.set(8, 8, 8, new Block(BlockTypes.CORE_ID));
            return modified;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void mapHull(SparseMatrix<Block> modified, BinvoxData hull)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        BinvoxLogic.getBounds(hull, lower, upper);
        Point3i center = new Point3i();
        center.x = (lower.x + upper.x)/2;
        center.y = (lower.y + upper.y)/2;
        center.z = (lower.z + upper.z)/2;
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
        {
            Point3i p = i.next();
            int o = getIndex(p.x, p.y, p.z, hull);
            if (hull.getVoxels()[o++] == 0)
            	continue;
            Block b = new Block();
            b.setBlockID(BlockTypes.HULL_COLOR_GREY_ID);
            modified.set(p.x - center.x + 8, p.y - center.y + 8, p.z - center.z + 8, b);
        }
    }

    private int getIndex(int x, int y, int z, BinvoxData hull) 
    { 
        int index = x * hull.getWidth() * hull.getHeight() + z * hull.getWidth() + y;
        return index;
    }
}

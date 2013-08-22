package jo.sm.plugins.planet.hollow;

import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class HollowPlugin implements IBlocksPlugin
{
    public static final String NAME = "Hollow";
    public static final String DESC = "Remove all non-surface blocks.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_MODIFY },
        { TYPE_STATION, SUBTYPE_MODIFY },
        { TYPE_SHOP, SUBTYPE_MODIFY },
        { TYPE_FLOATINGROCK, SUBTYPE_MODIFY },
        { TYPE_PLANET, SUBTYPE_MODIFY },
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
        return null;
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
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        original.getBounds(lower, upper);
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (CubeIterator i = new CubeIterator(lower, upper); i.hasNext(); )
        {
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            if (b == null)
                continue;
            if (isEdge(original, xyz))
                modified.set(xyz, b);
        }
        return modified;
    }

    private boolean isEdge(SparseMatrix<Block> grid, Point3i xyz)
    {
        if (!grid.contains(xyz.x - 1, xyz.y, xyz.z))
            return true;
        if (!grid.contains(xyz.x + 1, xyz.y, xyz.z))
            return true;
        if (!grid.contains(xyz.x, xyz.y - 1, xyz.z))
            return true;
        if (!grid.contains(xyz.x, xyz.y + 1, xyz.z))
            return true;
        if (!grid.contains(xyz.x, xyz.y, xyz.z - 1))
            return true;
        if (!grid.contains(xyz.x, xyz.y, xyz.z + 1))
            return true;
        return false;
    }
}

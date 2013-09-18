package jo.sm.plugins.planet.hollow;

import java.util.Iterator;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
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
            Object p, StarMade sm, IPluginCallback cb)
    {
        Set<Point3i> exterior = HullLogic.findExterior(original, cb);
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            if (isEdge(exterior, xyz) || (b.getBlockID() == BlockTypes.CORE_ID))
                modified.set(xyz, b);
        }
        return modified;
    }

    private boolean isEdge(Set<Point3i> exterior, Point3i xyz)
    {
        if (exterior.contains(new Point3i(xyz.x - 1, xyz.y, xyz.z)))
            return true;
        if (exterior.contains(new Point3i(xyz.x + 1, xyz.y, xyz.z)))
            return true;
        if (exterior.contains(new Point3i(xyz.x, xyz.y - 1, xyz.z)))
            return true;
        if (exterior.contains(new Point3i(xyz.x, xyz.y + 1, xyz.z)))
            return true;
        if (exterior.contains(new Point3i(xyz.x, xyz.y, xyz.z - 1)))
            return true;
        if (exterior.contains(new Point3i(xyz.x, xyz.y, xyz.z + 1)))
            return true;
        return false;
    }
}

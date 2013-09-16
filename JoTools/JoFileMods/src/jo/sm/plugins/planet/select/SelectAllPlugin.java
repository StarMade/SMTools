package jo.sm.plugins.planet.select;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class SelectAllPlugin implements IBlocksPlugin
{
    public static final String NAME = "Select All";
    public static final String DESC = "Select Entire Object";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT, 23 },
        { TYPE_STATION, SUBTYPE_EDIT, 23 },
        { TYPE_SHOP, SUBTYPE_EDIT, 23 },
        { TYPE_FLOATINGROCK, SUBTYPE_EDIT, 23 },
        { TYPE_PLANET, SUBTYPE_EDIT, 23 },
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
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        original.getBounds(lower, upper);
        sm.setSelectedLower(lower);
        sm.setSelectedUpper(upper);
        return null;
    }
}

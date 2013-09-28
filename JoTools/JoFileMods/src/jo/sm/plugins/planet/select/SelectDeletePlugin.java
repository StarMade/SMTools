package jo.sm.plugins.planet.select;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.GridLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class SelectDeletePlugin implements IBlocksPlugin
{
    public static final String NAME = "Delete";
    public static final String DESC = "Delete selection";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT, 22 },
        { TYPE_STATION, SUBTYPE_EDIT, 22 },
        { TYPE_SHOP, SUBTYPE_EDIT, 22 },
        { TYPE_FLOATINGROCK, SUBTYPE_EDIT, 22 },
        { TYPE_PLANET, SUBTYPE_EDIT, 22 },
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
        return null;
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
        Point3i lower = sm.getSelectedLower();
        Point3i upper = sm.getSelectedUpper();
        if ((lower != null) && (upper != null))
        {
            GridLogic.delete(original, lower, upper);
            return original;
        }
        return null;
    }
}

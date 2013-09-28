package jo.sm.plugins.planet.select;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

public class SelectNonePlugin implements IBlocksPlugin
{
    public static final String NAME = "Select None";
    public static final String DESC = "Clear selection";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT, 24 },
        { TYPE_STATION, SUBTYPE_EDIT, 24 },
        { TYPE_SHOP, SUBTYPE_EDIT, 24 },
        { TYPE_FLOATINGROCK, SUBTYPE_EDIT, 24 },
        { TYPE_PLANET, SUBTYPE_EDIT, 24 },
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
        sm.setSelectedLower(null);
        sm.setSelectedUpper(null);
        return null;
    }
}

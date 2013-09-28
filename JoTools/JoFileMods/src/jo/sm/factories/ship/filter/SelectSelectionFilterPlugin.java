package jo.sm.factories.ship.filter;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

public class SelectSelectionFilterPlugin implements IBlocksPlugin
{
    public static final String NAME = "Selection Only";
    public static final String DESC = "View only selection";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_VIEW, 15 },
        { TYPE_STATION, SUBTYPE_VIEW, 15 },
        { TYPE_SHOP, SUBTYPE_VIEW, 15 },
        { TYPE_FLOATINGROCK, SUBTYPE_VIEW, 15 },
        { TYPE_PLANET, SUBTYPE_VIEW, 15 },
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
    public int[][] getClassifications()
    {
        return CLASSIFICATIONS;
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
	public SparseMatrix<Block> modify(SparseMatrix<Block> original,
			Object params, StarMade sm, IPluginCallback cb)
	{
		sm.setViewFilter(new SelectionFilterPlugin());
		return null;
	}

}

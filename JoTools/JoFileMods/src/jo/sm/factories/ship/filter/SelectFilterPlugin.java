package jo.sm.factories.ship.filter;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

public class SelectFilterPlugin implements IBlocksPlugin
{
	private FilterDefinition	mDef;
	
	public SelectFilterPlugin(FilterDefinition def)
	{
		mDef = def;
	}

	@Override
	public String getName()
	{
		return mDef.getTitle();
	}

	@Override
	public String getDescription()
	{
		return mDef.getDescription();
	}

	@Override
	public String getAuthor()
	{
		return mDef.getAuthor();
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
		int[][] classifications = new int[][] {
        { TYPE_SHIP, SUBTYPE_VIEW, mDef.getPriority() },
        { TYPE_STATION, SUBTYPE_VIEW, mDef.getPriority() },
        { TYPE_SHOP, SUBTYPE_VIEW, mDef.getPriority() },
        { TYPE_FLOATINGROCK, SUBTYPE_VIEW, mDef.getPriority() },
        { TYPE_PLANET, SUBTYPE_VIEW, mDef.getPriority() },
		};
		return classifications;
	}

	@Override
	public SparseMatrix<Block> modify(SparseMatrix<Block> original,
			Object params, StarMade sm, IPluginCallback cb)
	{
		if (mDef.getBlocks().size() == 0)
			sm.setViewFilter(null);
		else
			sm.setViewFilter(new FilterPlugin(mDef));
		return null;
	}

}

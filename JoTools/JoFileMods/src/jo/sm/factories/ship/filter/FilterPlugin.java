package jo.sm.factories.ship.filter;

import java.util.Iterator;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class FilterPlugin implements IBlocksPlugin
{
	private FilterDefinition	mDef;
	
	public FilterPlugin(FilterDefinition def)
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
		SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            Block b = original.get(p);
            if (mDef.getBlocks().contains(b.getBlockID()))
                modified.set(p, b);
        }
        return modified;
	}

}

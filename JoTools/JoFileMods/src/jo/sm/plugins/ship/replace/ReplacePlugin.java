package jo.sm.plugins.ship.replace;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class ReplacePlugin implements IBlocksPlugin
{
    public static final String NAME = "Replace";
    public static final String DESC = "Replace one color on your ship with another.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_PAINT },
        { TYPE_STATION, SUBTYPE_PAINT },
        { TYPE_SHOP, SUBTYPE_PAINT },
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
        return new ReplaceParameters();
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
        ReplaceParameters params = (ReplaceParameters)p;
        //System.out.println("Params: color1="+params.getColor1()+", color2="+params.getColor2());
        cb.setStatus("Replacing colors");
        cb.startTask(original.size());
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            if (BlockTypes.isAnyHull(b.getBlockID()))
                b = modify(xyz, b, params);
            modified.set(xyz, b);
            cb.workTask(1);
        }
        cb.endTask();
        return modified;
    }

    private Block modify(Point3i xyz, Block b, ReplaceParameters params)
    {
    	short oldColor = BlockTypes.getColor(b.getBlockID());
    	if (oldColor == params.getColor1())
    	{
    		short newColor = BlockTypes.getColoredBlock(b.getBlockID(), params.getColor2());
    		if (newColor >= 0)
    			b.setBlockID(newColor);
    	}
        return b;
    }
}

package jo.sm.plugins.ship.replace;

import java.util.Iterator;

import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class ReplaceBlocksPlugin implements IBlocksPlugin
{
    public static final String NAME = "Replace Blocks";
    public static final String DESC = "Replace one type of block on your ship with another.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_PAINT },
        { TYPE_STATION, SUBTYPE_PAINT },
        { TYPE_SHOP, SUBTYPE_PAINT },
        { TYPE_PLANET, SUBTYPE_PAINT },
        { TYPE_FLOATINGROCK, SUBTYPE_PAINT },
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
        return new ReplaceBlocksParameters();
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
        ReplaceBlocksParameters params = (ReplaceBlocksParameters)p;
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);
        //System.out.println("Params: color1="+params.getColor1()+", color2="+params.getColor2());
        cb.setStatus("Replacing colors");
        cb.startTask(PluginUtils.getVolume(lower, upper));
        SparseMatrix<Block> modified = new SparseMatrix<Block>(original);
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
        {
            cb.workTask(1);
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            if (b == null)
                continue;
            if (b.getBlockID() == params.getColor1())
                if (params.getColor2() == 0)
                    b = null;
                else
                {
                	short oldOri = b.getOrientation();
                    b = new Block(params.getColor2());
                    b.setOrientation(oldOri);
                }
            modified.set(xyz, b);
        }
        cb.endTask();
        return modified;
    }
}

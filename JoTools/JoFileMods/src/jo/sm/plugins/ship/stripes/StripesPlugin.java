package jo.sm.plugins.ship.stripes;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class StripesPlugin implements IBlocksPlugin
{
    public static final String NAME = "Stripes";
    public static final String DESC = "Paint your ship with stripes or checkerboards.";
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
        return new StripesParameters();
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
        StripesParameters params = (StripesParameters)p;
        //System.out.println("Params: color1="+params.getColor1()+", color2="+params.getColor2()
        //        +", X="+params.isXAxis()+","+params.getXWidth1()+","+params.getXWidth2()
        //        +", Y="+params.isYAxis()+","+params.getYWidth1()+","+params.getYWidth2()
        //        +", Z="+params.isZAxis()+","+params.getZWidth1()+","+params.getZWidth2());
        cb.startTask(original.size());
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            if (b == null)
                continue;
            if (BlockTypes.isAnyHull(b.getBlockID()))
                b = modify(xyz, b, params);
            modified.set(xyz, b);
            cb.workTask(1);
        }
        cb.endTask();
        return modified;
    }

    private Block modify(Point3i xyz, Block b, StripesParameters params)
    {
        boolean color = false;
        if (params.isXAxis())
        {
            int off = mod(xyz.x, params.getXWidth1() + params.getXWidth2());
            if (off < params.getXWidth1())
                color = !color;
        }
        if (params.isYAxis())
        {
            int off = mod(xyz.y, params.getYWidth1() + params.getYWidth2());
            if (off < params.getYWidth1())
                color = !color;
        }
        if (params.isZAxis())
        {
            int off = mod(xyz.z, params.getZWidth1() + params.getZWidth2());
            if (off < params.getZWidth1())
                color = !color;
        }
        short newColor = BlockTypes.getColoredBlock(b.getBlockID(), color ? params.getColor2() : params.getColor1());
        b = new Block(b);
        b.setBlockID(newColor);
        return b;
    }

    private int mod(int value, int width)
    {
        value %= width;
        if (value < 0)
            value += width;
        return value;
    }
}

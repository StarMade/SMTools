package jo.sm.plugins.ship.stripes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;
import jo.vecmath.logic.MathUtils;

public class OmbrePlugin implements IBlocksPlugin
{
    public static final String NAME = "Ombre";
    public static final String DESC = "Paint a pattern fading from one color to another.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_PAINT },
        { TYPE_STATION, SUBTYPE_PAINT },
        { TYPE_SHOP, SUBTYPE_PAINT },
        };
    public Random mRND = new Random();

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
        return new OmbreParameters();
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
    	OmbreParameters params = (OmbreParameters)p;
        Point3i lower;
        Point3i upper;
        if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null))
        {
        	lower = sm.getSelectedLower();
        	upper = sm.getSelectedUpper();
        }
        else
        {
        	lower = new Point3i();
        	upper = new Point3i();
        	original.getBounds(lower, upper);
        }
        cb.startTask((upper.x - lower.x + 1)*(upper.y - lower.y + 1)*(upper.z - lower.z + 1));
        SparseMatrix<Block> modified = new SparseMatrix<Block>(original);
        for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
        {
        	cb.workTask(1);
            Point3i xyz = i.next();
            Block b = modified.get(xyz);
            if (b == null)
                continue;
            if (BlockTypes.isAnyHull(b.getBlockID()))
            {
                b = modify(xyz, b, params, lower, upper);
            	modified.set(xyz, b);
            }
        }
        cb.endTask();
        return modified;
    }

    private Block modify(Point3i xyz, Block b, OmbreParameters params, Point3i lower, Point3i upper)
    {
        List<Double> medians = new ArrayList<Double>();
        if (params.isXAxis())
        {
        	double a = MathUtils.interpolate(xyz.x, lower.x, upper.x, 0, 1);
        	medians.add(a);
        }
        if (params.isYAxis())
        {
        	double a = MathUtils.interpolate(xyz.y, lower.y, upper.y, 0, 1);
        	medians.add(a);
        }
        if (params.isZAxis())
        {
        	double a = MathUtils.interpolate(xyz.z, lower.z, upper.z, 0, 1);
        	medians.add(a);
        }
        double median = 0;
        for (Double m : medians)
        	median += m/medians.size();
        
        short newColor;
        if (mRND.nextDouble() < median)
        	newColor = params.getColor2();
        else
        	newColor = params.getColor1();
        newColor = BlockTypes.getColoredBlock(b.getBlockID(), newColor);
        b = new Block(b);
        b.setBlockID(newColor);
        return b;
    }
}

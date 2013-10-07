package jo.sm.plugins.ship.reflect;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.GridLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.CornerLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ship.logic.WedgeLogic;
import jo.vecmath.Point3i;

public class ReflectPlugin implements IBlocksPlugin
{
    public static final String NAME = "Reflect";
    public static final String DESC = "Reflect ship around the core.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_MODIFY },
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
        return new ReflectParameters();
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
        ReflectParameters params = (ReflectParameters)p;
        SparseMatrix<Block> modified;
        if ((sm.getSelectedLower() == null) || (sm.getSelectedUpper() == null))
        {
	        Point3i core = findCore(original);
	        //System.out.println("  Core at "+core);
	        modified = reflectAround(original, params, core);
        }
        else
        {
        	Point3i lower = sm.getSelectedLower();
        	Point3i upper = sm.getSelectedUpper();
        	Point3i center = new Point3i(lower);
        	center.add(upper);
        	center.scale(1, 2);
        	modified = new SparseMatrix<Block>(original);
        	SparseMatrix<Block> grid = GridLogic.extract(modified, lower, upper);
        	GridLogic.delete(modified, lower, upper);
        	grid = reflectAround(grid, params, center);
        	GridLogic.insert(modified, grid, lower);
        }
    	ShipLogic.ensureCore(modified);
        return modified;
    }

	private SparseMatrix<Block> reflectAround(SparseMatrix<Block> original,
			ReflectParameters params, Point3i around)
	{
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i inPoint = i.next();
            Point3i outPoint = reflect(inPoint, around, params);
            Block b = original.get(inPoint);
            //System.out.println("  "+inPoint+" -> "+outPoint);
            if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_WEDGE_ID))
            {
                short ori = b.getOrientation();
                ori = WedgeLogic.reflect(ori, params.isXReflect(), params.isYReflect(), params.isZReflect());
                if (ori >= 0)
                    b.setOrientation(ori);
                else
                    System.out.println("Could not rotate wedge ori="+b.getOrientation());
            }
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_CORNER_ID))
            {
                short ori = b.getOrientation();
                ori = CornerLogic.reflect(ori, params.isXReflect(), params.isYReflect(), params.isZReflect());
                if (ori >= 0)
                    b.setOrientation(ori);
                else
                    System.out.println("Could not rotate corner ori="+b.getOrientation());
            }
            modified.set(outPoint, b);
        }
        Point3i core = ShipLogic.findCore(modified);
        if (core != null)
        	if ((core.x != 8) || (core.y != 8) || (core.z != 8))
        	{
        		Block c = modified.get(core);
        		modified.set(core, null);
        		modified.set(8, 8, 8, c);
        	}
		return modified;
	}
	
	private Point3i reflect(Point3i p, Point3i around, ReflectParameters params)
	{
		Point3i n = new Point3i(p);
		n.sub(around);
		if (params.isXReflect())
			n.x = -n.x;
		if (params.isYReflect())
			n.y = -n.y;
		if (params.isZReflect())
			n.z = -n.z;
		n.add(around);
		return n;
	}

    private Point3i findCore(SparseMatrix<Block> grid)
    {
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            if (grid.get(xyz).getBlockID() == BlockTypes.CORE_ID)
                return xyz;
        }
        return null;
    }
}

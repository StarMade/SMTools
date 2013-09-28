package jo.sm.plugins.ship.move;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class MovePlugin implements IBlocksPlugin
{
    public static final String NAME = "Move";
    public static final String DESC = "Move ship's core.";
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
        return new MoveParameters();
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
        MoveParameters params = (MoveParameters)p;        
        //System.out.println("Params: X="+params.getXRotate()
        //        +", Y="+params.getYRotate()
        //        +", Z="+params.getZRotate());
        Point3i core = findCore(original);
        System.out.println("  Core at "+core);
        SparseMatrix<Block> modified = shift(original, params.getXMove(), params.getYMove(), params.getZMove(), cb);
        // copy core
        modified.set(core, original.get(core));
        return modified;
    }

    public static SparseMatrix<Block> shift(SparseMatrix<Block> original,
            int dx, int dy, int dz, IPluginCallback cb)
    {
    	cb.startTask(original.size());
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
        	cb.workTask(1);
            Point3i from = i.next();
            Point3i to = new Point3i(from.x - dx, from.y - dy, from.z - dz);
            Block b = original.get(from);
            if (b.getBlockID() == BlockTypes.CORE_ID)
                continue;
            modified.set(to, b);
        }
        return modified;
    }
    
    public static Point3i findCore(SparseMatrix<Block> grid)
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

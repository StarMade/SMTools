package jo.sm.plugins.ship.rotate;

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
import jo.vecmath.Point4i;
import jo.vecmath.logic.TransformInteger;

public class RotatePlugin implements IBlocksPlugin
{
    public static final String NAME = "Rotate";
    public static final String DESC = "Rotate ship around the core.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_MODIFY },
        { TYPE_STATION, SUBTYPE_MODIFY },
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
        return new RotateParameters();
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
        RotateParameters params = (RotateParameters)p;
        SparseMatrix<Block> modified;
        if ((sm.getSelectedLower() == null) || (sm.getSelectedUpper() == null))
        {
	        Point3i core = findCore(original);
	        //System.out.println("  Core at "+core);
	        modified = rotateAround(original, params, core);
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
        	grid = rotateAround(grid, params, center);
        	GridLogic.insert(modified, grid, lower);
        }
    	ShipLogic.ensureCore(modified);
        return modified;
    }

	public static SparseMatrix<Block> rotateAround(SparseMatrix<Block> original,
			RotateParameters params, Point3i around)
	{
		Point4i inPoint = new Point4i();
        Point4i outPoint = new Point4i();
        TransformInteger t = new TransformInteger();
        t.setIdentity();
        t.translate(-around.x, -around.y, -around.z);
        t.rotateEuler(params.getXRotate(), params.getYRotate(), params.getZRotate());
        t.translate(around.x, around.y, around.z);
        //System.out.print("Matrix: "+t);
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            inPoint.x = xyz.x; inPoint.y = xyz.y; inPoint.z = xyz.z; inPoint.w = 1;
            Block b = original.get(xyz);
            t.transform(inPoint, outPoint);
            //System.out.println("  "+inPoint+" -> "+outPoint);
            if (BlockTypes.isWedge(b.getBlockID()) || BlockTypes.isPowerWedge(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_WEDGE_ID))
            {
                short ori = b.getOrientation();
                ori = WedgeLogic.rotate(ori, params.getXRotate()/90, params.getYRotate()/90, params.getZRotate()/90);
                if (ori >= 0)
                    b.setOrientation(ori);
                else
                    System.out.println("Could not rotate wedge ori="+b.getOrientation());
            }
            if (BlockTypes.isCorner(b.getBlockID()) || BlockTypes.isPowerCorner(b.getBlockID()) || (b.getBlockID() == BlockTypes.GLASS_CORNER_ID))
            {
                short ori = b.getOrientation();
                ori = CornerLogic.rotate(ori, params.getXRotate()/90, params.getYRotate()/90, params.getZRotate()/90);
                if (ori >= 0)
                    b.setOrientation(ori);
                else
                    System.out.println("Could not rotate corner ori="+b.getOrientation());
            }
            modified.set(outPoint.x, outPoint.y, outPoint.z, b);
        }
		return modified;
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

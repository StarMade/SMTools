package jo.sm.plugins.ship.rotate;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.CornerLogic;
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
    public Object getParameterBean()
    {
        return new RotateParameters();
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
        //System.out.println("Params: X="+params.getXRotate()
        //        +", Y="+params.getYRotate()
        //        +", Z="+params.getZRotate());
        Point4i inPoint = new Point4i();
        Point4i outPoint = new Point4i();
        Point3i core = findCore(original);
        System.out.println("  Core at "+core);
        TransformInteger t = new TransformInteger();
        t.setIdentity();
        t.translate(-core.x, -core.y, -core.z);
        t.rotateEuler(params.getXRotate(), params.getYRotate(), params.getZRotate());
        t.translate(core.x, core.y, core.z);
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

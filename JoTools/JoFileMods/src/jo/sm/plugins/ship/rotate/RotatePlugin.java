package jo.sm.plugins.ship.rotate;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.logic.Transform;

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
            Object p)
    {
        RotateParameters params = (RotateParameters)p;        
        //System.out.println("Params: X="+params.getXRotate()
        //        +", Y="+params.getYRotate()
        //        +", Z="+params.getZRotate());
        Point3i core = findCore(original);
        System.out.println("  Core at "+core);
        Transform t = new Transform();
        t.setIdentity();
        t.translate(-core.x, -core.y, -core.z);
        t.rotateEuler(params.getXRotate(), params.getYRotate(), params.getZRotate());
        t.translate(core.x, core.y, core.z);
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            Point3f fPoint = new Point3f(xyz.x, xyz.y, xyz.z);
            t.transform(fPoint);
            //System.out.println("  "+xyz+" -> "+fPoint);
            modified.set(toInt(fPoint.x), toInt(fPoint.y), toInt(fPoint.z), b);
        }
        return modified;
    }
    
    private int toInt(float f)
    {
        return (int)(f + .5f);
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

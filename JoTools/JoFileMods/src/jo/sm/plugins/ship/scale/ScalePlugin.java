package jo.sm.plugins.ship.scale;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class ScalePlugin implements IBlocksPlugin
{
    public static final String NAME = "Scale";
    public static final String DESC = "Scale ship's size.";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_MODIFY },
        { TYPE_STATION, SUBTYPE_MODIFY },
        { TYPE_SHOP, SUBTYPE_MODIFY },
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
        return new ScaleParameters();
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
        ScaleParameters params = (ScaleParameters)p;        
        //System.out.println("Params: X="+params.getXRotate()
        //        +", Y="+params.getYRotate()
        //        +", Z="+params.getZRotate());
        if (params.getXScale() < 1)
            params.setXScale(1);
        if (params.getYScale() < 1)
            params.setYScale(1);
        if (params.getZScale() < 1)
            params.setZScale(1);
        Point3i core = findCore(original);
        System.out.println("  Core at "+core);
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        for (Iterator<Point3i> i = original.iteratorNonNull(); i.hasNext(); )
        {
            Point3i xyz = i.next();
            Block b = original.get(xyz);
            Point3i iPoint = transform(xyz, core, params);
            //System.out.println("  "+xyz+" -> "+fPoint);
            modified.set(iPoint, b);
            if (b.getBlockID() != BlockTypes.CORE_ID)
            {
                for (int x = 0; x < params.getXScale(); x++)
                    for (int y = 0; y < params.getYScale(); y++)
                        for (int z = 0; z < params.getZScale(); z++)
                            if ((x != 0) || (y != 0) || (z != 0))
                            {
                                Block newB = new Block(b);
                                if (BlockTypes.isController(newB.getBlockID()))
                                    newB.setBlockID(BlockTypes.CONTROLLER_IDS.get(newB.getBlockID()));
                                modified.set(iPoint.x + x, iPoint.y + y, iPoint.z + z, newB);
                            }
            }
        }
        return modified;
    }
    
    private Point3i transform(Point3i ori, Point3i core, ScaleParameters params)
    {
    	Point3i trans = new Point3i();
    	trans.x = transform(ori.x, core.x, params.getXScale());
    	trans.y = transform(ori.y, core.y, params.getYScale());
    	trans.z = transform(ori.z, core.z, params.getZScale());
    	return trans;
    }
    
    private int transform(int ori, int core, int scale)
    {
    	ori -= core;
    	ori *= scale;
    	ori += core;
    	return ori;
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

package jo.sm.plugins.ship.fill;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
import jo.vecmath.Point3i;

public class FillBlockPlugin implements IBlocksPlugin
{
    public static final String NAME = "Fill with Block";
    public static final String DESC = "Fill ship interior with specific block";
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
        return new FillBlockParameters();
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
        FillBlockParameters params = (FillBlockParameters)p;    
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        List<Point3i> interior = new ArrayList<Point3i>();
        Set<Point3i> exterior = HullLogic.findExterior(original, cb);
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);
        FillPlugin.scopeInterior(original, modified, interior, exterior, lower, upper, cb);
        int todo = params.getTotal();
        if (todo <= 0)
            todo = interior.size();
        FillStrategy strategy = new FillStrategy(params.getStrategy(), params.getAxis(), lower, upper);
        cb.setStatus("Filling");
        cb.startTask(todo);
        FillPlugin.fill(modified, interior, todo, (short)-1, params.getBlockID(), strategy, cb);
        cb.endTask();
        return modified;
    }
}

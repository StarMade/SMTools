package jo.sm.ship.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class HullLogic
{
    public static void power(SparseMatrix<Block> grid)
    {
        Map<Short, Short> filter = new HashMap<Short, Short>();
        for (int color = 0; color < BlockTypes.HULL_COLOR_MAP[0].length; color++)
        {
            filter.put(BlockTypes.HULL_COLOR_MAP[BlockTypes.HULL_COLORS][color], BlockTypes.HULL_COLOR_MAP[BlockTypes.POWERHULL_COLORS][color]);
            filter.put(BlockTypes.HULL_COLOR_MAP[BlockTypes.WEDGE_COLORS][color], BlockTypes.HULL_COLOR_MAP[BlockTypes.POWERWEDGE_COLORS][color]);
            filter.put(BlockTypes.HULL_COLOR_MAP[BlockTypes.CORNER_COLORS][color], BlockTypes.HULL_COLOR_MAP[BlockTypes.POWERCORNER_COLORS][color]);
        }
        doFilter(grid, filter);
    }
    
    public static void unpower(SparseMatrix<Block> grid)
    {
        Map<Short, Short> filter = new HashMap<Short, Short>();
        for (int color = 0; color < BlockTypes.HULL_COLOR_MAP[0].length; color++)
        {
            filter.put(BlockTypes.HULL_COLOR_MAP[BlockTypes.POWERHULL_COLORS][color], BlockTypes.HULL_COLOR_MAP[BlockTypes.HULL_COLORS][color]);
            filter.put(BlockTypes.HULL_COLOR_MAP[BlockTypes.POWERWEDGE_COLORS][color], BlockTypes.HULL_COLOR_MAP[BlockTypes.WEDGE_COLORS][color]);
            filter.put(BlockTypes.HULL_COLOR_MAP[BlockTypes.POWERCORNER_COLORS][color], BlockTypes.HULL_COLOR_MAP[BlockTypes.CORNER_COLORS][color]);
        }
        doFilter(grid, filter);
    }
       
    private static void doFilter(SparseMatrix<Block> grid, Map<Short, Short> filter)
    {
        for (Iterator<Point3i> i = grid.iterator(); i.hasNext(); )
        {
            Point3i coords = i.next();
            Block block = grid.get(coords);
            if (block == null)
                continue;
            short oldID = block.getBlockID();
            if (!filter.containsKey(oldID))
                continue;
            short newID = filter.get(oldID);
            if (newID != -1)
            {
                block.setBlockID(newID);
            }
        }
    }
    
    public static Set<Point3i> findExterior(SparseMatrix<Block> grid, IPluginCallback cb)
    {
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	grid.getBounds(lower, upper);
    	if (cb != null)
    	{
    		cb.setStatus("Calculating exterior");
    		cb.startTask((upper.x - lower.x + 3)*(upper.y - lower.y + 3)
    				+(upper.x - lower.x + 3)*(upper.z - lower.z + 3)
    				+(upper.z - lower.z + 3)*(upper.y - lower.y + 3)
    				);
    	}
    	Set<Point3i> exterior = new HashSet<Point3i>();
    	Point3i p = new Point3i();
    	// do z
    	for (p.x = lower.x - 1; p.x <= upper.x; p.x++)
    		for (p.y = lower.y - 1; p.y <= upper.y; p.y++)
    		{
    			if (cb != null)
    				cb.workTask(1);
    			// lower to upper
    			for (p.z = lower.z; p.z <= upper.z; p.z++)
    			{
    				if (grid.contains(p))
    					break;
    				else
    					exterior.add(new Point3i(p));
    			}
    			if (p.z < upper.z) // upper to lower
        			for (p.z = upper.z; p.z >= lower.z; p.z--)
        			{
        				if (grid.contains(p))
        					break;
        				else
        					exterior.add(new Point3i(p));
        			}
    		}
    	// do y
    	for (p.x = lower.x - 1; p.x <= upper.x; p.x++)
    		for (p.z = lower.z - 1; p.z <= upper.z; p.z++)
    		{
    			if (cb != null)
    				cb.workTask(1);
    			// lower to upper
    			for (p.y = lower.y; p.y <= upper.y; p.y++)
    			{
    				if (grid.contains(p))
    					break;
    				else
    					exterior.add(new Point3i(p));
    			}
    			if (p.y < upper.y) // upper to lower
        			for (p.y = upper.y; p.y >= lower.y; p.y--)
        			{
        				if (grid.contains(p))
        					break;
        				else
        					exterior.add(new Point3i(p));
        			}
    		}
    	// do x
    	for (p.y = lower.y - 1; p.y <= upper.y; p.y++)
    		for (p.z = lower.z - 1; p.z <= upper.z; p.z++)
    		{
    			if (cb != null)
    				cb.workTask(1);
    			// lower to upper
    			for (p.x = lower.x; p.x <= upper.x; p.x++)
    			{
    				if (grid.contains(p))
    					break;
    				else
    					exterior.add(new Point3i(p));
    			}
    			if (p.x < upper.x) // upper to lower
        			for (p.x = upper.x; p.x >= lower.x; p.x--)
        			{
        				if (grid.contains(p))
        					break;
        				else
        					exterior.add(new Point3i(p));
        			}
    		}
		if (cb != null)
			cb.endTask();
    	return exterior;
    }
}

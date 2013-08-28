package jo.sm.ship.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
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
}

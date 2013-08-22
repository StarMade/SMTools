package jo.sm.ship.logic;

import java.util.Iterator;

import jo.sm.data.BlockTypes;
import jo.sm.data.RenderTile;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class SmoothLogic
{
    private static Point3i[] DELTAS = {
        new Point3i(1, 0, 0),
        new Point3i(-1, 0, 0),
        new Point3i(0, 1, 0),
        new Point3i(0, -1, 0),
        new Point3i(0, 0, 1),
        new Point3i(0, 0, -1),        
    };
    
    public static void smooth(SparseMatrix<Block> grid)
    {
        boolean[] edges = new boolean[6];
        for (Iterator<Point3i> i = grid.iterator(); i.hasNext(); )
        {
            Point3i p = i.next();
            if (grid.contains(p))
                continue;
            int tot = 0;
            for (int j = 0; j < edges.length; j++)
            {
                edges[j] = isEdge(grid, p, DELTAS[j]);
                if (edges[j])
                    tot++;
            }
            if (tot != 2)
                continue;
            int ori = -1;
            if (edges[RenderTile.XM])
            {
                if (edges[RenderTile.YM])
                    ori = 3;
                else if (edges[RenderTile.YP])
                    ori = 5;
                else if (edges[RenderTile.ZM])
                    ori = 13;
                else if (edges[RenderTile.ZP])
                    ori = 8;
            }
            else if (edges[RenderTile.XP])
            {
                if (edges[RenderTile.YM])
                    ori = 1;
                else if (edges[RenderTile.YP])
                    ori = 7;
                else if (edges[RenderTile.ZM])
                    ori = 11;
                else if (edges[RenderTile.ZP])
                    ori = 10;
            }
            else if (edges[RenderTile.YM])
            {
                if (edges[RenderTile.ZM])
                    ori = 2;
                else if (edges[RenderTile.ZP])
                    ori = 0;
            }
            else if (edges[RenderTile.YP])
            {
                if (edges[RenderTile.ZM])
                    ori = 6;
                else if (edges[RenderTile.ZP])
                    ori = 4;
            }
            if (ori < 0)
                continue;
            Block b = new Block();
            b.setActive(false);
            b.setBlockID(calculateType(grid, p, edges));
            b.setHitPoints((short)100);
            b.setOrientation((short)ori);
            grid.set(p, b);
        }
    }

    private static short calculateType(SparseMatrix<Block> grid, Point3i p,
            boolean[] edges)
    {
        short type1 = -1;
        short type2 = -1;
        for (int i = 0; i < edges.length; i++)
        {
            if (!edges[i])
                continue;
            Point3i p2 = new Point3i();
            p2.add(p, DELTAS[i]);
            Block b = grid.get(p2);
            if (b == null)
                continue;
            if (type1 == -1)
                type1 = b.getBlockID();
            else if (type2 == -1)
            {
                type2 = b.getBlockID();
                break;
            }
        }
        if (type1 > type2)
            type1 = type2; 
        switch (type1)
        {
            case BlockTypes.HULL_COLOR_GREY_ID: return BlockTypes.HULL_COLOR_WEDGE_GREY_ID; 
            case BlockTypes.HULL_COLOR_PURPLE_ID: return BlockTypes.HULL_COLOR_WEDGE_PURPLE_ID; 
            case BlockTypes.HULL_COLOR_BROWN_ID: return BlockTypes.HULL_COLOR_WEDGE_BROWN_ID; 
            case BlockTypes.HULL_COLOR_BLACK_ID: return BlockTypes.HULL_COLOR_WEDGE_BLACK_ID; 
            case BlockTypes.HULL_COLOR_RED_ID: return BlockTypes.HULL_COLOR_WEDGE_RED_ID; 
            case BlockTypes.HULL_COLOR_BLUE_ID: return BlockTypes.HULL_COLOR_WEDGE_BLUE_ID; 
            case BlockTypes.HULL_COLOR_GREEN_ID: return BlockTypes.HULL_COLOR_WEDGE_GREEN_ID; 
            case BlockTypes.HULL_COLOR_YELLOW_ID: return BlockTypes.HULL_COLOR_WEDGE_YELLOW_ID; 
            case BlockTypes.HULL_COLOR_WHITE_ID: return BlockTypes.HULL_COLOR_WEDGE_WHITE_ID; 
            case BlockTypes.POWERHULL_COLOR_GREY: return BlockTypes.POWERHULL_COLOR_WEDGE_GREY; 
            case BlockTypes.POWERHULL_COLOR_PURPLE: return BlockTypes.POWERHULL_COLOR_WEDGE_PURPLE; 
            case BlockTypes.POWERHULL_COLOR_BROWN: return BlockTypes.POWERHULL_COLOR_WEDGE_BROWN; 
            case BlockTypes.POWERHULL_COLOR_BLACK: return BlockTypes.POWERHULL_COLOR_WEDGE_BLACK; 
            case BlockTypes.POWERHULL_COLOR_RED: return BlockTypes.POWERHULL_COLOR_WEDGE_RED; 
            case BlockTypes.POWERHULL_COLOR_BLUE: return BlockTypes.POWERHULL_COLOR_WEDGE_BLUE; 
            case BlockTypes.POWERHULL_COLOR_GREEN: return BlockTypes.POWERHULL_COLOR_WEDGE_GREEN; 
            //case BlockTypes.POWERHULL_COLOR_YELLOW: return BlockTypes.POWERHULL_COLOR_WEDGE_YELLOW; 
            case BlockTypes.POWERHULL_COLOR_WHITE: return BlockTypes.POWERHULL_COLOR_WEDGE_WHITE; 

        }
        return type1;
    }

    private static boolean isEdge(SparseMatrix<Block> grid, Point3i p, Point3i d)
    {
        Point3i p2 = new Point3i();
        p2.add(p, d);
        if (!grid.contains(p2))
            return false;
        short type = grid.get(p2).getBlockID();
        if (BlockTypes.isWedge(type) || BlockTypes.isPowerWedge(type))
            return false;
        return BlockTypes.isHull(type) || BlockTypes.isPowerHull(type);
    }
}

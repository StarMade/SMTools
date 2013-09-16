package jo.sm.ship.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Chunk;
import jo.sm.ship.data.Data;
import jo.vecmath.Point3i;
import jo.vecmath.logic.MathUtils;

public class ShipLogic
{
    public static void getBounds(Data datum, Point3i lower, Point3i upper)
    {
        boolean first = true;
        for (Chunk c : datum.getChunks())
        {
            Point3i pos = c.getPosition();
            for (CubeIterator i = new CubeIterator(new Point3i(0,0,0), new Point3i(15,15,15)); i.hasNext(); )
            {
                Point3i xyz = i.next();
                if ((c.getBlocks()[xyz.x][xyz.y][xyz.z] == null) || (c.getBlocks()[xyz.x][xyz.y][xyz.z].getBlockID() <= 0))
                    continue;
                if (first)
                {
                    lower.add(pos, xyz);
                    upper.add(pos, xyz);
                    first = false;
                }
                else
                {
                    lower.x = Math.min(lower.x, pos.x + xyz.x);
                    lower.y = Math.min(lower.y, pos.y + xyz.y);
                    lower.z = Math.min(lower.z, pos.z + xyz.z);
                    upper.x = Math.max(upper.x, pos.x + xyz.x);
                    upper.y = Math.max(upper.y, pos.y + xyz.y);
                    upper.z = Math.max(upper.z, pos.z + xyz.z);
                }
            }
        }
    }
    
    public static void getBounds(Map<Point3i,Data> data, Point3i lower, Point3i upper)
    {
        boolean first = true;
        for (Point3i o : data.keySet())
        {
            Point3i l = new Point3i();
            Point3i u = new Point3i();
            Data datum = data.get(o);
            getBounds(datum, l, u);
            adjustByBigChunk(l, o);
            adjustByBigChunk(u, o);
            if (first)
            {
                lower.set(l);
                upper.set(u);
                first = false;
            }
            else
            {
                lower.x = Math.min(lower.x, l.x);
                lower.y = Math.min(lower.y, l.y);
                lower.z = Math.min(lower.z, l.z);
                upper.x = Math.max(upper.x, u.x);
                upper.y = Math.max(upper.y, u.y);
                upper.z = Math.max(upper.z, u.z);
            }
        }
    }
    
    public static void dumpChunks(Map<Point3i, Data> data)
    {
        for (Point3i p : data.keySet())
        {
            Data datum = data.get(p);
            Point3i lower = new Point3i();
            Point3i upper = new Point3i();
            getBounds(datum, lower, upper);
            System.out.println("Datum: "+p+", "+lower+" -- "+upper);
            for (Chunk c : datum.getChunks())
            {
                System.out.println("  Chunk: "+c.getPosition()+", type="+c.getType());
            }
        }
    }
    
    public static SparseMatrix<Block> getBlocks(Map<Point3i, Data> data)
    {
        SparseMatrix<Block> blocks = new SparseMatrix<Block>();
        for (Point3i dataOrigin : data.keySet())
        {
        	Data datum = data.get(dataOrigin);
            for (Chunk c : datum.getChunks())
            {
                Point3i p = c.getPosition();
                //p.x += dataOrigin.x*256;
                //p.y += dataOrigin.y*256;
                //p.z += dataOrigin.z*256;
                for (CubeIterator i = new CubeIterator(new Point3i(0,0,0), new Point3i(15,15,15)); i.hasNext(); )
                {
                    Point3i xyz = i.next();
                    Block b = c.getBlocks()[xyz.x][xyz.y][xyz.z];
                    if ((b != null) && (b.getBlockID() > 0))
                        blocks.set(p.x + xyz.x, p.y + xyz.y, p.z + xyz.z, b);
                }
            }
        }
        return blocks;
    }
    
    private static void adjustByBigChunk(Point3i v, Point3i mod)
    {
        // looks like no mod is needed
//        v.a += mod.a*16*16;
//        v.b += mod.b*16*16;
//        v.c += mod.c*16*16;
    }
    
    public static Map<Point3i, Data> getData(SparseMatrix<Block> blocks)
    {
        long now = System.currentTimeMillis();
        Map<Point3i, Data> data = new HashMap<Point3i, Data>();
        Point3i lowerUniverse = new Point3i();
        Point3i upperUniverse = new Point3i();
        blocks.getBounds(lowerUniverse, upperUniverse);
        Point3i lowerData = new Point3i();
        lowerData.x = MathUtils.stride(lowerUniverse.x + 128, 256); 
        lowerData.y = MathUtils.stride(lowerUniverse.y + 128, 256);
        if (lowerUniverse.z >= -128)
        	lowerData.z = MathUtils.stride(lowerUniverse.z + 128, 256);
        else
        	lowerData.z = MathUtils.stride(lowerUniverse.z + 128, 256 - 16);
        Point3i upperData = new Point3i();
        upperData.x = MathUtils.stride(upperUniverse.x + 128, 256); 
		upperData.y = MathUtils.stride(upperUniverse.y + 128, 256);
		if (upperUniverse.z >= -128)
			upperData.z = MathUtils.stride(upperUniverse.z + 128, 256);
		else
			upperData.z = MathUtils.stride(upperUniverse.z + 128, 256 - 16);
        for (Iterator<Point3i> i = new CubeIterator(lowerData, upperData); i.hasNext(); )
        {
            Point3i superChunkIndex = i.next();
            Data datum = new Data();
            Point3i superChunkOrigin;
            if (superChunkIndex.z >= 0)
            	superChunkOrigin = new Point3i(superChunkIndex.x*256, superChunkIndex.y*256, superChunkIndex.z*256);
            else
            	superChunkOrigin = new Point3i(superChunkIndex.x*256, superChunkIndex.y*256, superChunkIndex.z*(256 - 16));
            Point3i lowerSuperChunk = new Point3i(superChunkOrigin.x - 128, superChunkOrigin.y - 128, superChunkOrigin.z - 128);
            Point3i upperSuperChunk;
            if (superChunkIndex.z >= 0)
            	upperSuperChunk = new Point3i(superChunkOrigin.x + 127, superChunkOrigin.y + 127, superChunkOrigin.z + 127);
            else
            	upperSuperChunk = new Point3i(superChunkOrigin.x + 127, superChunkOrigin.y + 127, superChunkOrigin.z + 127 - 16);
            List<Chunk> chunks = new ArrayList<Chunk>();
            System.out.println("Splitting "+superChunkIndex+" -> "+lowerSuperChunk+" / "+upperSuperChunk);
            for (Iterator<Point3i> j = new CubeIterator(lowerSuperChunk, upperSuperChunk, new Point3i(16, 16, 16)); j.hasNext(); )
            {
                Point3i chunkOrigin = j.next();
                Point3i chunkPos = new Point3i(chunkOrigin);
                //chunkPos.sub(origin);
                Chunk chunk = new Chunk();
                chunk.setPosition(chunkPos);
                chunk.setBlocks(new Block[16][16][16]);
                chunk.setTimestamp(now);
                chunk.setType(1);
                boolean doneAny = false;
                for (Iterator<Point3i> k = new CubeIterator(new Point3i(), new Point3i(15, 15, 15)); k.hasNext(); )
                {
                    Point3i chunkOffset = k.next();
                    Block b = blocks.get(chunkOrigin.x + chunkOffset.x, chunkOrigin.y + chunkOffset.y, chunkOrigin.z + chunkOffset.z);
                    if (b == null)
                        continue;
                    chunk.getBlocks()[chunkOffset.x][chunkOffset.y][chunkOffset.z] = b;
                    doneAny = true;
                }
                if (doneAny)
                    chunks.add(chunk);
            }
            if (chunks.size() == 0)
                continue;
            datum.setChunks(chunks.toArray(new Chunk[0]));
            data.put(superChunkIndex,  datum);
        }
        return data;
    }
    
    
    public static Point3i findCore(SparseMatrix<Block> grid)
    {
        Point3i p = new Point3i(8, 8, 8);
        Block b = grid.get(p);
        if ((b != null) && (b.getBlockID() == BlockTypes.CORE_ID))
            return p;
        return findFirstBlock(grid, BlockTypes.CORE_ID);
    }
    
    public static Point3i findFirstBlock(SparseMatrix<Block> grid, short id)
    {
        List<Point3i> finds = findBlocks(grid, id, true);
        if (finds.size() == 0)
            return null;
        else
            return finds.get(0);
    }
    
    public static List<Point3i> findBlocks(SparseMatrix<Block> grid, short id)
    {
        return findBlocks(grid, id, false);
    }
    
    public static List<Point3i> findBlocks(SparseMatrix<Block> grid, short id, boolean stopAfterFirst)
    {
        List<Point3i> finds = new ArrayList<Point3i>();
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext(); )
        {
            Point3i pp = i.next();
            Block b = grid.get(pp);
            if (b.getBlockID() == id)
            {
                finds.add(pp);
                if (stopAfterFirst)
                    break;
            }
        }
        return finds;
    }

}

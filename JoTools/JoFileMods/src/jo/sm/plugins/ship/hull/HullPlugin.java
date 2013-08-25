package jo.sm.plugins.ship.hull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;
import jo.vecmath.logic.MathUtils;
import jo.vecmath.logic.Point3iLogic;

public class HullPlugin implements IBlocksPlugin
{
    public static final String NAME = "Hull";
    public static final String DESC = "Generate Basic Hull";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_GENERATE },
        };
    
    private static final Random mRND = new Random();

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
        return new HullParameters();
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
        HullParameters params = (HullParameters)p;        
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        switch (params.getType())
        {
        	case HullParameters.OPEN_FRAME:
        		generateOpenFrame(modified, params);
        		break;
        	case HullParameters.NEEDLE:
        		generateNeedle(modified, params);
        		break;
        	case HullParameters.CONE:
        		generateCone(modified, params);
        		break;
        	case HullParameters.CYLINDER:
        		generateCylinder(modified, params);
        		break;
        	case HullParameters.BOX:
        		generateBox(modified, params);
        		break;
        	case HullParameters.SPHERE:
        		generateSphere(modified, params);
        		break;
        	case HullParameters.DISC:
        		generateDisc(modified, params);
        		break;
        	case HullParameters.IRREGULAR:
        		generateIrregular(modified, params);
        		break;
        }
        return modified;
    }
    
    private void generateBox(SparseMatrix<Block> grid, HullParameters params)
    {
    	int r = (int)Math.ceil(Math.pow(params.getVolume()/6, .33333))/2;
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	lower.x = params.getCenterX() - r*2;
    	upper.x = params.getCenterX() + r*2;
    	lower.y = params.getCenterY() - r;
    	upper.y = params.getCenterY() + r;
    	lower.z = params.getCenterZ() - r*3;
    	upper.z = params.getCenterZ() + r*3;
    	for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
    	{
    		Point3i p = i.next();
    		addHull(grid, p);
    	}
    }
    
    private void generateSphere(SparseMatrix<Block> grid, HullParameters params)
    {
    	int r = (int)Math.ceil(Math.pow(params.getVolume()*3/4, .33333));
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	lower.x = params.getCenterX() - r;
    	upper.x = params.getCenterX() + r;
    	lower.y = params.getCenterY() - r;
    	upper.y = params.getCenterY() + r;
    	lower.z = params.getCenterZ() - r;
    	upper.z = params.getCenterZ() + r;
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
    	{
    		Point3i p = i.next();
    		if (Point3iLogic.distance(center, p) > r)
    			continue;
    		addHull(grid, p);
    	}
    }
    
    private void generateCone(SparseMatrix<Block> grid, HullParameters params)
    {
    	int r = (int)Math.ceil(Math.pow(params.getVolume()/Math.PI, .33333));
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	lower.x = params.getCenterX() - r;
    	upper.x = params.getCenterX() + r;
    	lower.y = params.getCenterY() - r;
    	upper.y = params.getCenterY() + r;
    	lower.z = params.getCenterZ() - r*3/2;
    	upper.z = params.getCenterZ() + r*3/2;
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
    	{
    		Point3i p = i.next();
    		double hR = MathUtils.interpolate(p.z, lower.z, upper.z, r, 0);
    		double pR = Math.sqrt((p.x - center.x)*(p.x - center.x) + (p.y - center.y)*(p.y - center.y));
    		if (pR > hR)
    			continue;
    		addHull(grid, p);
    	}
    }
    
    private void generateNeedle(SparseMatrix<Block> grid, HullParameters params)
    {
    	int r = (int)Math.ceil(Math.pow(params.getVolume()*3/8/2/Math.PI, .33333));
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	lower.x = params.getCenterX() - r;
    	upper.x = params.getCenterX() + r;
    	lower.y = params.getCenterY() - r;
    	upper.y = params.getCenterY() + r;
    	lower.z = params.getCenterZ() - r*8;
    	upper.z = params.getCenterZ() + r*8;
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
    	{
    		Point3i p = i.next();
    		double hR = MathUtils.interpolate(Math.abs(p.z - center.z), 0, r*8, r, 0);
    		double pR = Math.sqrt((p.x - center.x)*(p.x - center.x) + (p.y - center.y)*(p.y - center.y));
    		if (pR > hR)
    			continue;
    		addHull(grid, p);
    	}
    }
    
    private void generateCylinder(SparseMatrix<Block> grid, HullParameters params)
    {
    	int r = (int)Math.ceil(Math.pow(params.getVolume()/Math.PI/6, .33333));
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	lower.x = params.getCenterX() - r;
    	upper.x = params.getCenterX() + r;
    	lower.y = params.getCenterY() - r;
    	upper.y = params.getCenterY() + r;
    	lower.z = params.getCenterZ() - r*3;
    	upper.z = params.getCenterZ() + r*3;
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
    	{
    		Point3i p = i.next();
    		double pR = Math.sqrt((p.x - center.x)*(p.x - center.x) + (p.y - center.y)*(p.y - center.y));
    		if (pR > r)
    			continue;
    		addHull(grid, p);
    	}
    }
    
    private void generateDisc(SparseMatrix<Block> grid, HullParameters params)
    {
    	int r = (int)Math.ceil(Math.pow(params.getVolume()/Math.PI*3, .33333));
    	Point3i lower = new Point3i();
    	Point3i upper = new Point3i();
    	lower.x = params.getCenterX() - r;
    	upper.x = params.getCenterX() + r;
    	lower.y = params.getCenterY() - r/3;
    	upper.y = params.getCenterY() + r/3;
    	lower.z = params.getCenterZ() - r;
    	upper.z = params.getCenterZ() + r;
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	for (Iterator<Point3i> i = new CubeIterator(lower, upper); i.hasNext(); )
    	{
    		Point3i p = i.next();
    		double pR = Math.sqrt((p.z - center.z)*(p.z - center.z) + (p.x - center.x)*(p.x - center.x));
    		if (pR > r)
    			continue;
    		addHull(grid, p);
    	}
    }
    
    private void generateIrregular(SparseMatrix<Block> grid, HullParameters params)
    {
    	Set<Point3i> done = new HashSet<Point3i>();
    	List<Point3i> todo = new ArrayList<Point3i>();
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	todo.add(center);
    	for (int i = 0; i < params.getVolume(); i++)
    	{
    		int idx = mRND.nextInt(todo.size());
    		Point3i p = todo.get(idx);
    		todo.remove(idx);
    		addHull(grid, p);
    		done.add(p);
    		add(done, todo, p, 1, 0, 0);
    		add(done, todo, p, -1, 0, 0);
    		add(done, todo, p, 0, 1, 0);
    		add(done, todo, p, 0, -1, 0);
    		add(done, todo, p, 0, 0, 1);
    		add(done, todo, p, 0, 0, -1);
    	}
    }

	private void add(Set<Point3i> done, List<Point3i> todo, Point3i p, int dx,
			int dy, int dz)
	{
		Point3i next = new Point3i(p.x + dx, p.y + dy, p.z + dz);
		if (done.contains(next))
			return;
		if (!todo.contains(next))
			todo.add(next);
	}
    
    private void generateOpenFrame(SparseMatrix<Block> grid, HullParameters params)
    {
    	Set<Point3i> done = new HashSet<Point3i>();
    	List<Point3i> todo = new ArrayList<Point3i>();
    	Point3i center = new Point3i(params.getCenterX(), params.getCenterY(), params.getCenterZ());
    	todo.add(new Point3i(0,0,0));
    	for (int i = params.getVolume()/(8*8*8); i > 0; i--)
    	{
    		int idx = mRND.nextInt(todo.size());
    		Point3i p = todo.get(idx);
    		todo.remove(idx);
    		addFrame(grid, p, center);
    		done.add(p);
    		addFrameLink(grid, done, todo, p, center, 1, 0, 0);
    		addFrameLink(grid, done, todo, p, center, -1, 0, 0);
    		addFrameLink(grid, done, todo, p, center, 0, 1, 0);
    		addFrameLink(grid, done, todo, p, center, 0, -1, 0);
    		addFrameLink(grid, done, todo, p, center, 0, 0, 1);
    		addFrameLink(grid, done, todo, p, center, 0, 0, -1);
    	}
    }

	private void addFrameLink(SparseMatrix<Block> grid, Set<Point3i> done, List<Point3i> todo, Point3i p, Point3i center, int dx, int dy, int dz)
	{
		add(done, todo, p, dx, dy, dz);
		Point3i next = new Point3i(p.x + dx, p.y + dy, p.z + dz);
		if (!done.contains(next))
			return;
		Point3i planeX = new Point3i();
		Point3i planeY = new Point3i();
		Point3i axisZ = new Point3i(dx, dy, dz);
		Point3i o = new Point3i(center.x + p.x*12, center.y + p.y*12, center.z + p.z*12);
		if (dx == 0)
		{
			planeX.x = 1;
			if (dy == 0)
				planeY.y = 1;
			else
				planeY.z = 1;
		}
		else
		{
			planeX.y = 1;
			planeY.z = 1;
		}
		if (dx == 1)
			o.x += 7;
		else if (dy == 1)
			o.y += 7;
		else if (dz == 1)
			o.z += 7;
		for (int z = 0; z <= 5; z++)
		{
			int skip = ((z == 0) || (z == 5)) ? 1 : 3;
			int rad = ((z == 0) || (z == 5)) ? 6 : 2;
			short type = ((z == 0) || (z == 5)) ? BlockTypes.HULL_COLOR_BLUE_ID : BlockTypes.HULL_COLOR_WHITE_ID;
			Point3i squareO = new Point3i(o);
			for (int s = 0; s < skip; s++)
			{
				squareO.add(planeX);
				squareO.add(planeY);
			}
			for (int x = 0; x < rad; x++)
			{
				Point3i squareOO = new Point3i(squareO);
				for (int y = 0; y < rad; y++)
				{
					addHull(grid, squareOO, type);
					squareOO.add(planeY);
				}
				squareO.add(planeX);
			}
			o.add(axisZ);
		}
	}

	private void addFrame(SparseMatrix<Block> grid, Point3i p, Point3i center)
	{
		Point3i o = new Point3i(center.x + p.x*12, center.y + p.y*12, center.z + p.z*12);
		for (int i = 0; i < 8; i++)
		{
			addHull(grid, o.x+i, o.y  , o.z  );
			addHull(grid, o.x+i, o.y+7, o.z  );
			addHull(grid, o.x+i, o.y+7, o.z+7);
			addHull(grid, o.x+i, o.y  , o.z+7);
			addHull(grid, o.x  , o.y+i, o.z  );
			addHull(grid, o.x+7, o.y+i, o.z  );
			addHull(grid, o.x+7, o.y+i, o.z+7);
			addHull(grid, o.x  , o.y+i, o.z+7);
			addHull(grid, o.x  , o.y  , o.z+i);
			addHull(grid, o.x+7, o.y  , o.z+i);
			addHull(grid, o.x+7, o.y+7, o.z+i);
			addHull(grid, o.x  , o.y+7, o.z+i);
		}
	}
	
	private void addHull(SparseMatrix<Block> grid, Point3i p)
	{
		addHull(grid, p.x, p.y, p.z);
	}
	
	private void addHull(SparseMatrix<Block> grid, Point3i p, short type)
	{
		addHull(grid, p.x, p.y, p.z, type);
	}
	
	private void addHull(SparseMatrix<Block> grid, int x, int y, int z)
	{
		addHull(grid, x, y, z, BlockTypes.HULL_COLOR_GREY_ID);
	}
	
	private void addHull(SparseMatrix<Block> grid, int x, int y, int z, short type)
	{
		Block b = new Block();
		b.setBlockID(type);
		grid.set(x, y, z, b);
	}
}

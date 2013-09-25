package jo.sm.logic;

import java.util.Iterator;

import jo.sm.data.CubeIterator;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class PluginUtils
{
	public static void getEffectiveSelection(StarMade sm, SparseMatrix<Block> grid, Point3i lower, Point3i upper)
	{
		if ((sm.getSelectedLower() != null) && (sm.getSelectedUpper() != null))
		{
			lower.set(sm.getSelectedLower());
			upper.set(sm.getSelectedUpper());
		}
		else
			grid.getBounds(lower, upper);
	}
	
	public static Iterator<Point3i> getEffectiveSelectionIterator(StarMade sm, SparseMatrix<Block> grid)
	{
		Point3i lower = new Point3i();
		Point3i upper = new Point3i();
		getEffectiveSelection(sm, grid, lower, upper);
		return new CubeIterator(lower, upper);
	}
}

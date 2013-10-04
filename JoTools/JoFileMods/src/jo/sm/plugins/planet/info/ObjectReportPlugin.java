package jo.sm.plugins.planet.info;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class ObjectReportPlugin implements IBlocksPlugin
{
    public static final String NAME = "Object Report";
    public static final String DESC = "Report on object";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT, 95 },
        { TYPE_STATION, SUBTYPE_EDIT, 95 },
        { TYPE_SHOP, SUBTYPE_EDIT, 95 },
        { TYPE_FLOATINGROCK, SUBTYPE_EDIT, 95 },
        { TYPE_PLANET, SUBTYPE_EDIT, 95 },
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
        return null;
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
        try
        {
            File repFile = File.createTempFile("smReport", ".txt");
            PrintWriter wtr = new PrintWriter(repFile);
            reportBasics(wtr, original);
            wtr.close();
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().open(repFile);
        }
        catch (IOException e)
        {
            cb.setError(e);
        }
        return null;
    }

    private void reportBasics(PrintWriter wtr, SparseMatrix<Block> grid)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        grid.getBounds(lower, upper);
        int gridSize = grid.size();
        wtr.println("Bounds: "+lower+" -- "+upper);
        wtr.println("Mass  : "+gridSize);
        List<Point3i> core = new ArrayList<Point3i>();
        Map<Short,Integer> quantities = new HashMap<Short, Integer>();
        for (Iterator<Point3i> i = grid.iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            Block b = grid.get(p);
            Integer count = quantities.get(b.getBlockID());
            if (count == null)
                quantities.put(b.getBlockID(), 1);
            else
                quantities.put(b.getBlockID(), count + 1);
            if (b.getBlockID() == BlockTypes.CORE_ID)
                core.add(p);
        }
        if (core.size() == 0)
            wtr.println("Core  : none");
        else for (Point3i p : core)
            wtr.println("Core  : "+p);
        wtr.println("Breakdown:");
        Short[] ids = quantities.keySet().toArray(new Short[0]);
        Arrays.sort(ids);
        for (short id : ids)
        {
            int q = quantities.get(id);
            int pc = q*100/gridSize;
            wtr.print(StringUtils.prefix(Integer.toString(q), ' ', 5));
            if (pc > 0)
                wtr.print(" "+StringUtils.prefix(Integer.toString(pc), ' ', 3)+"%");
            else
                wtr.print("     ");
            wtr.print(" "+BlockTypes.BLOCK_NAMES.get(id));
            wtr.println();
        }
    }
}

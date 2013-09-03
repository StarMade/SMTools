package jo.sm.plugins.ship.fill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.ShipLogic;
import jo.vecmath.Point3i;

public class FillPlugin implements IBlocksPlugin
{
    public static final String NAME = "Fill";
    public static final String DESC = "Autofill Ship Interior";
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
        return new FillParameters();
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
        FillParameters params = (FillParameters)p;    
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        List<Point3i> interior = new ArrayList<Point3i>();
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        original.getBounds(lower, upper);
        scopeInterior(original, modified, interior, lower, upper);
        int interiorSize = interior.size();
        int oneHundredPercent = params.getEmpty() + params.getMissileDumb() + params.getMissileFafo()
                + params.getMissileHeat() + params.getPower() + params.getSalvage()
                + params.getShield() + params.getThrusters() + params.getWeapon();
        fill(modified, interior, params.getThrusters()*interiorSize/oneHundredPercent, (short)-1, BlockTypes.THRUSTER_ID,
                new FillStrategy(FillStrategy.MINUS, FillStrategy.Z, lower, upper));
        fill(modified, interior, params.getMissileDumb()*interiorSize/oneHundredPercent, BlockTypes.MISSILE_DUMB_CONTROLLER_ID, BlockTypes.MISSILE_DUMB_ID,
                new FillStrategy(FillStrategy.PLUS, FillStrategy.Z, lower, upper));
        fill(modified, interior, params.getMissileHeat()*interiorSize/oneHundredPercent, BlockTypes.MISSILE_HEAT_CONTROLLER_ID, BlockTypes.MISSILE_HEAT_ID,
                new FillStrategy(FillStrategy.PLUS, FillStrategy.Z, lower, upper));
        fill(modified, interior, params.getMissileFafo()*interiorSize/oneHundredPercent, BlockTypes.MISSILE_FAFO_CONTROLLER_ID, BlockTypes.MISSILE_FAFO_ID,
                new FillStrategy(FillStrategy.PLUS, FillStrategy.Z, lower, upper));
        fill(modified, interior, params.getWeapon()*interiorSize/oneHundredPercent, BlockTypes.WEAPON_CONTROLLER_ID, BlockTypes.WEAPON_ID,
                new FillStrategy(FillStrategy.OUTSIDE, FillStrategy.X, lower, upper));
        fill(modified, interior, params.getSalvage()*interiorSize/oneHundredPercent, BlockTypes.SALVAGE_CONTROLLER_ID, BlockTypes.SALVAGE_ID,
                new FillStrategy(FillStrategy.OUTSIDE, FillStrategy.Y, lower, upper));
        fill(modified, interior, params.getPower()*interiorSize/oneHundredPercent, BlockTypes.POWER_COIL_ID, BlockTypes.POWER_ID,
                new FillStrategy(FillStrategy.CENTER, FillStrategy.X|FillStrategy.Y|FillStrategy.Z, lower, upper));
        fill(modified, interior, params.getShield()*interiorSize/oneHundredPercent, (short)-1, BlockTypes.SHIELD_ID,
                new FillStrategy(FillStrategy.OUTSIDE, FillStrategy.X|FillStrategy.Y|FillStrategy.Z, lower, upper));
        
        return modified;
    }

    private void fill(SparseMatrix<Block> modified, List<Point3i> interior,
            int numBlocks, short controllerID, short blockID, FillStrategy fillStrategy)
    {
        if (numBlocks <= 0)
            return;
        Collections.sort(interior, fillStrategy);
        System.out.println("Sorting for "+BlockTypes.BLOCK_NAMES.get(blockID)+": "+interior.get(0)+", "+interior.get(1)+", "+interior.get(2)+", ...");
        if ((controllerID > 0) && (interior.size() > 0))
            place(modified, interior, controllerID);
        while ((numBlocks-- > 0) && (interior.size() > 0))
            place(modified, interior, blockID);
    }
    
    private void place(SparseMatrix<Block> modified, List<Point3i> interior, short blockID)
    {
        Block b = new Block();
        b.setBlockID(blockID);
        Point3i p = interior.get(0);
        interior.remove(0);
        modified.set(p,  b);
    }

    private void scopeInterior(SparseMatrix<Block> original,
            SparseMatrix<Block> modified, List<Point3i> interior,
            Point3i lower, Point3i upper)
    {
        for (int x = lower.x; x <= upper.x; x++)
            for (int y = lower.y; y <= upper.y; y++)
            {
                int bottom = findBottom(original, x, y, lower.z, upper.z);
                int top = findTop(original, x, y, lower.z, upper.z);
                if (bottom > top)
                    continue; // no blocks;
                for (int z = bottom; z <= top; z++)
                {
                    Point3i xyz = new Point3i(x, y, z);
                    if (original.contains(xyz))
                        modified.set(xyz, original.get(xyz));
                    else
                        interior.add(xyz);
                }
            }
        Point3i core = ShipLogic.findCore(modified);
        if (core == null)
        {
            core = new Point3i(8, 8, 8);
            modified.set(core, new Block(BlockTypes.CORE_ID));
        }
        int accessRadius = (int)(Math.pow(interior.size(), .333)/200);
        for (Iterator<Point3i> i = interior.iterator(); i.hasNext(); )
        {
            Point3i p = i.next();
            if ((Math.abs(p.x - core.x) <= accessRadius) || (Math.abs(p.y - core.y) <= accessRadius) || (Math.abs(p.z - core.z) <= accessRadius))
                i.remove();
        }
    }

    private int findBottom(SparseMatrix<Block> grid, int x, int y, int lowZ, int highZ)
    {
        for (int z = lowZ; z <= highZ; z++)
            if (grid.contains(x, y, z))
                return z;
        return highZ + 1;
    }

    private int findTop(SparseMatrix<Block> grid, int x, int y, int lowZ, int highZ)
    {
        for (int z = highZ; z >= lowZ; z--)
            if (grid.contains(x, y, z))
                return z;
        return lowZ - 1;
    }
}

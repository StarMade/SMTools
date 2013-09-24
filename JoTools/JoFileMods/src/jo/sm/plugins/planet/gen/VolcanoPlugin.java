package jo.sm.plugins.planet.gen;

import jo.sm.data.BlockTypes;
import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.logic.MathUtils;

public class VolcanoPlugin implements IBlocksPlugin
{
    public static final String NAME = "Shape/Volcano";
    public static final String DESC = "One single mountain";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_PLANET, SUBTYPE_GENERATE, 26 },
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
        return new VolcanoParameters();
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
        VolcanoParameters params = (VolcanoParameters)p;
        SparseMatrix<Block> modified = new SparseMatrix<Block>();
        fillGrid(modified, params, cb);
        return modified;
    }
    
    private void fillGrid(SparseMatrix<Block> grid, VolcanoParameters params, IPluginCallback cb)
    {
        cb.setStatus("Filling in terrain");
        cb.startTask(params.getPlanetRadius()*2);        
        for (int x = -params.getPlanetRadius(); x <= params.getPlanetRadius(); x++)
        {
            cb.workTask(1);
            for (int y = -params.getPlanetRadius(); y <= params.getPlanetRadius(); y++)
                fillColumn(x, y, grid, params);
        }
        cb.endTask();
    }
    
    private void fillColumn(int x, int y, SparseMatrix<Block> grid, VolcanoParameters params)
    {
        double r = Math.sqrt(x*x + y*y);
        if (r > params.getPlanetRadius())
            return; // out of radius        
        int columnHeight;
        if (r < params.getCalderaRadius())
            columnHeight = (int)MathUtils.interpolate(r, 0, params.getCalderaRadius(), params.getPlanetHeight() - params.getCalderaDepth(), params.getPlanetHeight());
        else
            columnHeight = (int)MathUtils.interpolateCos(r, params.getPlanetRadius(), params.getCalderaRadius(), 0, params.getPlanetHeight());
        int columnDepth;
        if (params.isRoundedBottom())
            columnDepth = (int)MathUtils.interpolateCos(r, 0, params.getPlanetRadius(), -params.getPlanetDepth(), 0);
        else
            columnDepth = -params.getPlanetDepth();
        for (int z = columnDepth; z <= columnHeight; z++)
        {
            Block b = new Block(BlockTypes.TERRAIN_ROCK_ID);
            grid.set(x, z, y, b);
        }
    }
}

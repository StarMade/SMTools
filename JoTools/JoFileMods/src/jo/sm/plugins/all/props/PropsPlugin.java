package jo.sm.plugins.all.props;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

public class PropsPlugin implements IBlocksPlugin
{
    public static final String NAME = "Properties...";
    public static final String DESC = "System Proeprties";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_ALL, SUBTYPE_FILE, 99 },
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
        return new PropsParameters();
    }
	@Override
	public void initParameterBean(SparseMatrix<Block> original, Object params,
			StarMade sm, IPluginCallback cb)
	{
		PropsParameters p = (PropsParameters)params;
		p.setInvertXAxis(StarMadeLogic.isProperty(StarMadeLogic.INVERT_X_AXIS));
		p.setInvertYAxis(StarMadeLogic.isProperty(StarMadeLogic.INVERT_Y_AXIS));
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
        PropsParameters params = (PropsParameters)p;
        StarMadeLogic.setProperty(StarMadeLogic.INVERT_X_AXIS, params.isInvertXAxis());
        StarMadeLogic.setProperty(StarMadeLogic.INVERT_Y_AXIS, params.isInvertYAxis());
        return null;
    }
}

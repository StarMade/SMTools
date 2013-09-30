package jo.sm.plugins.planet.select;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.GridLogic;
import jo.sm.logic.PluginUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

public class SelectCopyPlugin implements IBlocksPlugin, ClipboardOwner
{
    public static final String NAME = "Copy";
    public static final String DESC = "Copy selection to clipboard";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT, 13 },
        { TYPE_STATION, SUBTYPE_EDIT, 13 },
        { TYPE_SHOP, SUBTYPE_EDIT, 13 },
        { TYPE_FLOATINGROCK, SUBTYPE_EDIT, 13 },
        { TYPE_PLANET, SUBTYPE_EDIT, 13 },
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
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        PluginUtils.getEffectiveSelection(sm, original, lower, upper);
        SparseMatrix<Block> clip = GridLogic.extract(original, lower, upper);
        String xml = GridLogic.toString(clip);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(xml), this);
        return null;
    }

    @Override
    public void lostOwnership(Clipboard cb, Transferable t)
    {
    }
}

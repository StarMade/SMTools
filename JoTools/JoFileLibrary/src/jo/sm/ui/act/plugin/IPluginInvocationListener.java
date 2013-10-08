package jo.sm.ui.act.plugin;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

public interface IPluginInvocationListener
{
    public void pluginInvoked(IBlocksPlugin plugin, SparseMatrix<Block> original, Object params, StarMade sm, IPluginCallback cb);
}

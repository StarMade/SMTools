package jo.sm.ui.act.plugin;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import jo.sm.data.SparseMatrix;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.sm.ui.RenderPanel;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class BlocksPluginAction extends GenericAction
{
    private IBlocksPlugin mPlugin;
    private RenderPanel mPanel;
    
    public BlocksPluginAction(RenderPanel panel, IBlocksPlugin plugin)
    {
        mPanel = panel;
        mPlugin = plugin;
        setName(mPlugin.getName());
        setToolTipText(mPlugin.getDescription());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Object params = mPlugin.getParameterBean();
        if (!getParams(params))
            return;
        SparseMatrix<Block> original = mPanel.getGrid();
        SparseMatrix<Block> modified = mPlugin.modify(original, params);
        if (modified != null)
            mPanel.setGrid(modified);
    }

    private boolean getParams(Object params)
    {
        if (params == null)
            return true;
        BeanEditDlg dlg = new BeanEditDlg(getFrame(), params);
        dlg.setVisible(true);
        params = dlg.getBean();
        return params != null;
    }

    private JFrame getFrame()
    {
        for (Component c = mPanel; c != null; c = c.getParent())
            if (c instanceof JFrame)
                return (JFrame)c;
        return null;
    }
}

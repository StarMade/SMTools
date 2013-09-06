package jo.sm.ui.act.plugin;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Properties;

import javax.swing.JFrame;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.ConvertLogic;
import jo.sm.logic.StarMadeLogic;
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
        String name = mPlugin.getName();
        int o = name.indexOf('/');
        if (o >= 0)
            name = name.substring(o + 1);
        setName(name);
        setToolTipText(mPlugin.getDescription());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        final Object params = mPlugin.getParameterBean();
        loadProps(params);
        if (!getParams(params))
            return;
        saveProps(params);
        final PluginProgressDlg progress = new PluginProgressDlg(getFrame(), mPlugin.getName());
        Thread t = new Thread(mPlugin.getName()) { public void run() {
            SparseMatrix<Block> original = mPanel.getGrid();
            mPanel.getUndoer().checkpoint(original);
            SparseMatrix<Block> modified = mPlugin.modify(original, params, StarMadeLogic.getInstance(), progress);
            if (!progress.isPleaseCancel())
            {
                if (modified != null)
                {
                    mPanel.setGrid(modified);
                }
                else
                    mPanel.updateTiles();
            }
            progress.dispose();
        }
        };
        t.start();
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
        }
        if (t.isAlive())
        {
            progress.setVisible(true);
            try
            {
                t.join();
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    private void saveProps(Object params)
    {
        if (params == null)
            return;
        Properties props = StarMadeLogic.getProps();
        String prefix = params.getClass().getName()+"$";
        try
        {
            BeanInfo info = Introspector.getBeanInfo(params.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors())
            {
                if ((pd.getReadMethod() == null) || (pd.getWriteMethod() == null))
                    continue;
                try
                {
                    Object val = pd.getReadMethod().invoke(params);
                    if (val != null)
                        props.setProperty(prefix + pd.getName(), val.toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        StarMadeLogic.saveProps();
    }

    private void loadProps(Object params)
    {
        if (params == null)
            return;
        Properties props = StarMadeLogic.getProps();
        String prefix = params.getClass().getName()+"$";
        try
        {
            BeanInfo info = Introspector.getBeanInfo(params.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors())
            {
                if ((pd.getReadMethod() == null) || (pd.getWriteMethod() == null))
                    continue;
                if (!props.containsKey(prefix + pd.getName()))
                    continue;
                String val = props.getProperty(prefix + pd.getName());
                try
                {
                    pd.getWriteMethod().invoke(params, ConvertLogic.toObject(val, pd.getPropertyType()));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
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

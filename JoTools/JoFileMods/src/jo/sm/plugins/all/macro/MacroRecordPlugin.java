package jo.sm.plugins.all.macro;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.File;
import java.io.IOException;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.logic.macro.MacroFunctionOpLogic;
import jo.sm.logic.utils.FileUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ui.act.plugin.BlocksPluginAction;
import jo.sm.ui.act.plugin.DescribedBeanInfo;
import jo.sm.ui.act.plugin.IPluginInvocationListener;

public class MacroRecordPlugin implements IBlocksPlugin, IPluginInvocationListener
{
    public static final String NAME_START = "Macro/Record Macro...";
    public static final String NAME_STOP = "Macro/Stop Recording Macro";
    public static final String DESC = "Record a macro to a file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_ALL, SUBTYPE_EDIT, 91 },
        };
    
    private boolean         mRecording;
    private File            mMacroFile;
    private StringBuffer    mMacro;

    @Override
    public String getName()
    {
        if (mRecording)
            return NAME_STOP;
        else
            return NAME_START;
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
        if (mRecording)
            return null;
        else
            return new MacroRecordParameters();
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
            if (mRecording)
                stopRecording(sm);
            else
                startRecording(sm, (MacroRecordParameters)p);
        	return null;
        }
        catch (Exception e)
        {
            cb.setError(e);
            return null;
        }
    }

    private void stopRecording(StarMade sm) throws IOException
    {
        mRecording = false;
        BlocksPluginAction.removePluginInvocationListener(this);
        if (mMacro.length() == 0)
        {
            sm.setStatusMessage("No commands issued to record.");
            return;
        }
        mMacro.append("grid\n"); // final return value
        if (!mMacroFile.getParentFile().exists())
            mMacroFile.getParentFile().mkdirs();
        FileUtils.writeFile(mMacro.toString(), mMacroFile);
        sm.setStatusMessage("Finished recording macro in "+mMacroFile);
    }

    private void startRecording(StarMade sm, MacroRecordParameters p)
    {
        mMacroFile = new File(p.getFile());
        mRecording = true;
        BlocksPluginAction.addPluginInvocationListener(this);
        sm.setStatusMessage("Recording macro in "+mMacroFile);
        mMacro = new StringBuffer();
    }

    @Override
    public void pluginInvoked(IBlocksPlugin plugin,
            SparseMatrix<Block> original, Object params, StarMade sm,
            IPluginCallback cb)
    {
        if (plugin == this)
            return;
        mMacro.append("grid = @");
        mMacro.append(MacroFunctionOpLogic.getID(plugin));
        mMacro.append("(grid");
        if (params != null)
        {
            DescribedBeanInfo info = new DescribedBeanInfo(params);
            for (PropertyDescriptor prop : info.getOrderedProps())
            {
                mMacro.append(", ");
                PropertyEditor edit = info.getEditors().get(prop.getName());
                Object val = edit.getValue();
                if (val == null)
                    mMacro.append("null");
                else
                {
                    if (val instanceof String)
                    {
                        mMacro.append("\"");
                        mMacro.append(((String)val).replace("\"", "\\\""));
                        mMacro.append("\"");
                    }
                    else if ((val instanceof Integer) || (val instanceof Boolean))
                    {
                        mMacro.append(val.toString());
                    }
                    else
                    {
                        mMacro.append("\"");
                        String sval = edit.getAsText();
                        mMacro.append(sval);
                        mMacro.append("\"");
                    }
                }
            }
        }
        mMacro.append(");\n");
    }
}

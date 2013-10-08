package jo.sm.factories.all.macro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.FileUtils;
import jo.sm.mods.IStarMadePlugin;
import jo.sm.mods.IStarMadePluginFactory;

public class MacroFactory implements IStarMadePluginFactory
{
    private long    mLastLoad;
    private File    mMacroDir;
	private List<MacroDefinition>	mDefs = new ArrayList<MacroDefinition>();
	private List<IStarMadePlugin>	mPlugins = new ArrayList<IStarMadePlugin>();
	
	public MacroFactory()
	{
	}

	@Override
	public IStarMadePlugin[] getPlugins()
	{
	    updateDefinitions();
		return mPlugins.toArray(new IStarMadePlugin[0]);
	}
	
	private void updatePlugins()
	{
		for (MacroDefinition fd : mDefs)
			mPlugins.add(new MacroPlugin(fd));
	}
	
	private void updateDefinitions()
	{
	    if (mMacroDir == null)
	    {
	        File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
	        mMacroDir = new File(jo_plugins, "macros");
	    }
	    if (mMacroDir.lastModified() < mLastLoad)
	        return;
	    mDefs.clear();
	    mPlugins.clear();
	    if (!mMacroDir.exists())
	        return;
	    File[] macros = mMacroDir.listFiles();
	    if (macros == null)
	        return;
	    for (File macro : macros)
	        addMacro(macro);
	    updatePlugins();
	}
	
	private void addMacro(File macroFile)
	{
	    try
        {
	        if (!macroFile.getName().endsWith(".js"))
	            return;
            String macroText = FileUtils.readFileAsString(macroFile.toString());
            MacroDefinition def = new MacroDefinition();
            def.setScript(macroFile);
            mDefs.add(def);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
}

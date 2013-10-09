package jo.sm.plugins.all.macro;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.sm.factories.all.macro.MacroPlugin;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.macro.MacroFunctionOpLogic;
import jo.sm.mods.IBlocksPlugin;

public class MacroPropertyEditor extends PropertyEditorSupport
{
    private Map<String,Object>  mKeyToValue;
    private Map<Object,String>  mValueToKey;
    private String[]            mTags;
    
    public MacroPropertyEditor()
    {
        super();
        init();
    }

    public MacroPropertyEditor(Object bean)
    {
        super(bean);
        init();
    }
    
    private void init()
    {
    	mKeyToValue = new HashMap<String, Object>();
    	addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_FILE));
    	addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_EDIT));
    	addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_VIEW));
    	addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_MODIFY));
    	addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_GENERATE));
    	addPlugins(StarMadeLogic.getBlocksPlugins(IBlocksPlugin.TYPE_ALL, IBlocksPlugin.SUBTYPE_PAINT));
    	mTags = mKeyToValue.keySet().toArray(new String[0]);
        mValueToKey = new HashMap<Object, String>();
        for (String key : mTags)
            mValueToKey.put(mKeyToValue.get(key), key);
    }

	private void addPlugins(List<IBlocksPlugin> blocksPlugins)
	{
		for (IBlocksPlugin plugin: blocksPlugins)
			if (plugin instanceof MacroPlugin)
				mKeyToValue.put(plugin.getName(), MacroFunctionOpLogic.getID(plugin));
	}
    @Override
    public String getAsText()
    {
        return mValueToKey.get(getValue());
    }
    
    
    @Override
    public String[] getTags()
    {
        return mTags;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        setValue(mKeyToValue.get(text));
    }
}

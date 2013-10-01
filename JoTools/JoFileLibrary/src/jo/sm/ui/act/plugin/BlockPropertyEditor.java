package jo.sm.ui.act.plugin;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jo.sm.data.BlockTypes;

public class BlockPropertyEditor extends PropertyEditorSupport
{
    private boolean mIncludeAir;
    private Map<Short,String> mNames = new HashMap<Short, String>();

    public BlockPropertyEditor()
    {
        super();
        copyNames();
    }

    public BlockPropertyEditor(Object bean)
    {
        super(bean);
        copyNames();
    }
    
    private void copyNames()
    {
        for (Short key : BlockTypes.BLOCK_NAMES.keySet())
            mNames.put(key, BlockTypes.BLOCK_NAMES.get(key));
    }

    @Override
    public String getAsText()
    {
        Short c = (Short)getValue();
        String txt = mNames.get(c);
        return txt;
    }
    
    
    @Override
    public String[] getTags()
    {
        String[] tags = mNames.values().toArray(new String[0]);
        Arrays.sort(tags);
        return tags;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        for (Short id : mNames.keySet())
            if (text.equals(mNames.get(id)))
            {
                setValue(id);
                return;
            }
    }

    public boolean isIncludeAir()
    {
        return mIncludeAir;
    }

    public void setIncludeAir(boolean includeAir)
    {
        mIncludeAir = includeAir;
        if (mIncludeAir)
            mNames.put((short)0, "Air");
        else
            mNames.remove((short)0);
    }

    public Map<Short, String> getNames()
    {
        return mNames;
    }

    public void setNames(Map<Short, String> names)
    {
        mNames = names;
    }
}

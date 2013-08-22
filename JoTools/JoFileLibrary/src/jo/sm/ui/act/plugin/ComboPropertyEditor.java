package jo.sm.ui.act.plugin;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.Map;

public class ComboPropertyEditor extends PropertyEditorSupport
{
    private Map<String,Object>  mKeyToValue;
    private Map<Object,String>  mValueToKey;
    private String[]            mTags;
    
    public ComboPropertyEditor(Map<String,Object> keyToValue)
    {
        super();
        mKeyToValue = keyToValue;
        init();
    }

    public ComboPropertyEditor(Object bean, Map<String,Object> keyToValue)
    {
        super(bean);
        mKeyToValue = keyToValue;
        init();
    }
    
    private void init()
    {
        mTags = mKeyToValue.keySet().toArray(new String[0]);
        mValueToKey = new HashMap<Object, String>();
        for (String key : mTags)
            mValueToKey.put(mKeyToValue.get(key), key);
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

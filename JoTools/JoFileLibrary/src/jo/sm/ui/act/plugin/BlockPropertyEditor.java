package jo.sm.ui.act.plugin;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

import jo.sm.data.BlockTypes;

public class BlockPropertyEditor extends PropertyEditorSupport
{
    public BlockPropertyEditor()
    {
        super();
    }

    public BlockPropertyEditor(Object bean)
    {
        super(bean);
    }

    @Override
    public String getAsText()
    {
        Short c = (Short)getValue();
        String txt = BlockTypes.BLOCK_NAMES.get(c);
        return txt;
    }
    
    
    @Override
    public String[] getTags()
    {
        String[] tags = BlockTypes.BLOCK_NAMES.values().toArray(new String[0]);
        Arrays.sort(tags);
        return tags;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        for (Short id : BlockTypes.BLOCK_NAMES.keySet())
            if (text.equals(BlockTypes.BLOCK_NAMES.get(id)))
            {
                setValue(id);
                return;
            }
    }
}

package jo.sm.ui.act.plugin;

import java.awt.Component;
import java.beans.PropertyEditorSupport;

public class FilePropertyEditor extends PropertyEditorSupport
{
	private FilePropertyInfo	mInfo;
	private FilePropertyPanel	mPanel;
	
    public FilePropertyEditor(FilePropertyInfo info)
    {
        super();
        mInfo = info;
    }

    public FilePropertyEditor(Object bean, FilePropertyInfo info)
    {
        super(bean);
        mInfo = info;
    }
    @Override
    public boolean isPaintable()
    {
    	return true;
    }
    @Override
    public Component getCustomEditor()
    {
    	mPanel = new FilePropertyPanel(mInfo, this);
    	return mPanel;
    }
    @Override
    public String getAsText()
    {
    	return (String)getValue();
    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
    	setValue(text);
    }
}

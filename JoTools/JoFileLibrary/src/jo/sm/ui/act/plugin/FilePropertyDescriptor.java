package jo.sm.ui.act.plugin;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;

public class FilePropertyDescriptor extends PropertyDescriptor
{
	private FilePropertyInfo mInfo;
	
    public FilePropertyDescriptor(String propertyName, Class<?> beanClass,
            String readMethodName, String writeMethodName, FilePropertyInfo info)
            throws IntrospectionException
    {
        super(propertyName, beanClass, readMethodName, writeMethodName);
        mInfo = info;
    }

    public FilePropertyDescriptor(String propertyName, Class<?> beanClass, FilePropertyInfo info)
            throws IntrospectionException
    {
        super(propertyName, beanClass);
        mInfo = info;
    }

    public FilePropertyDescriptor(String propertyName, Method readMethod,
            Method writeMethod, FilePropertyInfo info) throws IntrospectionException
    {
        super(propertyName, readMethod, writeMethod);
        mInfo = info;
    }

    @Override
    public Class<?> getPropertyEditorClass()
    {
        return FilePropertyEditor.class;
    }
    
    @Override
    public PropertyEditor createPropertyEditor(final Object bean)
    {
        final PropertyEditor pe = new FilePropertyEditor(bean, mInfo);
        try
        {
            pe.setValue(getReadMethod().invoke(bean));
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        pe.addPropertyChangeListener(new PropertyChangeListener() { 
            @Override
            public void propertyChange(PropertyChangeEvent ev)
            {
                try
                {
                    getWriteMethod().invoke(bean, pe.getValue());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return pe;
    }
    
    
}

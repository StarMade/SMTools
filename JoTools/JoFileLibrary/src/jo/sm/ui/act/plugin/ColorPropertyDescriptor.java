package jo.sm.ui.act.plugin;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;

public class ColorPropertyDescriptor extends PropertyDescriptor
{

    public ColorPropertyDescriptor(String propertyName, Class<?> beanClass,
            String readMethodName, String writeMethodName)
            throws IntrospectionException
    {
        super(propertyName, beanClass, readMethodName, writeMethodName);
    }

    public ColorPropertyDescriptor(String propertyName, Class<?> beanClass)
            throws IntrospectionException
    {
        super(propertyName, beanClass);
    }

    public ColorPropertyDescriptor(String propertyName, Method readMethod,
            Method writeMethod) throws IntrospectionException
    {
        super(propertyName, readMethod, writeMethod);
    }

    @Override
    public Class<?> getPropertyEditorClass()
    {
        return ColorPropertyEditor.class;
    }
    
    @Override
    public PropertyEditor createPropertyEditor(final Object bean)
    {
        final PropertyEditor pe = new ColorPropertyEditor(bean);
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

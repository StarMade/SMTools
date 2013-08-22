package jo.sm.ui.act.plugin;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;
import java.util.Map;

public class ComboPropertyDescriptor extends PropertyDescriptor
{
    private Map<String,Object>  mKeyToValue;
    
    public ComboPropertyDescriptor(String propertyName, Class<?> beanClass,
            String readMethodName, String writeMethodName, Map<String,Object> keyToValue)
            throws IntrospectionException
    {
        super(propertyName, beanClass, readMethodName, writeMethodName);
        mKeyToValue = keyToValue;
    }

    public ComboPropertyDescriptor(String propertyName, Class<?> beanClass, Map<String,Object> keyToValue)
            throws IntrospectionException
    {
        super(propertyName, beanClass);
        mKeyToValue = keyToValue;
    }

    public ComboPropertyDescriptor(String propertyName, Method readMethod,
            Method writeMethod, Map<String,Object> keyToValue) throws IntrospectionException
    {
        super(propertyName, readMethod, writeMethod);
        mKeyToValue = keyToValue;
    }

    @Override
    public Class<?> getPropertyEditorClass()
    {
        return ComboPropertyEditor.class;
    }
    
    @Override
    public PropertyEditor createPropertyEditor(final Object bean)
    {
        final PropertyEditor pe = new ComboPropertyEditor(bean, mKeyToValue);
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

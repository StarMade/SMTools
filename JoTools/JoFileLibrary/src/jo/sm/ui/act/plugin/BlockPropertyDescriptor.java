package jo.sm.ui.act.plugin;

import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Method;

public class BlockPropertyDescriptor extends PropertyDescriptor
{
    private boolean mIncludeAir;

    public BlockPropertyDescriptor(String propertyName, Class<?> beanClass,
            String readMethodName, String writeMethodName)
            throws IntrospectionException
    {
        super(propertyName, beanClass, readMethodName, writeMethodName);
    }

    public BlockPropertyDescriptor(String propertyName, Class<?> beanClass)
            throws IntrospectionException
    {
        super(propertyName, beanClass);
    }

    public BlockPropertyDescriptor(String propertyName, Method readMethod,
            Method writeMethod) throws IntrospectionException
    {
        super(propertyName, readMethod, writeMethod);
    }

    @Override
    public Class<?> getPropertyEditorClass()
    {
        return BlockPropertyEditor.class;
    }
    
    @Override
    public PropertyEditor createPropertyEditor(final Object bean)
    {
        final PropertyEditor pe = new BlockPropertyEditor(bean);
        ((BlockPropertyEditor)pe).setIncludeAir(mIncludeAir);
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

    public boolean isIncludeAir()
    {
        return mIncludeAir;
    }

    public void setIncludeAir(boolean includeAir)
    {
        mIncludeAir = includeAir;
    }
    
    
}

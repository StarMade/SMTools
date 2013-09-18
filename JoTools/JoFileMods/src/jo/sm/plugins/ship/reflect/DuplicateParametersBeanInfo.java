package jo.sm.plugins.ship.reflect;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import jo.sm.ui.act.plugin.ComboPropertyDescriptor;

public class DuplicateParametersBeanInfo implements BeanInfo
{
    private static final Map<String,Object> COMBO_MAP = new HashMap<String, Object>();
    static
    {
        COMBO_MAP.put("Negative", -1);
        COMBO_MAP.put("No reflection", 0);
        COMBO_MAP.put("Positive", 1);

    }
    
    private BeanInfo mRootBeanInfo;
    
    public DuplicateParametersBeanInfo() throws IntrospectionException
    {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(DuplicateParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor[] props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if (props[i].getName().endsWith("eflect"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), COMBO_MAP);
                }
                catch (IntrospectionException e)
                {
                    e.printStackTrace();
                }
        }
        return props;
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo()
    {
        return mRootBeanInfo.getAdditionalBeanInfo();
    }

    @Override
    public BeanDescriptor getBeanDescriptor()
    {
        return mRootBeanInfo.getBeanDescriptor();
    }

    @Override
    public int getDefaultEventIndex()
    {
        return mRootBeanInfo.getDefaultEventIndex();
    }

    @Override
    public int getDefaultPropertyIndex()
    {
        return mRootBeanInfo.getDefaultPropertyIndex();
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors()
    {
        return mRootBeanInfo.getEventSetDescriptors();
    }

    @Override
    public Image getIcon(int flags)
    {
        return mRootBeanInfo.getIcon(flags);
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors()
    {
        return mRootBeanInfo.getMethodDescriptors();
    }
}

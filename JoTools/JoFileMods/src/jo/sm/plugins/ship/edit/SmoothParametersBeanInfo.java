package jo.sm.plugins.ship.edit;

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

import jo.sm.ship.logic.SmoothLogic;
import jo.sm.ui.act.plugin.ComboPropertyDescriptor;

public class SmoothParametersBeanInfo implements BeanInfo
{
    private static final Map<String,Object> SCOPE_MAP = new HashMap<String, Object>();
    static
    {
        SCOPE_MAP.put("Outside", SmoothLogic.EXTERIOR);
        SCOPE_MAP.put("Inside", SmoothLogic.INTERIOR);
        SCOPE_MAP.put("Outside and Inside", SmoothLogic.EVERYWHERE);
    }
    
    private static final Map<String,Object> TYPE_MAP = new HashMap<String, Object>();
    static
    {
        TYPE_MAP.put("Wedges and Corners", SmoothLogic.EVERYTHING);
        TYPE_MAP.put("Wedges", SmoothLogic.WEDGES);
        TYPE_MAP.put("Corners", SmoothLogic.CORNERS);
    }
    
    private BeanInfo mRootBeanInfo;
    
    public SmoothParametersBeanInfo() throws IntrospectionException
    {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(SmoothParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor[] props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if (props[i].getName().endsWith("ype"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), TYPE_MAP);
                }
                catch (IntrospectionException e)
                {
                    e.printStackTrace();
                }
            else if (props[i].getName().endsWith("cope"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), SCOPE_MAP);
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

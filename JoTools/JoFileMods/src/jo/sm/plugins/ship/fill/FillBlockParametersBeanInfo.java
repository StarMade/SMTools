package jo.sm.plugins.ship.fill;

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

import jo.sm.ui.act.plugin.BlockPropertyDescriptor;
import jo.sm.ui.act.plugin.ComboPropertyDescriptor;

public class FillBlockParametersBeanInfo implements BeanInfo
{
    private static final Map<String,Object> AXIS_MAP = new HashMap<String, Object>();
    static
    {
        AXIS_MAP.put("Port/Starboard", FillStrategy.X);
        AXIS_MAP.put("Dorsal/Ventral", FillStrategy.Y);
        AXIS_MAP.put("Fore/Aft", FillStrategy.Z);
    }
    
    private static final Map<String,Object> STRATEGY_MAP = new HashMap<String, Object>();
    static
    {
        STRATEGY_MAP.put("Center to Perimiter", FillStrategy.CENTER);
        STRATEGY_MAP.put("Low to High", FillStrategy.MINUS);
        STRATEGY_MAP.put("Outside in", FillStrategy.OUTSIDE);
        STRATEGY_MAP.put("High to Low", FillStrategy.PLUS);
    }
    
    private BeanInfo mRootBeanInfo;
    
    public FillBlockParametersBeanInfo() throws IntrospectionException
    {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(FillBlockParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor[] props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if (props[i].getName().endsWith("lockID"))
                try
                {
                    props[i] = new BlockPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod());
                }
                catch (IntrospectionException e)
                {
                    e.printStackTrace();
                }
            else if (props[i].getName().endsWith("rategy"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), STRATEGY_MAP);
                }
                catch (IntrospectionException e)
                {
                    e.printStackTrace();
                }
            else if (props[i].getName().endsWith("xis"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), AXIS_MAP);
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

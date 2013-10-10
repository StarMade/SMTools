package jo.sm.plugins.ship.hull;

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

public class HullParametersBeanInfo implements BeanInfo
{
    private static final Map<String,Object> COMBO_MAP = new HashMap<String, Object>();
    static
    {
        COMBO_MAP.put("Open Frame", HullParameters.OPEN_FRAME);
        COMBO_MAP.put("Needle", HullParameters.NEEDLE);
        COMBO_MAP.put("Cone", HullParameters.CONE);
        COMBO_MAP.put("Cylinder", HullParameters.CYLINDER);
        COMBO_MAP.put("Box", HullParameters.BOX);
        COMBO_MAP.put("Sphere", HullParameters.SPHERE);
        COMBO_MAP.put("Hemisphere", HullParameters.HEMISPHERE);
        COMBO_MAP.put("Disc", HullParameters.DISC);
        COMBO_MAP.put("Irregular", HullParameters.IRREGULAR);
        COMBO_MAP.put("Torus", HullParameters.TORUS);
    }
    
    private BeanInfo mRootBeanInfo;
    
    public HullParametersBeanInfo() throws IntrospectionException
    {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(HullParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
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

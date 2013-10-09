package jo.sm.plugins.all.macro;

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

public class MacroRecordParametersBeanInfo implements BeanInfo
{
	private static final Map<String,Object> PLACEMENT_MAP = new HashMap<String, Object>();
	static
	{
		PLACEMENT_MAP.put("File", "FILE");
		PLACEMENT_MAP.put("Edit", "EDIT");
		PLACEMENT_MAP.put("File", "VIEW");
		PLACEMENT_MAP.put("Generate", "GENERATE");
		PLACEMENT_MAP.put("Modify", "MODIFY");
		PLACEMENT_MAP.put("Paint", "PAINT");
	}
	private static final Map<String,Object> ENABLEMENT_MAP = new HashMap<String, Object>();
	static
	{
		ENABLEMENT_MAP.put("All", "ALL");
		ENABLEMENT_MAP.put("Ship", "SHIP");
		ENABLEMENT_MAP.put("Station", "STATION");
		ENABLEMENT_MAP.put("Shop", "SHOP");
		ENABLEMENT_MAP.put("Floating Rock", "FLOATINGROCK");
		ENABLEMENT_MAP.put("Planet", "PLANET");
	}
    private BeanInfo mRootBeanInfo;
    
    public MacroRecordParametersBeanInfo() throws IntrospectionException
    {
        super();
        mRootBeanInfo = Introspector.getBeanInfo(MacroRecordParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor[] props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if (props[i].getName().endsWith("acement"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), PLACEMENT_MAP);
                }
                catch (IntrospectionException e)
                {
                    e.printStackTrace();
                }
            else if (props[i].getName().endsWith("nablement"))
                try
                {
                    props[i] = new ComboPropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), ENABLEMENT_MAP);
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

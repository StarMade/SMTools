package jo.sm.plugins.all.macro;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import javax.swing.JFileChooser;

import jo.sm.ui.act.plugin.FilePropertyDescriptor;
import jo.sm.ui.act.plugin.FilePropertyInfo;

public class MacroRecordParametersBeanInfo implements BeanInfo
{
    private BeanInfo mRootBeanInfo;
    private FilePropertyInfo	mInfo;
    
    public MacroRecordParametersBeanInfo() throws IntrospectionException
    {
        super();
        mInfo = new FilePropertyInfo();
        mInfo.setDialogTitle("Save Javascript macro");
        mInfo.setFilters(new String[][]{
        		{ "Javascript macro", "js" },
        });
        mInfo.setDialogType(JFileChooser.SAVE_DIALOG);
        mInfo.setApproveButtonText("Save");
        mInfo.setApproveButtonTooltipText("Select file to save");
        mRootBeanInfo = Introspector.getBeanInfo(MacroRecordParameters.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor[] props = mRootBeanInfo.getPropertyDescriptors();
        for (int i = 0; i < props.length; i++)
        {
            if (props[i].getName().endsWith("ile"))
                try
                {
                    props[i] = new FilePropertyDescriptor(props[i].getName(), 
                            props[i].getReadMethod(), props[i].getWriteMethod(), mInfo);
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

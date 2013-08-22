package jo.sm.ui.act.plugin;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

public class GenericPropertyEditor implements PropertyEditor
{
    private Object              mBean;
    private PropertyDescriptor  mDescriptor;

    public GenericPropertyEditor(Object bean,
            PropertyDescriptor propertyDescriptor)
    {
        mBean = bean;
        mDescriptor = propertyDescriptor;
        if (!mDescriptor.getPropertyType().isPrimitive())
            throw new IllegalArgumentException("Cannot handle editing type '"+mDescriptor.getPropertyType().getName()+"'");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public String getAsText()
    {
        Object oVal = getValue();
        if (oVal != null)
            return oVal.toString();
        return null;
    }

    @Override
    public Component getCustomEditor()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getJavaInitializationString()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getTags()
    {
        if (mDescriptor.getPropertyType() == boolean.class)
            return new String[] { "false", "true" };
        return null;
    }

    @Override
    public Object getValue()
    {
        try
        {
            if (mDescriptor.getReadMethod() != null)
                return mDescriptor.getReadMethod().invoke(mBean);
            else
                return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isPaintable()
    {
        return false;
    }

    @Override
    public void paintValue(Graphics g, Rectangle rect)
    {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setAsText(String sVal) throws IllegalArgumentException
    {
        Object oVal = null;
        if (sVal != null)
        {
            if (mDescriptor.getPropertyType() == boolean.class)
                try
                {
                    oVal = Boolean.parseBoolean(sVal);
                }
                catch (Exception e)
                {
                    oVal = Boolean.FALSE;
                }
            else if (mDescriptor.getPropertyType() == byte.class)
                try
                {
                    oVal = Byte.parseByte(sVal);
                }
                catch (Exception e)
                {
                    oVal = (byte)0;
                }
            else if (mDescriptor.getPropertyType() == short.class)
                try
                {
                    oVal = Short.parseShort(sVal);
                }
                catch (Exception e)
                {
                    oVal = (short)0;
                }
            else if (mDescriptor.getPropertyType() == int.class)
                try
                {
                    oVal = Integer.parseInt(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0;
                }
            else if (mDescriptor.getPropertyType() == long.class)
                try
                {
                    oVal = Long.parseLong(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0L;
                }
            else if (mDescriptor.getPropertyType() == float.class)
                try
                {
                    oVal = Float.parseFloat(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0f;
                }
            else if (mDescriptor.getPropertyType() == double.class)
                try
                {
                    oVal = Double.parseDouble(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0.0;
                }
            else if (mDescriptor.getPropertyType() == char.class)
            {
                if (sVal.trim().length() > 0)
                    oVal = sVal.trim().charAt(0);
            }
            else if (mDescriptor.getPropertyType() == String.class)
                oVal = sVal;
            else
                throw new IllegalArgumentException("Cannot handle editing type '"+mDescriptor.getPropertyType().getName()+"'");
        }
        setValue(oVal);
    }

    @Override
    public void setValue(Object val)
    {
        if (mDescriptor.getWriteMethod() != null)
            try
            {
                mDescriptor.getWriteMethod().invoke(mBean, val);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    @Override
    public boolean supportsCustomEditor()
    {
        return false;
    }

}

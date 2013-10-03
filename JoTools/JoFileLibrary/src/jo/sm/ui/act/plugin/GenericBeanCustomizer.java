package jo.sm.ui.act.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.BeanInfo;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import jo.sm.logic.utils.StringUtils;
import jo.sm.ui.utils.SpringUtilities;

@SuppressWarnings("serial")
public class GenericBeanCustomizer extends JPanel implements Customizer
{
    private Object mBean;
    private Class<?> mBeanClass;
    private BeanInfo mBeanInfo;
    
    private PropertyDescriptor[] mProps;
    private PropertyEditor[]    mEditors;
    private Component[]         mControls;

    @Override
    public void setObject(Object bean)
    {
        mBean = bean;
        mBeanClass = mBean.getClass();
        try
        {
            mBeanInfo = Introspector.getBeanInfo(mBeanClass);
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
            return;
        }
        List<PropertyDescriptor> props = new ArrayList<PropertyDescriptor>();
        for (PropertyDescriptor prop : mBeanInfo.getPropertyDescriptors())
            if (prop.getReadMethod() == null || prop.getWriteMethod() == null)
                continue;
            else
            {
                try
                {
                    Description d = findDescription(mBeanClass, prop);
                    if (d != null)
                    {
                        if (StringUtils.nonTrivial(d.displayName()))
                            prop.setDisplayName(d.displayName());
                        if (StringUtils.nonTrivial(d.shortDescription()))
                            prop.setShortDescription(d.shortDescription());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                props.add(prop);
            }
        mProps = props.toArray(new PropertyDescriptor[0]);
        
        createUI();
    }

    private Description findDescription(Class<?> beanClass,
            PropertyDescriptor prop)
    {
        String propName = prop.getName();
        Field match= null;
        for (Field f : beanClass.getDeclaredFields())
        {
            String fieldName = f.getName();
            if (propName.equalsIgnoreCase(fieldName))
            {
                match = f;
                break;
            }
            if (fieldName.startsWith("m") && propName.equalsIgnoreCase(fieldName.substring(1)))
            {
                match = f;
                break;
            }
        }
        if (match == null)
            return null;
        return match.getAnnotation(Description.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void createUI()
    {        
        mEditors = new PropertyEditor[mProps.length];
        mControls = new Component[mProps.length];
        setLayout(new GridLayout(mProps.length, 2));
        setLayout(new SpringLayout());
        int rows = 0;
        for (int i = 0; i < mProps.length; i++)
        {
            mEditors[i] = mProps[i].createPropertyEditor(mBean);
            if (mEditors[i] == null)
                mEditors[i] = new GenericPropertyEditor(mBean, mProps[i]);
            String name = mProps[i].getDisplayName();
            JLabel label = new JLabel(name);
            if (mEditors[i].isPaintable())
                mControls[i] = mEditors[i].getCustomEditor();
            else if (mEditors[i].getTags() != null)
            {
                final PropertyEditor editor = mEditors[i];
                final JComboBox combo = new JComboBox(mEditors[i].getTags());
                combo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev)
                    {
                        String txt = (String)combo.getSelectedItem();
                        editor.setAsText(txt);
                    }
                });
                String txt = mEditors[i].getAsText();
                if (txt != null)
                	combo.setSelectedItem(txt);
                mControls[i] = combo;
            }
            else
            {
                final PropertyEditor editor = mEditors[i];
                final JTextField field = new JTextField(mEditors[i].getAsText());
                field.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e)
                    {
                        String txt = field.getText();
                        editor.setAsText(txt);
                    }
                });
                mControls[i] = field;
            }
            label.setLabelFor(mControls[i]);
            add(label);
            add(mControls[i]);
            rows++;
            String desc = mProps[i].getShortDescription();
            if (StringUtils.nonTrivial(desc) && !name.equals(desc))
            {
                JLabel spacer = new JLabel();
                JLabel description = new JLabel(desc);
                description.setForeground(Color.DARK_GRAY);
                description.setFont(new Font("Dialog", Font.PLAIN, 9));
                spacer.setLabelFor(description);
                add(spacer);
                add(description);
                rows++;
            }
        }
        SpringUtilities.makeCompactGrid(this, rows, 2, 6, 6, 6, 6);
    }
}

package jo.sm.ui.act.plugin;

import java.awt.Component;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
                props.add(prop);
        mProps = props.toArray(new PropertyDescriptor[0]);
        createUI();
    }

    private void createUI()
    {        
        mEditors = new PropertyEditor[mProps.length];
        mControls = new Component[mProps.length];
        setLayout(new GridLayout(mProps.length, 2));
        for (int i = 0; i < mProps.length; i++)
        {
            mEditors[i] = mProps[i].createPropertyEditor(mBean);
            if (mEditors[i] == null)
                mEditors[i] = new GenericPropertyEditor(mBean, mProps[i]);
            add(new JLabel(mProps[i].getDisplayName()));
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
                combo.setSelectedItem(mEditors[i].getAsText());
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
            add(mControls[i]);
        }
    }
}

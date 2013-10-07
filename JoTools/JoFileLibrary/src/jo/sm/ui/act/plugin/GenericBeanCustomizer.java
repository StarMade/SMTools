package jo.sm.ui.act.plugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.Customizer;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

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
	private DescribedBeanInfo mInfo;
    
    private Component[]         mControls;

    @Override
    public void setObject(Object bean)
    {
    	mInfo = new DescribedBeanInfo(bean);
        createUI();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void createUI()
    {        
        mControls = new Component[mInfo.getOrderedProps().size()];
        setLayout(new GridLayout(mInfo.getOrderedProps().size(), 2));
        setLayout(new SpringLayout());
        int rows = 0;
        for (int i = 0; i < mInfo.getOrderedProps().size(); i++)
        {
        	PropertyDescriptor prop = mInfo.getOrderedProps().get(i);
        	final PropertyEditor editor = mInfo.getEditors().get(prop.getName());
            String name = prop.getDisplayName();
            JLabel label = new JLabel(name);
            if (editor.isPaintable())
                mControls[i] = editor.getCustomEditor();
            else if (editor.getTags() != null)
            {
                final JComboBox combo = new JComboBox(editor.getTags());
                combo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev)
                    {
                        String txt = (String)combo.getSelectedItem();
                        editor.setAsText(txt);
                    }
                });
                String txt = editor.getAsText();
                if (txt != null)
                	combo.setSelectedItem(txt);
                mControls[i] = combo;
            }
            else
            {
                final JTextField field = new JTextField(editor.getAsText());
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
            String desc = prop.getShortDescription();
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

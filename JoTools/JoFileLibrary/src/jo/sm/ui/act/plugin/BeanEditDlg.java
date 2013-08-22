package jo.sm.ui.act.plugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.Customizer;
import java.beans.IntrospectionException;
import java.beans.Introspector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class BeanEditDlg extends JDialog
{
    private Object mBean;
    private Class<?> mBeanClass;
    private BeanInfo mBeanInfo;
    private Customizer mCustomizer;
    
    public BeanEditDlg(JFrame base, Object bean)
    {
        super(base, "Edit Parameters", Dialog.ModalityType.DOCUMENT_MODAL);
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
        // instantiate
        Class<?> customizerClass = mBeanInfo.getBeanDescriptor().getCustomizerClass();
        if (customizerClass == null)
            customizerClass = GenericBeanCustomizer.class;
        try
        {
            mCustomizer = (Customizer)customizerClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        // layout
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.CENTER, new JScrollPane((Component)mCustomizer));
        JPanel buttonBar = new JPanel();
        client.add(BorderLayout.SOUTH, buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(cancel);
        // link
        ok.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doOK();
            }});
        cancel.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doCancel();
            }});
        
        mCustomizer.setObject(mBean);
        setSize(640, 480);
        setLocationRelativeTo(base);
    }
    
    private void doOK()
    {
        setVisible(false);
        dispose();
    }
    
    private void doCancel()
    {
        mBean = null;
        setVisible(false);
        dispose();
    }

    public Object getBean()
    {
        return mBean;
    }
}

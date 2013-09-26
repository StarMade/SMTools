package jo.sm.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.sm.logic.utils.ResourceUtils;

@SuppressWarnings("serial")
public class DlgAbout extends JDialog
{
	private JEditorPane	mMessage;
	private JScrollPane mScroller;
    
    public DlgAbout(JFrame base)
    {
        super(base, "About SMEdit", Dialog.ModalityType.DOCUMENT_MODAL);
        // instantiate
        mMessage = new JEditorPane();
        mMessage.setContentType("text/html");
        mMessage.setEditable(false);
        try
		{
			mMessage.setText(ResourceUtils.loadSystemResourceString("about.htm", DlgAbout.class));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
        mScroller = new JScrollPane(mMessage);
        JButton ok = new JButton("Close");
        JButton audio = new JButton("Audiobook");
        JButton ebook = new JButton("E-book");
        JButton doc = new JButton("Documentation");
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.NORTH, new JLabel("About SMEdit"));
        client.add(BorderLayout.CENTER, mScroller);
        JPanel buttonBar = new JPanel();
        client.add(BorderLayout.SOUTH, buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(doc);
        buttonBar.add(audio);
        buttonBar.add(ebook);
        // link
        ok.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doOK();
            }});
        doc.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doGoto(BegPanel.DOCUMENTATION);
            }});
        ebook.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doGoto(BegPanel.THE_RAIDERS_LAMENT);
            }            
        });
        audio.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doGoto(BegPanel.THE_RAIDERS_LAMENT_AUDIO);
            }            
        });
        setSize(640, 480);
        setLocationRelativeTo(base);
        Thread t = new Thread() { public void run() {
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            mScroller.getVerticalScrollBar().getModel().setValue(mScroller.getVerticalScrollBar().getModel().getMinimum());
        }};
        t.start();
    }
    
    private void doOK()
    {
        setVisible(false);
        dispose();
    }
    
    private void doGoto(String url)
    {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Action.BROWSE))
                try {
                    desktop.browse(URI.create(url));
                    return;
                } catch (IOException e) {
                    // handled below
                }
        }
    }
}

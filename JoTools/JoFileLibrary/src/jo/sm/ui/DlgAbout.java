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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DlgAbout extends JDialog
{
	private JTextArea	mMessage;
    
    public DlgAbout(JFrame base)
    {
        super(base, "About SMEdit", Dialog.ModalityType.DOCUMENT_MODAL);
        // instantiate
        mMessage = new JTextArea();
        mMessage.setLineWrap(true);
        mMessage.setWrapStyleWord(true);
        mMessage.setEditable(false);
        mMessage.setText(BegPanel.MESSAGE);
        JButton ok = new JButton("OK");
        JButton audio = new JButton("Audiobook");
        JButton ebook = new JButton("E-book");        // layout
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.NORTH, new JLabel("About SMEdit"));
        client.add(BorderLayout.CENTER, new JScrollPane(mMessage));
        JPanel buttonBar = new JPanel();
        client.add(BorderLayout.SOUTH, buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(ok);
        buttonBar.add(audio);
        buttonBar.add(ebook);
        // link
        ok.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doOK();
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

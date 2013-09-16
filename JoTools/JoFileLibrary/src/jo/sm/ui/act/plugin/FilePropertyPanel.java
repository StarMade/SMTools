package jo.sm.ui.act.plugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class FilePropertyPanel extends JPanel
{
	private FilePropertyEditor  mEditor;
	private FilePropertyInfo	mInfo;
	
	private JTextField	mFileName;
	private JButton		mBrowse;
	
	public FilePropertyPanel(FilePropertyInfo info, FilePropertyEditor editor)
	{
		mInfo = info;
		mEditor = editor;
		// instantiate
		mFileName = new JTextField();
		mFileName.setText(mEditor.getAsText());
		mBrowse = new JButton("Browse...");
		//layout
		setLayout(new BorderLayout());
		add("Center", mFileName);
		add("East", mBrowse);
		// link
		mBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev)
			{
				doBrowse();
			}
		});
		mFileName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e)
			{
				mEditor.setAsText(mFileName.getText());
			}
		});
	}
	
	private void doBrowse()
	{
		File base = new File(mFileName.getText());
        JFileChooser chooser = new JFileChooser(base.getParentFile());
        chooser.setSelectedFile(base);
        if ((mInfo.getFilters() != null) && (mInfo.getFilters().length > 0))
        {
        	FileNameExtensionFilter first = null;
	        for (String[] filter : mInfo.getFilters())
	        {
	        	FileNameExtensionFilter f = new FileNameExtensionFilter(
	        			filter[0], filter[1]);
	        	chooser.addChoosableFileFilter(f);
	        	if (first == null)
	        		first = f;
	        }
	        chooser.setFileFilter(first);
        }
        if (mInfo.getApproveButtonText() != null)
        	chooser.setApproveButtonText(mInfo.getApproveButtonText());
        if (mInfo.getApproveButtonTooltipText() != null)
        	chooser.setApproveButtonToolTipText(mInfo.getApproveButtonTooltipText());
        if (mInfo.getDialogTitle() != null)
        	chooser.setDialogTitle(mInfo.getDialogTitle());
        chooser.setDialogType(mInfo.getDialogType());
        chooser.setFileSelectionMode(mInfo.getFileSelectionMode());
        int returnVal = chooser.showOpenDialog(getFrame());
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;
        File smb2 = chooser.getSelectedFile();
        mFileName.setText(smb2.toString());
        mEditor.setAsText(smb2.toString());
	}

    private JFrame getFrame()
    {
        for (Component c = this; c != null; c = c.getParent())
            if (c instanceof JFrame)
                return (JFrame)c;
        return null;
    }
}

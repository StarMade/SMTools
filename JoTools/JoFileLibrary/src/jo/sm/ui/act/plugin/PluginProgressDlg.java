package jo.sm.ui.act.plugin;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IPluginCallback;

@SuppressWarnings("serial")
public class PluginProgressDlg extends JDialog implements IPluginCallback
{
    private JLabel          mMessage;
    private JProgressBar    mProgress;
    private JButton         mCancel;
    
    private String          mErrorTitle;
    private String          mErrorDescription;
    private Throwable       mError;
    
    private int             mTotalUnits;
    private int             mUnitsDone;
    private boolean         mPleaseCancel;
    
    public PluginProgressDlg(JFrame base, String title)
    {
        super(base, title, Dialog.ModalityType.DOCUMENT_MODAL);
        mMessage = new JLabel("");
        mProgress = new JProgressBar(0, 100);
        mProgress.setStringPainted(true);
        mProgress.setVisible(false);
        mCancel = new JButton("Cancel");
        // layout
        JPanel client = new JPanel();
        getContentPane().add(client);
        client.setLayout(new BorderLayout());
        client.add(BorderLayout.NORTH, mMessage);
        client.add(BorderLayout.CENTER, mProgress);
        client.add(BorderLayout.SOUTH, mCancel);
        //links
        mCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                mPleaseCancel = true;
            }
        });
        setSize(320, 240);
        setLocationRelativeTo(base);
    }
    
    private void updateProgress()
    {
        mProgress.setMaximum(mTotalUnits);
        mProgress.setValue(mUnitsDone);        
    }

    @Override
    public void setStatus(String status)
    {
        System.out.println(status);
        mMessage.setText(status);
    }

    @Override
    public void startTask(int size)
    {
        mTotalUnits = size;
        mUnitsDone = 0;
        mProgress.setVisible(true);
        updateProgress();
    }

    @Override
    public void workTask(int amnt)
    {
        mUnitsDone += amnt;
        updateProgress();
        if (mPleaseCancel)
            throw new IllegalStateException("Operation canceled at user request");
    }

    @Override
    public void endTask()
    {
        mUnitsDone = mTotalUnits;
        updateProgress();
        mProgress.setVisible(false);
    }

    public boolean isPleaseCancel()
    {
        return mPleaseCancel;
    }

    public void setPleaseCancel(boolean pleaseCancel)
    {
        mPleaseCancel = pleaseCancel;
    }

    public String getErrorTitle()
    {
        return mErrorTitle;
    }

    public void setErrorTitle(String errorTitle)
    {
        mErrorTitle = errorTitle;
    }

    public String getErrorDescription()
    {
        return mErrorDescription;
    }

    public void setErrorDescription(String errorDescription)
    {
        mErrorDescription = errorDescription;
    }

    public Throwable getError()
    {
        return mError;
    }

    public void setError(Throwable error)
    {
        mError = error;
        if (StringUtils.isTrivial(mErrorTitle))
            setTitle(mError.toString());
    }
}

package jo.sm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jo.sm.logic.StarMadeLogic;

@SuppressWarnings("serial")
public class StatusPanel extends JPanel
{
	private JProgressBar mMemory;
    private JLabel  mStatus;
    private JButton mAbout;
    
    private Color mNormal;
    
    public StatusPanel()
    {
        // instantiate
        mStatus = new JLabel("");
        setBackground(Color.cyan);
        mAbout = new JButton("About");
        mMemory = new JProgressBar(0, (int)(Runtime.getRuntime().maxMemory()/1024L/1024L));
        mMemory.setStringPainted(true);
        mMemory.setIndeterminate(true);
        mNormal = mMemory.getBackground();
        // layout
        setLayout(new BorderLayout());
        add("Center", mStatus);
        add("East", mAbout);
        add("West", mMemory);
        mAbout.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                DlgAbout dlg = new DlgAbout(getFrame());
                dlg.setVisible(true);
            }            
        });
        StarMadeLogic.getInstance().addPropertyChangeListener("statusMessage", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent ev)
			{
				mStatus.setText((String)ev.getNewValue());
			}
		});
        Thread t = new Thread("mem_ticker") { public void run() { doTicker(); } };
        t.setDaemon(true);
        t.start();
    }
    
    private void doTicker()
    {
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        }
        mMemory.setIndeterminate(false);
        for (;;)
        {
        	int free = (int)(Runtime.getRuntime().freeMemory()/1024L/1024L);
        	int total = (int)(Runtime.getRuntime().totalMemory()/1024L/1024L);
        	int max = (int)(Runtime.getRuntime().maxMemory()/1024L/1024L);
        	free +=  (max - total);
            mMemory.setValue(free);
            mMemory.setString(free+"M");
            mMemory.setToolTipText("Free="+free+", max="+max+", total="+total);
            if (free*100/max < 5)
            {
                mMemory.setBackground(Color.red);
                mStatus.setText("LOW ON MEMORY");
            }
            else
            {
                mMemory.setBackground(mNormal);
                mStatus.setText(StarMadeLogic.getInstance().getStatusMessage());
            }
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    private JFrame getFrame()
    {
        for (Component c = this; c != null; c = c.getParent())
            if (c instanceof JFrame)
                return (JFrame)c;
        return null;
    }
}

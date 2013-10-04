package jo.sm.logic;

import javax.swing.JFrame;

import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.act.plugin.PluginProgressDlg;

public class RunnableLogic
{
	public static void run(JFrame frame, String title, final IRunnableWithProgress runnable)
	{
        final PluginProgressDlg progress = new PluginProgressDlg(frame, title);
        Thread t = new Thread(title) { public void run() {
        	runnable.run(progress);
            progress.dispose();
        }
        };
        t.start();
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
        }
        if (t.isAlive() && !progress.isPleaseCancel())
        {
            progress.setVisible(true);
            try
            {
                t.join();
            }
            catch (InterruptedException e)
            {
            }
        }

	}
}

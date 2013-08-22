package jo.sm.ui.act.file;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class QuitAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public QuitAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Exit");
        setToolTipText("Exit Application");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        mFrame.dispose();
    }
 }

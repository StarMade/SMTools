package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class DontDrawAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public DontDrawAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Don't Draw");
        setToolTipText("Save performance by not drawing ship");
        setChecked(mFrame.getClient().isDontDraw());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
    	mFrame.getClient().setDontDraw(!mFrame.getClient().isDontDraw());
        setChecked(!mFrame.getClient().isDontDraw());
    }

}

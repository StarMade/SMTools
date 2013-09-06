package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class PlainAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public PlainAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Plain");
        setToolTipText("Use plain graphics");
        setChecked(mFrame.getClient().isPlainGraphics());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
    	mFrame.getClient().setPlainGraphics(!mFrame.getClient().isPlainGraphics());
        setChecked(!mFrame.getClient().isPlainGraphics());
    }

}

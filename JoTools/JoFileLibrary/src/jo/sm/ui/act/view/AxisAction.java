package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class AxisAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public AxisAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Axis");
        setToolTipText("Display Axis");
        setChecked(mFrame.getClient().isAxis());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
    	mFrame.getClient().setAxis(!mFrame.getClient().isAxis());
        setChecked(!mFrame.getClient().isAxis());
    }

}

package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FancyAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FancyAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Fancy");
        setToolTipText("Use fancy graphics");
        setChecked(mFrame.getClient().isFancyGraphics());
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
    	mFrame.getClient().setFancyGraphics(!mFrame.getClient().isFancyGraphics());
        setChecked(mFrame.getClient().isFancyGraphics());
    }

}

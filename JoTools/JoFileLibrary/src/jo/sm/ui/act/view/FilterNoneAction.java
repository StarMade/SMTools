package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterNoneAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterNoneAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("All");
        setToolTipText("View all blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        mFrame.getClient().setFilter(null);
    }

}

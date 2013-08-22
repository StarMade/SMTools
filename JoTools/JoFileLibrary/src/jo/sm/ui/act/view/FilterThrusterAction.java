package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterThrusterAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterThrusterAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Engine");
        setToolTipText("View engine blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Set<Short> filter = new HashSet<Short>();
        filter.add(BlockTypes.THRUSTER_ID);
        filter.add(BlockTypes.CORE_ID);
        mFrame.getClient().setFilter(filter);
    }

}

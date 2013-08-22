package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterMissileDumbAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterMissileDumbAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Dumb");
        setToolTipText("View dumb missile blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Set<Short> filter = new HashSet<Short>();
        filter.add(BlockTypes.MISSILE_DUMB_CONTROLLER_ID);
        filter.add(BlockTypes.MISSILE_DUMB_ID);
        filter.add(BlockTypes.CORE_ID);
        mFrame.getClient().setFilter(filter);
    }

}

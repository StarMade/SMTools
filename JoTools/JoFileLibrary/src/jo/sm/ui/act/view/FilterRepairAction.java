package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterRepairAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterRepairAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Repair");
        setToolTipText("View repair blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Set<Short> filter = new HashSet<Short>();
        filter.add(BlockTypes.REPAIR_CONTROLLER_ID);
        filter.add(BlockTypes.REPAIR_ID);
        filter.add(BlockTypes.CORE_ID);
        mFrame.getClient().setFilter(filter);
    }

}

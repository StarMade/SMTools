package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterMissileFafoAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterMissileFafoAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("FAFO");
        setToolTipText("View FAFO missile blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Set<Short> filter = new HashSet<Short>();
        filter.add(BlockTypes.MISSILE_FAFO_CONTROLLER_ID);
        filter.add(BlockTypes.MISSILE_FAFO_ID);
        filter.add(BlockTypes.CORE_ID);
        mFrame.getClient().setFilter(filter);
    }

}

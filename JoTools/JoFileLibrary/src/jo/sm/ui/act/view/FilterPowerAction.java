package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterPowerAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterPowerAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Power");
        setToolTipText("View power blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Set<Short> filter = new HashSet<Short>();
        filter.add(BlockTypes.POWER_COIL_ID);
        filter.add(BlockTypes.POWER_ID);
        filter.add(BlockTypes.CORE_ID);
        mFrame.getClient().setFilter(filter);
    }

}

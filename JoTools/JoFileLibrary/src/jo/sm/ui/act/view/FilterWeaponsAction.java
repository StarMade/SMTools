package jo.sm.ui.act.view;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import jo.sm.data.BlockTypes;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class FilterWeaponsAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public FilterWeaponsAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Weapon");
        setToolTipText("View weapon blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        Set<Short> filter = new HashSet<Short>();
        filter.add(BlockTypes.WEAPON_CONTROLLER_ID);
        filter.add(BlockTypes.WEAPON_ID);
        filter.add(BlockTypes.CORE_ID);
        mFrame.getClient().setFilter(filter);
    }

}

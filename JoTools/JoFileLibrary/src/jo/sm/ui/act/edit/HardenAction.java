package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.HullLogic;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class HardenAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public HardenAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Harden");
        setToolTipText("Convert all unhardened hull blocks to hardened hull blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        if (grid == null)
            return;
        mFrame.getClient().getUndoer().checkpoint(grid);
        HullLogic.power(grid);
        StarMadeLogic.setModel(grid);
    }

}

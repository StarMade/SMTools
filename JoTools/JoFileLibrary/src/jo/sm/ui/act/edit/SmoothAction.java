package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;

import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ship.logic.SmoothLogic;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class SmoothAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public SmoothAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Smooth");
        setToolTipText("Smooth hull outline by adding wedges between right angle intersections of blocks");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        SparseMatrix<Block> grid = mFrame.getClient().getGrid();
        if (grid == null)
            return;
        SmoothLogic.smooth(grid);
        mFrame.getClient().setGrid(grid);
    }

}

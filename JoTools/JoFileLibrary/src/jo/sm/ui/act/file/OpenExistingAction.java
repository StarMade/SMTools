package jo.sm.ui.act.file;

import java.awt.event.ActionEvent;

import jo.sm.data.SparseMatrix;
import jo.sm.ship.data.Block;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.ShipChooser;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;
import jo.sm.ui.logic.ShipTreeLogic;

@SuppressWarnings("serial")
public class OpenExistingAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public OpenExistingAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Open...");
        setToolTipText("Open an existing data object");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        ShipChooser chooser = new ShipChooser(mFrame);
        chooser.setVisible(true);
        ShipSpec spec = chooser.getSelected();
        if (spec == null)
            return;
        SparseMatrix<Block> grid = ShipTreeLogic.loadShip(spec);
        if (grid != null)
        {
            mFrame.setSpec(spec);
            mFrame.getClient().setGrid(grid);
        }
    }

}

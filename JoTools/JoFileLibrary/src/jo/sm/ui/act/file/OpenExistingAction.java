package jo.sm.ui.act.file;

import java.awt.event.ActionEvent;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
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
        final ShipSpec spec = chooser.getSelected();
        if (spec == null)
            return;
        IRunnableWithProgress t = new IRunnableWithProgress() {			
			@Override
			public void run(IPluginCallback cb)
			{
		        SparseMatrix<Block> grid = ShipTreeLogic.loadShip(spec, cb);
		        if (grid != null)
		        {
		            StarMadeLogic.getInstance().setCurrentModel(spec);
		            StarMadeLogic.setModel(grid);
		            mFrame.getClient().getUndoer().clear();
		        }
			}
		};
		RunnableLogic.run(mFrame, "Open "+spec.getName(), t);
    }

}

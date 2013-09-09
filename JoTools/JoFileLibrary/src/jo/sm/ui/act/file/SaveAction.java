package jo.sm.ui.act.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import jo.sm.data.SparseMatrix;
import jo.sm.logic.BlueprintLogic;
import jo.sm.logic.RunnableLogic;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ship.data.Block;
import jo.sm.ship.data.Data;
import jo.sm.ship.logic.DataLogic;
import jo.sm.ship.logic.ShipLogic;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;
import jo.vecmath.Point3i;

@SuppressWarnings("serial")
public class SaveAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public SaveAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Save");
        setToolTipText("Save object back to source");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        if (mFrame.getSpec() == null)
            return;
        if (mFrame.getSpec().getType() == ShipSpec.FILE)
            doSaveFile();
        else if (mFrame.getSpec().getType() == ShipSpec.BLUEPRINT)
            doSaveBlueprint(false);
        else if (mFrame.getSpec().getType() == ShipSpec.DEFAULT_BLUEPRINT)
            doSaveBlueprint(true);
        else if (mFrame.getSpec().getType() == ShipSpec.ENTITY)
            doSaveEntity();
    }
    
    private void doSaveFile()
    {
        final File dataFile = mFrame.getSpec().getFile();
        SparseMatrix<Block> grid = mFrame.getClient().getGrid();
        Map<Point3i, Data> data = ShipLogic.getData(grid);
        final Point3i p = new Point3i();
        final Data d = data.get(p);
        if (d == null)
            throw new IllegalArgumentException("No core element to ship!");
        IRunnableWithProgress t = new IRunnableWithProgress() {
			@Override
			public void run(IPluginCallback cb)
			{
		        try
		        {
		            DataLogic.writeFile(p, d, new FileOutputStream(dataFile), true, cb);
		        }
		        catch (IOException e)
		        {
		            throw new IllegalStateException("Cannot save to '"+mFrame.getSpec().getFile()+"'", e);
		        }
			}
        };
        RunnableLogic.run(mFrame, mFrame.getSpec().getName(), t);
    }
    
    private void doSaveBlueprint(final boolean def)
    {
        IRunnableWithProgress t = new IRunnableWithProgress() {
			@Override
			public void run(IPluginCallback cb)
			{
				BlueprintLogic.saveBlueprint(mFrame.getClient().getGrid(), mFrame.getSpec(), def, cb);        
	        };
        };
        RunnableLogic.run(mFrame, mFrame.getSpec().getName(), t);
    }
    
    private void doSaveEntity()
    {
        SparseMatrix<Block> grid = mFrame.getClient().getGrid();
        final Map<Point3i, Data> data = ShipLogic.getData(grid);
        final File baseDir = new File(mFrame.getSpec().getEntity().getFile().getParentFile(), "DATA");
        String fName = mFrame.getSpec().getEntity().getFile().getName();
        final String baseName = fName.substring(0, fName.length() - 4); // remove .ent
        IRunnableWithProgress t = new IRunnableWithProgress() {
			@Override
			public void run(IPluginCallback cb)
			{
		        try
		        {
		            DataLogic.writeFiles(data, baseDir, baseName, cb);
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		        }        
			}
        };
        RunnableLogic.run(mFrame, baseName, t);
    }

}

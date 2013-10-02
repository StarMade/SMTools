package jo.sm.ui.act.file;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;

import jo.sm.logic.BlueprintLogic;
import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;
import jo.sm.ui.logic.ShipSpec;

@SuppressWarnings("serial")
public class SaveAsBlueprintAction extends GenericAction
{
    private boolean     mDef;
    private RenderFrame mFrame;
    
    public SaveAsBlueprintAction(RenderFrame frame, boolean def)
    {
        mFrame = frame;
        mDef = def;
        if (mDef)
        {
            setName("Default Blueprint...");
            setToolTipText("Save object as a new default blueprint");
        }
        else
        {
            setName("Blueprint...");
            setToolTipText("Save object as a new blueprint");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        ShipSpec oriSpec = StarMadeLogic.getInstance().getCurrentModel();
        if (oriSpec == null)
            return;
        String name = JOptionPane.showInputDialog(mFrame, "What do you want to name it?", oriSpec.getName());
        if (StringUtils.isTrivial(name))
            return;
        File prints;
        if (mDef)
            prints = new File(StarMadeLogic.getInstance().getBaseDir(), "blueprints-default");
        else
            prints = new File(StarMadeLogic.getInstance().getBaseDir(), "blueprints");
        File dir = new File(prints, name);
        final ShipSpec spec = new ShipSpec();
        spec.setType(mDef ? ShipSpec.DEFAULT_BLUEPRINT : ShipSpec.BLUEPRINT);
        spec.setClassification(IBlocksPlugin.TYPE_SHIP); // TODO: autodetect
        spec.setName(name);
        spec.setFile(dir);
        IRunnableWithProgress t = new IRunnableWithProgress() {
			@Override
			public void run(IPluginCallback cb)
			{
		        BlueprintLogic.saveBlueprint(StarMadeLogic.getModel(), spec, mDef, cb);
	           StarMadeLogic.getInstance().setCurrentModel(spec);
			}
		};
		RunnableLogic.run(mFrame, name, t);
    }
}

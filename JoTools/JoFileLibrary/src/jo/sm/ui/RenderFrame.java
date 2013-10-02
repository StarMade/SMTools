package jo.sm.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import jo.sm.logic.RunnableLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.mods.IRunnableWithProgress;
import jo.sm.ui.act.edit.RedoAction;
import jo.sm.ui.act.edit.UndoAction;
import jo.sm.ui.act.file.OpenExistingAction;
import jo.sm.ui.act.file.OpenFileAction;
import jo.sm.ui.act.file.QuitAction;
import jo.sm.ui.act.file.SaveAction;
import jo.sm.ui.act.file.SaveAsBlueprintAction;
import jo.sm.ui.act.file.SaveAsFileAction;
import jo.sm.ui.act.view.AxisAction;
import jo.sm.ui.act.view.DontDrawAction;
import jo.sm.ui.act.view.PlainAction;
import jo.sm.ui.logic.MenuLogic;
import jo.sm.ui.logic.ShipSpec;
import jo.sm.ui.logic.ShipTreeLogic;
import jo.sm.ui.lwjgl.LWJGLRenderPanel;

@SuppressWarnings("serial")
public class RenderFrame extends JFrame implements WindowListener
{
    private String[]    mArgs;
    private RenderPanel mClient;

    public RenderFrame(String[] args)
    {
        super("SMEdit");
        mArgs = args;
        // instantiate
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        JMenu menuView = new JMenu("View");
        JMenu menuModify = new JMenu("Modify");
        if ((mArgs.length > 0) && (mArgs[0].equals("-opengl")))
            mClient = new LWJGLRenderPanel();
        else
            mClient = new AWTRenderPanel();
        // layout
        setJMenuBar(menuBar);
        menuBar.add(menuFile);
        menuFile.add(new OpenExistingAction(this));
        menuFile.add(new OpenFileAction(this));
        menuFile.add(new JSeparator());
        menuFile.add(new SaveAction(this));
        JMenu saveAs = new JMenu("Save As");
        menuFile.add(saveAs);
        saveAs.add(new SaveAsBlueprintAction(this, false));
        saveAs.add(new SaveAsBlueprintAction(this, true));
        saveAs.add(new SaveAsFileAction(this));
        JSeparator menuFileStart = new JSeparator();
        menuFileStart.setName("pluginsStartHere");
        menuFile.add(menuFileStart);
        menuFile.add(new JSeparator());
        menuFile.add(new QuitAction(this));
        menuBar.add(menuEdit);
        menuEdit.add(new UndoAction(this));
        menuEdit.add(new RedoAction(this));
        menuEdit.add(new JSeparator());
        menuBar.add(menuView);
        menuView.add(new JCheckBoxMenuItem(new PlainAction(this)));
        menuView.add(new JCheckBoxMenuItem(new AxisAction(this)));
        menuView.add(new JCheckBoxMenuItem(new DontDrawAction(this)));
        JSeparator viewFileStart = new JSeparator();
        viewFileStart.setName("pluginsStartHere");
        menuView.add(viewFileStart);
        menuBar.add(menuModify);
        getContentPane().add(BorderLayout.WEST, new EditPanel(mClient));
        getContentPane().add(BorderLayout.CENTER, mClient);
        getContentPane().add(BorderLayout.SOUTH, new StatusPanel());
        // link
        menuFile.addMenuListener(new PluginPopupListener(IBlocksPlugin.SUBTYPE_FILE));
        menuEdit.addMenuListener(new PluginPopupListener(IBlocksPlugin.SUBTYPE_EDIT));
        menuView.addMenuListener(new PluginPopupListener(IBlocksPlugin.SUBTYPE_VIEW));
        menuModify.addMenuListener(new PluginPopupListener(IBlocksPlugin.SUBTYPE_MODIFY, IBlocksPlugin.SUBTYPE_GENERATE));

        this.addWindowListener(this);
        this.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e)
            { mClient.requestFocusInWindow(); }
         });
        setSize(1024, 768);
        Image icon;
		try
		{
			icon = ImageIO.read(getClass().getResourceAsStream("icon64.png"));
	        setIconImage(icon);
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		if (mClient instanceof Runnable)
		{
		    Thread t = new Thread((Runnable)mClient);
		    t.start();
		}
    }

    public void windowClosing(WindowEvent evt)
    {
        this.setVisible(false);
        this.dispose();
        System.exit(0);
    }

    public void windowOpened(WindowEvent evt)
    {
    }

    public void windowClosed(WindowEvent evt)
    {
    }

    public void windowIconified(WindowEvent evt)
    {
    }

    public void windowDeiconified(WindowEvent evt)
    {
    }

    public void windowActivated(WindowEvent evt)
    {
    }

    public void windowDeactivated(WindowEvent evt)
    {
    }
 
    private void updatePopup(JMenu menu, int... subTypes)
    {
        MenuLogic.clearPluginMenus(menu);
        ShipSpec spec = StarMadeLogic.getInstance().getCurrentModel();
        if (spec == null)
            return;
        int type = spec.getClassification();
        int lastModIndex = menu.getItemCount();
        int lastCount = 0;
        for (int subType : subTypes)
        {
        	int thisCount = MenuLogic.addPlugins(mClient, menu, type, subType);
        	if ((thisCount > 0) && (lastCount > 0))
        	{
                JSeparator sep = new JSeparator();
                sep.setName("plugin");
                menu.add(sep, lastModIndex);
                lastCount = 0;
        	}
        	lastCount += thisCount;
        	lastModIndex = menu.getItemCount();
        }
    }
    
    private static void preLoad()
    {
        Properties props = StarMadeLogic.getProps();
        String home = props.getProperty("starmade.home", "");
        if (!StarMadeLogic.isStarMadeDirectory(home))
        {
            home = System.getProperty("user.dir");
            if (!StarMadeLogic.isStarMadeDirectory(home))
            {
                home = JOptionPane.showInputDialog(null, "Enter in the home directory for StarMade", home);
                if (home == null)
                    System.exit(0);
            }
            props.put("starmade.home", home);
            StarMadeLogic.saveProps();
        }
        StarMadeLogic.setBaseDir(home);
    }

    public static void main(String[] args)
    {
        preLoad();
        final RenderFrame f = new RenderFrame(args);
        f.setVisible(true);
        try
        {
            final ShipSpec spec = ShipTreeLogic.getBlueprintSpec("Isanth-VI", true);
            if (spec != null)
            {
            	IRunnableWithProgress t = new IRunnableWithProgress() {				
    				@Override
    				public void run(IPluginCallback cb)
    				{
    				    StarMadeLogic.getInstance().setCurrentModel(spec);
    		        	StarMadeLogic.setModel(ShipTreeLogic.loadShip(spec, cb));
    				}
    			};
    			RunnableLogic.run(f, "Loading...", t);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public RenderPanel getClient()
    {
        return mClient;
    }

    public void setClient(RenderPanel client)
    {
        mClient = client;
    }
    
    class PluginPopupListener implements MenuListener
    {
    	private int[] mTypes;
    	
    	public PluginPopupListener(int... types)
    	{
    		mTypes = types;
    	}

		@Override
		public void menuCanceled(MenuEvent ev)
		{
		}

		@Override
		public void menuDeselected(MenuEvent ev)
		{
		}
		@Override
		public void menuSelected(MenuEvent ev)
		{
            updatePopup((JMenu)ev.getSource(), mTypes);
		}
    }
}

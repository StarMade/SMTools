package jo.sm.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jo.sm.data.BlockTypes;
import jo.sm.data.CubeIterator;
import jo.sm.data.RenderPoly;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.StarMadeLogic;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.sm.ui.act.plugin.BlocksPluginAction;
import jo.vecmath.Point3i;

@SuppressWarnings("serial")
public class EditPanel extends JPanel
{
    private boolean             mPainting;

    private RenderPanel         mRenderer;

    private JLabel              mCurrent;
    private JButton             mGrey;
    private JButton             mBlack;
    private JButton             mRed;
    private JButton             mPurple;
    private JButton             mBlue;
    private JButton             mGreen;
    private JButton             mBrown;
    private JButton             mYellow;
    private JButton             mWhite;
    private JButton             mClear;
    private JSpinner			mRadius;
    private JButton             mAll;
    private JButton             mPlugins;
    private JCheckBox           mXSymmetry;
    private JCheckBox           mYSymmetry;
    private JCheckBox           mZSymmetry;

    public EditPanel(RenderPanel renderer)
    {
        mRenderer = renderer;
        // instantiate
        mCurrent = new JLabel("blank");
        mGrey = newButton(BlockTypes.HULL_COLOR_GREY_ID);
        mBlack = newButton(BlockTypes.HULL_COLOR_BLACK_ID);
        mRed = newButton(BlockTypes.HULL_COLOR_RED_ID);
        mPurple = newButton(BlockTypes.HULL_COLOR_PURPLE_ID);
        mBlue = newButton(BlockTypes.HULL_COLOR_BLUE_ID);
        mGreen = newButton(BlockTypes.HULL_COLOR_GREEN_ID);
        mBrown = newButton(BlockTypes.HULL_COLOR_BROWN_ID);
        mYellow = newButton(BlockTypes.HULL_COLOR_YELLOW_ID);
        mWhite = newButton(BlockTypes.HULL_COLOR_WHITE_ID);
        mRadius = new JSpinner(new SpinnerNumberModel(1, 1, 64, 1));
        mXSymmetry = new JCheckBox("X Symmetry");
        mXSymmetry.setToolTipText("Mirror paint port/starboard");
        mYSymmetry = new JCheckBox("Y Symmetry");
        mYSymmetry.setToolTipText("Mirror paint dorsal/ventral");
        mZSymmetry = new JCheckBox("Z Symmetry");
        mZSymmetry.setToolTipText("Mirror paint fore/aft");
        mClear = new JButton("Clear");
        mClear.setToolTipText("Stop painting");
        mAll = new JButton("All");
        mAll.setToolTipText("Set all hulls to current color");
        mPlugins = new JButton("Mods");
        mPlugins.setToolTipText("Invoke a mod");
        // layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Paint:"));
        add(mCurrent);
        add(new JLabel("Choose:"));
        add(mGrey);
        add(mBlack);
        add(mRed);
        add(mPurple);
        add(mBlue);
        add(mGreen);
        add(mBrown);
        add(mYellow);
        add(mWhite);
        add(new JLabel("Radius:"));
        add(mRadius);
        add(mXSymmetry);
        add(mYSymmetry);
        add(mZSymmetry);
        add(mClear);
        add(mAll);
        add(mPlugins);
        // link
        MouseAdapter ma = new MouseAdapter() {
            public void mouseClicked(MouseEvent e)
            {
                doMouseClick(e.getX(), e.getY());
            }
            public void mousePressed(MouseEvent ev)
            {
                if (ev.getButton() == MouseEvent.BUTTON3)
                    mPainting = true;
            }
            public void mouseReleased(MouseEvent ev)
            {
                if (ev.getButton() == MouseEvent.BUTTON3)
                    mPainting = false;
            }
            public void mouseDragged(MouseEvent ev)
            {
                if (mPainting)
                    doMouseClick(ev.getX(), ev.getY());
            }
        };
        mRenderer.addMouseListener(ma);
        mRenderer.addMouseMotionListener(ma);
        mClear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doColorClick(null, (short)-1);
            }});
        mAll.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doColorAll();
            }});
        mPlugins.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doPlugin();
            }});
    }

    private JButton newButton(final short blockID)
    {
        ImageIcon rawImage = BlockTypeColors.getBlockImage(blockID);
        Image image = rawImage.getImage().getScaledInstance(32, 32,
                Image.SCALE_DEFAULT);
        final JButton btn = new JButton(new ImageIcon(image));
        btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doColorClick(btn.getIcon(), blockID);
            }});
        return btn;
    }
    
    private void doPlugin()
    {
        int classification = StarMadeLogic.getInstance().getCurrentModel().getClassification();
        List<IBlocksPlugin> plugins = StarMadeLogic.getBlocksPlugins(classification, IBlocksPlugin.SUBTYPE_PAINT);
        if (plugins.size() == 0)
            return;
        JPopupMenu popup = new JPopupMenu();
        for (IBlocksPlugin plugin : plugins)
        {
            BlocksPluginAction action = new BlocksPluginAction(mRenderer, plugin);
            JMenuItem men = new JMenuItem(action);
            popup.add(men);
        }
        Dimension d = mPlugins.getSize();
        popup.show(mPlugins, d.width, d.height);
    }
    
    private void doColorClick(Icon color, short blockID)
    {
        StarMadeLogic.getInstance().setSelectedBlockType(blockID);
        if (StarMadeLogic.getInstance().getSelectedBlockType() == -1)
        {
            mCurrent.setIcon(null);
            mCurrent.setText("blank");
        }
        else
        {
            mCurrent.setIcon(color);
            mCurrent.setText("");
        }
    }
    
    private void doColorAll()
    {
        if (StarMadeLogic.getInstance().getSelectedBlockType() < 0)
            return;
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Iterator<Point3i> i;
        if ((StarMadeLogic.getInstance().getSelectedLower() != null) && (StarMadeLogic.getInstance().getSelectedUpper() != null))
        	i = new CubeIterator(StarMadeLogic.getInstance().getSelectedLower(), StarMadeLogic.getInstance().getSelectedUpper());
        else
        	i = grid.iteratorNonNull();
        colorByIterator(grid, i, false);
    }

	private void colorByIterator(SparseMatrix<Block> grid, Iterator<Point3i> i, boolean symmetric)
	{
	    List<Point3i> coords = new ArrayList<Point3i>();
		while (i.hasNext())
        {
		    coords.clear();
            coords.add(i.next());
            if (symmetric)
            {
                if (mXSymmetry.isSelected())
                    for (int j = coords.size() - 1; j >= 0; j--)
                    {
                        Point3i p1 = coords.get(j);
                        if (p1.x != 8)
                            coords.add(new Point3i(16 - p1.x, p1.y, p1.z));
                    }
                if (mYSymmetry.isSelected())
                    for (int j = coords.size() - 1; j >= 0; j--)
                    {
                        Point3i p1 = coords.get(j);
                        if (p1.y != 8)
                            coords.add(new Point3i(p1.x, 16 - p1.y, p1.z));
                    }
                if (mZSymmetry.isSelected())
                    for (int j = coords.size() - 1; j >= 0; j--)
                    {
                        Point3i p1 = coords.get(j);
                        if (p1.z != 8)
                            coords.add(new Point3i(p1.x, p1.y, 16 - p1.z));
                    }
            }
            for (Point3i c : coords)
                paintBlock(grid, c);
        }
        mRenderer.repaint();
	}

    private void paintBlock(SparseMatrix<Block> grid, Point3i coords)
    {
        Block block = grid.get(coords);
        if (block == null)
            return;
        short newID = BlockTypes.getColoredBlock(block.getBlockID(), StarMadeLogic.getInstance().getSelectedBlockType());
        if (newID != -1)
            block.setBlockID(newID);
    }

    private void doMouseClick(int x, int y)
    {
        if (StarMadeLogic.getInstance().getSelectedBlockType() < 0)
            return;
        RenderPoly b = mRenderer.getTileAt(x, y);
        if (b == null)
            return;
        SparseMatrix<Block> grid = StarMadeLogic.getModel();
        Point3i p = b.getPosition();
        if (p == null)
            return;
        int r = (Integer)mRadius.getValue() - 1;
        Point3i lower = new Point3i(p.x - r, p.y - r, p.z - r);
        Point3i upper = new Point3i(p.x + r, p.y + r, p.z + r);
        colorByIterator(grid, new CubeIterator(lower, upper), true);
    }
}

package jo.sm.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;

import jo.sm.data.BlockTypes;
import jo.sm.data.RenderPoly;
import jo.sm.data.RenderSet;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.RenderPolyLogic;
import jo.sm.logic.StarMadeLogic;
import jo.sm.ship.data.Block;
import jo.sm.ui.logic.ShipSpec;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.Vector3f;
import jo.vecmath.logic.Matrix4fLogic;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel
{
    private static final float  PIXEL_TO_RADIANS = (1f/3.14159f/16f);
    private static final float  ROLL_SCALE = 1.1f;
    
    private static final int MOUSE_MODE_NULL = 0;
    private static final int MOUSE_MODE_PIVOT = 1;
    private static final int MOUSE_MODE_SELECT = 2;
    
    private Point               mMouseDownAt;
    private int					mMouseMode;
    private boolean				mFancyGraphics;
    
    private ShipSpec            mSpec;
    private SparseMatrix<Block> mGrid;
    private SparseMatrix<Block> mFilteredGrid;
    private Set<Short>          mFilter;
    private RenderSet			mTiles;
    private Matrix4f            mTransform;
    private Vector3f            mPreTranslate;
    private float               mScale;
    private float               mRotX;
    private float               mRotY;
    Vector3f            mPOVTranslate;
    private Vector3f            mPostTranslate;
    
    public RenderPanel()
    {
    	mTiles = new RenderSet();
        mTransform = new Matrix4f();
        mPreTranslate = new Vector3f();
        mPOVTranslate = new Vector3f();
        mScale = 1f;
        mRotX = 0;
        mRotY = 0;
        mPostTranslate = new Vector3f();
        mFancyGraphics = true;
        MouseAdapter ma =  new MouseAdapter(){
            public void mousePressed(MouseEvent ev)
            {            	
                if (ev.getButton() == MouseEvent.BUTTON1)
                    doMouseDown(ev.getPoint(), ev.getModifiers());
            }
            public void mouseReleased(MouseEvent ev)
            {
                if (ev.getButton() == MouseEvent.BUTTON1)
                    doMouseUp(ev.getPoint(), ev.getModifiers());
            }
            public void mouseDragged(MouseEvent ev)
            {
                if (mMouseDownAt != null)
                    doMouseMove(ev.getPoint(), ev.getModifiers());
            }
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                doMouseWheel(e.getWheelRotation());
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
        		new RenderPanelKeyEventDispatcher(this));
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addMouseWheelListener(ma);
    }
    
    void updateTransform()
    {
        Dimension s = getSize();
        mPostTranslate.x = s.width/2;
        mPostTranslate.y = s.height/2;
        
        mTransform.setIdentity();
        Matrix4fLogic.translate(mTransform, mPreTranslate);
        Matrix4fLogic.rotX(mTransform, mRotX);
        Matrix4fLogic.rotY(mTransform, mRotY);
        Matrix4fLogic.translate(mTransform, mPOVTranslate);
        Matrix4fLogic.scale(mTransform, mScale);
        Matrix4fLogic.translate(mTransform, mPostTranslate);
        RenderPolyLogic.transformAndSort(mTiles, mTransform);
        repaint();
    }
    
    private void doMouseDown(Point p, int modifiers)
    {
        mMouseDownAt = p;
        //System.out.println("MouseMod="+Integer.toHexString(modifiers));
        if ((modifiers&MouseEvent.SHIFT_MASK) != 0)
        {
        	RenderPoly tile = getTileAt(p.x, p.y);
        	if (tile == null)
        		return;
        	mMouseMode = MOUSE_MODE_SELECT;
        	StarMadeLogic.getInstance().setSelectedLower(null);
        	StarMadeLogic.getInstance().setSelectedUpper(null);
        	extendSelection(tile);
        }
        else
        	mMouseMode = MOUSE_MODE_PIVOT;
    }
    
    private void doMouseMove(Point p, int modifiers)
    {
    	if (mMouseMode == MOUSE_MODE_PIVOT)
    	{
	        int dx = p.x - mMouseDownAt.x;
	        int dy = p.y - mMouseDownAt.y;
	        mMouseDownAt = p;
	        mRotX += dy*PIXEL_TO_RADIANS;
	        mRotY += dx*PIXEL_TO_RADIANS;
	        updateTransform();
    	}
    	else if (mMouseMode == MOUSE_MODE_SELECT)
    	{
        	RenderPoly tile = getTileAt(p.x, p.y);
        	if (tile != null)
        		extendSelection(tile);
    	}
    }
    
    private void doMouseUp(Point p, int modifiers)
    {
    	if (mMouseMode == MOUSE_MODE_PIVOT)
    	{
    		doMouseMove(p, modifiers);
    		mMouseDownAt = null;
    	}
    	else if (mMouseMode == MOUSE_MODE_PIVOT)
    	{
    		doMouseMove(p, modifiers);
    	}
        mMouseMode = MOUSE_MODE_NULL;
    }

    private void doMouseWheel(int roll)
    {
        if (roll > 0)
        {
            while (roll-- > 0)
                mScale /= ROLL_SCALE;
        }
        else if (roll < 0)
        {
            while (roll++ < 0)
                mScale *= ROLL_SCALE;
        }
        updateTransform();
    }
    
    private void extendSelection(RenderPoly tile)
    {
    	Point3i lowest = new Point3i();
    	Point3i highest = new Point3i();
    	RenderPolyLogic.getBounds(tile, lowest, highest);
    	Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
    	if (lower == null)
    	{
    		lower = lowest;
    		StarMadeLogic.getInstance().setSelectedLower(lower);
    	}
    	else
    	{
    		lower.x = Math.min(lower.x, lowest.x);
    		lower.y = Math.min(lower.y, lowest.y);
    		lower.z = Math.min(lower.z, lowest.z);
    	}
    	Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
    	if (upper == null)
    	{
    		upper = highest;
    		StarMadeLogic.getInstance().setSelectedUpper(upper);
    	}
    	else
    	{
    		upper.x = Math.min(upper.x, highest.x);
    		upper.y = Math.min(upper.y, highest.y);
    		upper.z = Math.min(upper.z, highest.z);
    	}
    	updateTiles();
    }
    
    public void paint(Graphics g)
    {
        if (mTiles == null)
            return;
        Dimension s = getSize();
        g.setColor(Color.black);
        g.fillRect(0, 0, s.width, s.height);
        Graphics2D g2 = (Graphics2D)g;
        RenderPolyLogic.draw(g2, mTiles, mFancyGraphics);
    }

    public SparseMatrix<Block> getGrid()
    {
        return mGrid;
    }

    public void setGrid(SparseMatrix<Block> grid)
    {
        mGrid = grid;
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        mGrid.getBounds(lower, upper);
        mPreTranslate.x = -(lower.x + upper.x)/2;
        mPreTranslate.y = -(lower.y + upper.y)/2;
        mPreTranslate.z = -(lower.z + upper.z)/2;
        float maxModel = Math.max(Math.max(upper.x - lower.x, upper.y - lower.y), upper.z - lower.z);
        Dimension s = getSize();
        float maxScreen = Math.max(s.width, s.height);
        mScale = maxScreen/maxModel/2f;
        //System.out.println("Scale="+mScale+", preTrans="+mPreTranslate);
        //mTransform.setTranslation(new Vector3f(s.width/2f, s.height/2f, 0));
        updateTiles();
    }
    
    public void updateTiles()
    {
        if ((mFilter == null) || (mFilter.size() == 0))
            mFilteredGrid = mGrid;
        else
        {
            mFilteredGrid = new SparseMatrix<Block>();
            for (Iterator<Point3i> i = mGrid.iterator(); i.hasNext(); )
            {
                Point3i p = i.next();
                Block b = mGrid.get(p);
                if ((b != null) && mFilter.contains(b.getBlockID()))
                    mFilteredGrid.set(p, b);
            }
        }
        RenderPolyLogic.fillPolys(mFilteredGrid, mTiles);
        Point3i lower = StarMadeLogic.getInstance().getSelectedLower();
        Point3i upper = StarMadeLogic.getInstance().getSelectedUpper();
        if ((lower != null) && (upper != null))
        {
            upper = new Point3i(upper.x + 1, upper.y + 1, upper.z + 1); // only place where bounds are at +1
        	addSelectFace(upper.x, lower.y, lower.z, upper.x, upper.y, upper.z,
        			RenderPoly.XP, BlockTypes.SPECIAL_SELECT_XP);
        	addSelectFace(lower.x, lower.y, lower.z, lower.x, upper.y, upper.z,
        			RenderPoly.XM, BlockTypes.SPECIAL_SELECT_XM);
        	addSelectFace(lower.x, upper.y, lower.z, upper.x, upper.y, upper.z,
        			RenderPoly.YP, BlockTypes.SPECIAL_SELECT_YP);
        	addSelectFace(lower.x, lower.y, lower.z, upper.x, lower.y, upper.z,
        			RenderPoly.YM, BlockTypes.SPECIAL_SELECT_YM);
        	addSelectFace(lower.x, lower.y, upper.z, upper.x, upper.y, upper.z,
        			RenderPoly.ZP, BlockTypes.SPECIAL_SELECT_ZP);
        	addSelectFace(lower.x, lower.y, lower.z, upper.x, upper.y, lower.z,
        			RenderPoly.ZM, BlockTypes.SPECIAL_SELECT_ZM);
    	}
        updateTransform();
    }
    
    private void addSelectFace(int x1, int y1, int z1, int x2, int y2, int z2,
			int face, short type)
	{
    	if (x1 == x2)
    	{
    		for (int y = y1; y < y2; y++)
    			for (int z = z1; z < z2; z++)
    				addSelectTile(x1, y, z, x2, y+1, z+1, face, type);
    	}
    	else if (y1 == y2)
    	{
    		for (int x = x1; x < x2; x++)
    			for (int z = z1; z < z2; z++)
    				addSelectTile(x, y1, z, x+1, y2, z+1, face, type);
    	}
    	else if (z1 == z2)
    	{
    		for (int x = x1; x < x2; x++)
        		for (int y = y1; y < y2; y++)
    				addSelectTile(x, y, z1, x+1, y+1, z2, face, type);
    	}
	}
    
    private void addSelectTile(int x1, int y1, int z1, int x2, int y2, int z2,
			int face, short type)
	{
    	RenderPoly tile = new RenderPoly();
    	if (x1 == x2)
	    	tile.setModelPoints(new Point3i[]{
	    			new Point3i(x1, y1, z1),
	    			new Point3i(x1, y1, z2),
	    			new Point3i(x1, y2, z2),
	    			new Point3i(x1, y2, z1),
	    	});
    	else if (y1 == y2)
	    	tile.setModelPoints(new Point3i[]{
	    			new Point3i(x1, y1, z1),
	    			new Point3i(x1, y1, z2),
	    			new Point3i(x2, y1, z2),
	    			new Point3i(x2, y1, z1),
	    	});
    	else if (z1 == z2)
	    	tile.setModelPoints(new Point3i[]{
	    			new Point3i(x1, y1, z1),
	    			new Point3i(x1, y2, z1),
	    			new Point3i(x2, y2, z1),
	    			new Point3i(x2, y1, z1),
	    	});
    	tile.setNormal(face);
    	tile.setType(RenderPoly.SQUARE);
    	tile.setBlock(new Block(type));
    	mTiles.getAllPolys().add(tile);
	}

	public RenderPoly getTileAt(double x, double y)
    {
        for (int i = mTiles.getVisiblePolys().size() - 1; i >= 0; i--)
        {
            RenderPoly tile = mTiles.getVisiblePolys().get(i);
            Point3f[] corners = RenderPolyLogic.getCorners(tile, mTiles);
            Path2D p = new Path2D.Float();
            p.moveTo(corners[0].x, corners[0].y);
            p.lineTo(corners[1].x, corners[1].y);
            p.lineTo(corners[2].x, corners[2].y);
            p.lineTo(corners[3].x, corners[3].y);
            p.lineTo(corners[0].x, corners[0].y);
            if (p.contains(x, y))
                return tile;
        }
        return null;
    }
    
    public Block getBlockAt(double x, double y)
    {
    	RenderPoly tile = getTileAt(x, y);
    	if (tile != null)
    		return tile.getBlock();
        return null;
    }

    public Set<Short> getFilter()
    {
        return mFilter;
    }

    public void setFilter(Set<Short> filter)
    {
        mFilter = filter;
        updateTiles();
    }

    public ShipSpec getSpec()
    {
        return mSpec;
    }

    public void setSpec(ShipSpec spec)
    {
        mSpec = spec;
    }

	public boolean isFancyGraphics()
	{
		return mFancyGraphics;
	}

	public void setFancyGraphics(boolean fancyGraphics)
	{
		mFancyGraphics = fancyGraphics;
		repaint();
	}

}

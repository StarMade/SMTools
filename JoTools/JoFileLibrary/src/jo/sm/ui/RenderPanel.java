package jo.sm.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;

import jo.sm.data.RenderPoly;
import jo.sm.data.RenderSet;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.RenderPolyLogic;
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
    
    private static final int PAN_XM = 'A';
    private static final int PAN_XP = 'D';
    private static final int PAN_YM = 'W';
    private static final int PAN_YP = 'S';
    private static final int PAN_ZM = 'Q';
    private static final int PAN_ZP = 'E';
    
    private Point               mMouseDownAt;
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
    private Vector3f            mPOVTranslate;
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
                    doMouseDown(ev.getPoint());
            }
            public void mouseReleased(MouseEvent ev)
            {
                if (ev.getButton() == MouseEvent.BUTTON1)
                    doMouseUp(ev.getPoint());
            }
            public void mouseDragged(MouseEvent ev)
            {
                if (mMouseDownAt != null)
                    doMouseMove(ev.getPoint());
            }
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                doMouseWheel(e.getWheelRotation());
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {			
			@Override
			public boolean dispatchKeyEvent(KeyEvent ev)
			{
				if (ev.getID() == KeyEvent.KEY_PRESSED)
					doKeyDown(ev.getKeyCode(), ev.getModifiersEx());
				else if (ev.getID() == KeyEvent.KEY_RELEASED)
						doKeyUp(ev.getKeyCode(), ev.getModifiersEx());
				return false;
			}
		});
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addMouseWheelListener(ma);
    }
    
    private void updateTransform()
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
    
    public void doKeyDown(int keyCode, int keyMod)
    {
    	System.out.println("code="+Integer.toHexString(keyCode)+", mod="+Integer.toHexString(keyMod));
    	if ((keyCode == PAN_XP) && (keyMod == 0))
    	{
    		mPOVTranslate.x++;
    		updateTransform();
    	}
    	else if ((keyCode == PAN_XM) && (keyMod == 0))
    	{
    		mPOVTranslate.x--;
    		updateTransform();
    	}
    	else if ((keyCode == PAN_YP) && (keyMod == 0))
    	{
    		mPOVTranslate.y++;
    		updateTransform();
    	}
    	else if ((keyCode == PAN_YM) && (keyMod == 0))
    	{
    		mPOVTranslate.y--;
    		updateTransform();
    	}
    	else if ((keyCode == PAN_ZP) && (keyMod == 0))
    	{
    		mPOVTranslate.z++;
    		updateTransform();
    	}
    	else if ((keyCode == PAN_ZM) && (keyMod == 0))
    	{
    		mPOVTranslate.z--;
    		updateTransform();
    	}
    }
    
    public void doKeyUp(int keyCode, int keyMod)
    {
    	
    }
    
    private void doMouseDown(Point p)
    {
        mMouseDownAt = p;
    }
    
    private void doMouseMove(Point p)
    {
        int dx = p.x - mMouseDownAt.x;
        int dy = p.y - mMouseDownAt.y;
        mMouseDownAt = p;
        mRotX += dy*PIXEL_TO_RADIANS;
        mRotY += dx*PIXEL_TO_RADIANS;
        updateTransform();
    }
    
    private void doMouseUp(Point p)
    {
        doMouseMove(p);
        mMouseDownAt = null;
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
    
    private void updateTiles()
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
        updateTransform();
    }
    
    public Block getBlockAt(double x, double y)
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
                return tile.getBlock();
        }
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

package jo.sm.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import jo.sm.data.RenderPoly;
import jo.sm.data.RenderSet;
import jo.sm.data.SparseMatrix;
import jo.sm.data.UndoBuffer;
import jo.sm.logic.RenderPolyLogic;
import jo.sm.ship.data.Block;
import jo.sm.ui.logic.ShipSpec;
import jo.vecmath.Point3i;
import jo.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("serial")
public class LWJGLRenderPanel extends RenderPanel implements Runnable
{
    private boolean mCloseRequested = false;
    private AtomicReference<Dimension> mNewCanvasSize = new AtomicReference<Dimension>();

    private JGLCanvas              mCanvas;
    private SparseMatrix<Block> mGrid;
    private SparseMatrix<Block> mFilteredGrid;
    private Set<Short>          mFilter;
    private boolean             mAxis;
    private boolean             mPlainGraphics;
    private UndoBuffer          mUndoer;
    private ShipSpec            mSpec;
    private RenderSet           mTiles;

    private Vector3f            mPreTranslate;
    private float               mScale;
    private float               mRotX;
    private float               mRotY;
    Vector3f            mPOVTranslate;
    private Vector3f            mPostTranslate;

    public LWJGLRenderPanel()
    {
        mUndoer = new UndoBuffer();
        mTiles = new RenderSet();
        mIB16 = BufferUtils.createIntBuffer(16);
        mPreTranslate = new Vector3f();
        mPOVTranslate = new Vector3f();
        mScale = 1f;
        mRotX = (float)Math.PI;
        mRotY = 0;
        mPostTranslate = new Vector3f();
        mCanvas = new Canvas();
        setLayout(new BorderLayout());
        add("Center", mCanvas);
        mCanvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            { mNewCanvasSize.set(getSize()); }
         });
    }

    public void run()
    {
        try {
            Display.setParent(mCanvas);
            Display.setVSyncEnabled(true);
            Display.create();

            Dimension newDim;
            
            while(!Display.isCloseRequested() && !mCloseRequested)
            {
               newDim = mNewCanvasSize.getAndSet(null);
               
               if (newDim != null)
               {
                  GL11.glViewport(0, 0, newDim.width, newDim.height);
                  syncViewportSize();
               }
               
               paint();
               Display.update();
            }

            Display.destroy();
         } catch (LWJGLException e) {
            e.printStackTrace();
         }        
    }
    
    private void paint()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        // transform
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        float[] rgba = new float[4];
        // draw tiles
        for (RenderPoly p : mTiles.getAllPolys().toArray(new RenderPoly[0]))
        {
            Color c = BlockTypeColors.getFillColor(p.getBlock().getBlockID());            
            c.getColorComponents(rgba);
            GL11.glColor3f(rgba[0], rgba[1], rgba[2]);
            if (p.getModelPoints().length == 4)
            {
                // draw quad
                GL11.glBegin(GL11.GL_QUADS);
                for (Point3i v : p.getModelPoints())
                    GL11.glVertex3f(v.x, v.y, v.z);
                GL11.glEnd();
            }
        }
    }
    
    private final IntBuffer mIB16;
    private int mViewportX;
    private int mViewportBottom;
    private int mWidth;
    private int mHeight;
    
    /**
     * <p>Queries the current view port size & position and updates all related
     * internal state.</p>
     *
     * <p>It is important that the internal state matches the OpenGL viewport or
     * clipping won't work correctly.</p>
     *
     * <p>This method should only be called when the viewport size has changed.
     * It can have negative impact on performance to call every frame.</p>
     * 
     * @see #getWidth()
     * @see #getHeight()
     */
    public void syncViewportSize() {
        mIB16.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, mIB16);
        mViewportX = mIB16.get(0);
        mWidth = mIB16.get(2);
        mHeight = mIB16.get(3);
        mViewportBottom = mIB16.get(1) + mHeight;
    }
    @Override
    public void updateTransform()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public SparseMatrix<Block> getGrid()
    {
        return mGrid;
    }

    @Override
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

    @Override
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
    }

    @Override
    public RenderPoly getTileAt(double x, double y)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getBlockAt(double x, double y)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Short> getFilter()
    {
        return mFilter;
    }

    @Override
    public void setFilter(Set<Short> filter)
    {
        mFilter = filter;
    }

    @Override
    public ShipSpec getSpec()
    {
        return mSpec;
    }

    @Override
    public void setSpec(ShipSpec spec)
    {
        mSpec = spec;
    }

    @Override
    public boolean isPlainGraphics()
    {
        return mPlainGraphics;
    }

    @Override
    public void setPlainGraphics(boolean plainGraphics)
    {
        mPlainGraphics = plainGraphics;
    }

    @Override
    public boolean isAxis()
    {
        return mAxis;
    }

    @Override
    public void setAxis(boolean axis)
    {
        mAxis = axis;
    }

    public UndoBuffer getUndoer()
    {
        return mUndoer;
    }

    public void setUndoer(UndoBuffer undoer)
    {
        mUndoer = undoer;
    }

    public void undo()
    {
        SparseMatrix<Block> grid = mUndoer.undo();
        if (grid != null)
            setGrid(grid);
    }

    public void redo()
    {
        SparseMatrix<Block> grid = mUndoer.redo();
        if (grid != null)
            setGrid(grid);
    }

    @Override
    public void setCloseRequested(boolean pleaseClose)
    {
        mCloseRequested = pleaseClose;
    }
}

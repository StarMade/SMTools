package jo.sm.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import jo.sm.data.RenderPoly;
import jo.sm.data.RenderSet;
import jo.sm.data.SparseMatrix;
import jo.sm.data.UndoBuffer;
import jo.sm.ship.data.Block;
import jo.sm.ui.logic.ShipSpec;
import jo.util.jgl.obj.JGLCamera;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLScene;
import jo.util.jgl.obj.tri.JGLObjCube;
import jo.util.lwjgl.win.JGLCanvas;
import jo.vecmath.Color4f;
import jo.vecmath.Point3f;
import jo.vecmath.Point3i;
import jo.vecmath.Vector3f;

@SuppressWarnings("serial")
public class LWJGLRenderPanel extends RenderPanel
{
    private boolean mCloseRequested = false;
    private AtomicReference<Dimension> mNewCanvasSize = new AtomicReference<Dimension>();

    private JGLCanvas           mCanvas;
    private JGLScene			mScene;
    private JGLCamera			mUniverse;
    private JGLGroup			mBlocks;
    private JGLGroup			mSelection;
    private JGLGroup			mAxis;
    
    private SparseMatrix<Block> mGrid;
    private SparseMatrix<Block> mFilteredGrid;
    private Set<Short>          mFilter;
    private boolean             mPlainGraphics;
    private UndoBuffer          mUndoer;
    private ShipSpec            mSpec;
    private RenderSet           mTiles;

    private Vector3f            mPreTranslate;
    private float               mScale;
    private float               mRotX;
    private float               mRotY;
    Vector3f            		mPOVTranslate;
    private Vector3f            mPostTranslate;

    public LWJGLRenderPanel()
    {
        mUndoer = new UndoBuffer();
        mTiles = new RenderSet();
        mPreTranslate = new Vector3f();
        mPOVTranslate = new Vector3f();
        mScale = 1f;
        mRotX = (float)Math.PI;
        mRotY = 0;
        mPostTranslate = new Vector3f();
        mScene = new JGLScene();
        mScene.setBackground(new Color4f());
        mUniverse = new JGLCamera();
        mScene.setNode(mUniverse);
        mBlocks = new JGLGroup();
        mUniverse.getChildren().add(mBlocks);
        mSelection = new JGLGroup();
        mUniverse.getChildren().add(mSelection);
        mAxis = new JGLGroup();
        mUniverse.getChildren().add(mAxis);
        mCanvas = new JGLCanvas();
        mCanvas.setScene(mScene);
        setLayout(new BorderLayout());
        add("Center", mCanvas);
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
        mUniverse.getCamera().setLocation(new Point3f(0, 0, .51f));
        //mUniverse.getCamera().scale(mScale);
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
        mBlocks.getChildren().clear();
        JGLObjCube cube = new JGLObjCube(new Point3f(1, 1, 1), new Point3f());
        mBlocks.add(cube);
        /*
        for (Iterator<Point3i> i = mFilteredGrid.iteratorNonNull(); i.hasNext(); )
        {
        	Point3i p = i.next();
        	Point3f center = new Point3f(p.x, p.y, p.z);
        	Color c = BlockTypeColors.getFillColor(mFilteredGrid.get(p).getBlockID());
        	Point3f color = new Point3f(c.getComponents(null));
        	System.out.println("Added at "+center+", color="+color);
        	JGLObjCube cube = new JGLObjCube(new Point3f(1, 1, 1), center);
        	cube.setSolidColor(color);
        	mBlocks.add(cube);
        }
         */
        System.out.println("Added "+mBlocks.getChildren().size()+" blocks");
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
        return !mAxis.isCull();
    }

    @Override
    public void setAxis(boolean axis)
    {
        mAxis.setCull(!axis);
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

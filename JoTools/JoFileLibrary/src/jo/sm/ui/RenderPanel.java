package jo.sm.ui;

import javax.swing.JPanel;

import jo.sm.data.RenderPoly;
import jo.sm.data.UndoBuffer;
import jo.sm.ship.data.Block;

@SuppressWarnings("serial")
public abstract class RenderPanel extends JPanel
{
    public abstract void updateTransform();
    public abstract void updateTiles();
	public abstract RenderPoly getTileAt(double x, double y);
    public abstract Block getBlockAt(double x, double y);
	public abstract boolean isPlainGraphics();
	public abstract void setPlainGraphics(boolean plainGraphics);
	public abstract boolean isAxis();
	public abstract void setAxis(boolean axis);
	public abstract boolean isDontDraw();
	public abstract void setDontDraw(boolean dontDraw);
	public abstract void setCloseRequested(boolean pleaseClose);
    public abstract UndoBuffer getUndoer();
    public abstract void setUndoer(UndoBuffer undoer);
	public abstract void undo();
	public abstract void redo();
}

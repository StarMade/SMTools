package jo.sm.data;

import java.util.ArrayList;
import java.util.List;

import jo.vecmath.Point3f;

public class RenderSet
{
	private List<RenderPoly>	mAllPolys;
	private List<RenderPoly>	mVisiblePolys;
	private Point3f				mUnitX;
	private Point3f				mUnitY;
	private Point3f				mUnitZ;
	private Point3f				mOrigin;
	
	public RenderSet()
	{
		mAllPolys = new ArrayList<RenderPoly>();
		mVisiblePolys = new ArrayList<RenderPoly>();
	}

	public List<RenderPoly> getAllPolys()
	{
		return mAllPolys;
	}

	public void setAllPolys(List<RenderPoly> polys)
	{
		mAllPolys = polys;
	}

	public List<RenderPoly> getVisiblePolys()
	{
		return mVisiblePolys;
	}

	public void setVisiblePolys(List<RenderPoly> visiblePolys)
	{
		mVisiblePolys = visiblePolys;
	}

	public Point3f getUnitX()
	{
		return mUnitX;
	}

	public void setUnitX(Point3f unitX)
	{
		mUnitX = unitX;
	}

	public Point3f getUnitY()
	{
		return mUnitY;
	}

	public void setUnitY(Point3f unitY)
	{
		mUnitY = unitY;
	}

	public Point3f getUnitZ()
	{
		return mUnitZ;
	}

	public void setUnitZ(Point3f unitZ)
	{
		mUnitZ = unitZ;
	}

	public Point3f getOrigin()
	{
		return mOrigin;
	}

	public void setOrigin(Point3f origin)
	{
		mOrigin = origin;
	}
}

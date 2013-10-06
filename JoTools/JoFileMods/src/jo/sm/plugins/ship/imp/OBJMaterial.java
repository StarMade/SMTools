package jo.sm.plugins.ship.imp;

import java.awt.image.BufferedImage;
import java.io.File;

import jo.vecmath.Point3f;

public class OBJMaterial
{
	private String			mName;
	private float			mNS;
	private Point3f			mKA;
	private Point3f			mKD;
	private Point3f			mKS;
	private float			mNi;
	private float			mD;
	private float			mIllum;
	private File			mMapKD;
	private BufferedImage	mMapKDImage;
	
	public String getName()
	{
		return mName;
	}
	public void setName(String name)
	{
		mName = name;
	}
	public float getNS()
	{
		return mNS;
	}
	public void setNS(float nS)
	{
		mNS = nS;
	}
	public Point3f getKA()
	{
		return mKA;
	}
	public void setKA(Point3f kA)
	{
		mKA = kA;
	}
	public Point3f getKD()
	{
		return mKD;
	}
	public void setKD(Point3f kF)
	{
		mKD = kF;
	}
	public Point3f getKS()
	{
		return mKS;
	}
	public void setKS(Point3f kS)
	{
		mKS = kS;
	}
	public float getNi()
	{
		return mNi;
	}
	public void setNi(float ni)
	{
		mNi = ni;
	}
	public float getD()
	{
		return mD;
	}
	public void setD(float f)
	{
		mD = f;
	}
	public float getIllum()
	{
		return mIllum;
	}
	public void setIllum(float illum)
	{
		mIllum = illum;
	}
	public File getMapKD()
	{
		return mMapKD;
	}
	public void setMapKD(File mapKD)
	{
		mMapKD = mapKD;
	}
	public BufferedImage getMapKDImage()
	{
		return mMapKDImage;
	}
	public void setMapKDImage(BufferedImage mapKDImage)
	{
		mMapKDImage = mapKDImage;
	}
}

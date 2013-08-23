package jo.vecmath.ext;

public class Plane3f 
{
	private Point3f	mR; // position
	private Point3f	mN; // normal
	
	public Plane3f()
	{
		mR = new Point3f();
		mN = new Point3f(0, 0, 1);
	}
	
	public Plane3f(Point3f r, Point3f n)
	{
		this();
		mR.set(r);
		mN.set(n);
		mN.normalize();
	}
	
	public Plane3f(Point3f n, double radius)
	{
		this();
		mN.set(n);
		mN.normalize();
		mR.set(mN);
		mR.scale(radius);
	}
	
	public Plane3f(Plane3f p)
	{
		this(p.getR(), p.getN());
	}
	
	public String toString()
	{
		return mR+"||"+mN;
	}
	
	public double dist(Point3f p)
	{
		return Math.abs(p.sub(mR).dot(mN));
	}
	
	public Point3f getR() {
		return mR;
	}
	public void setR(Point3f r) {
		mR = r;
	}
	public Point3f getN() {
		return mN;
	}
	public void setN(Point3f n) {
		mN = n;
	}
}

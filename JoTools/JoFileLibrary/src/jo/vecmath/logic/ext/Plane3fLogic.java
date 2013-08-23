package jo.vecmath.logic.ext;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.Plane3f;

public class Plane3fLogic 
{
	// make a plane from a point, and two vectors originating at that point
	public static Plane3f fromPointAndVectors(Point3f p, Point3f v1, Point3f v2)
	{
		Point3f norm = v1.cross(v2);
		return new Plane3f(p, norm);
	}

	// make a plane from three points
	public static Plane3f fromPoints(Point3f p1, Point3f p2, Point3f p3)
	{
		return fromPointAndVectors(p1, p2.sub(p1), p3.sub(p1));
	}
	
	public static Line3f intersect(Plane3f p1, Plane3f p2)
	{
		double h1 = p1.getR().dot(p1.getN());
		double h2 = p2.getR().dot(p2.getN());
		double n1dotn2 = p1.getN().dot(p2.getN());
		if (Math.abs(Math.abs(n1dotn2) - 1) < Point3fLogic.EPSILON)
			return null;
		double c1 = (h1 - h2*n1dotn2)/(1 - n1dotn2*n1dotn2);
		double c2 = (h2 - h1*n1dotn2)/(1 - n1dotn2*n1dotn2);
		Point3f p = p1.getN().mult(c1).add(p2.getN().mult(c2));
		Point3f n = p1.getN().cross(p2.getN());
		return new Line3f(p, n);
	}
	
	public static double angle(Plane3f p1, Plane3f p2)
	{
		double cosa = p1.getN().dot(p2.getN());
		return Math.acos(cosa);
	}
	
	public static int whichSide(Plane3f plane, Point3f point)
	{
		double dot = point.sub(plane.getR()).dot(plane.getN());
		return Point3fLogic.sgn(dot);
	}
}

package jo.vecmath.logic.ext;

import jo.vecmath.Point3f;
import jo.vecmath.ext.Line3f;
import jo.vecmath.ext.LineSegment3f;

public class Line3fLogic {
	// make a line from two points
	public static Line3f fromPoints(Point3f p1, Point3f p2)
	{
		return new Line3f(p1, p2.sub(p1));
	}

	public static Point3f[] closestPoints(Line3f l1, Line3f l2)
	{
		Point3f p21 = l2.getP().sub(l1.getP());
		Point3f m = l2.getN().cross(l1.getN());
		double m2 = m.dot(m);
		if (m2 == 0)
			return null;
		Point3f r = p21.cross(m.mult(1/m2));
		double t1 = r.dot(l2.getN());
		Point3f q1 = l1.getP().add(l1.getN().mult(t1));
		double t2 = r.dot(l1.getN());
		Point3f q2 = l2.getP().add(l2.getN().mult(t2));
		return new Point3f[] { q1, q2 };
	}
	
	public static double dist(Line3f l, Point3f p)
	{
    	Point3f direct = p.sub(l.getP());
    	Point3f projected = l.getN().mult(direct.dot(l.getN()));
    	double d = direct.sub(projected).mag();
    	return d;
	}
	
	public static double dist(Line3f l1, Line3f l2)
	{
		Point3f[] closest = closestPoints(l1, l2);
		if (closest == null)
			return dist(l1, l2.getP()); // parallel
		double dist = closest[0].dist(closest[1]);
		return dist;
	}
	
	public static Point3f intersect(Line3f l1, Line3f l2)
	{
		Point3f[] closest = closestPoints(l1, l2);
		if (closest == null)
			return null;
		double dist = closest[0].dist(closest[1]);
		if (dist > Point3fLogic.EPSILON)
			return null;
		return closest[0];
	}

    public static Line3f fromSegment(LineSegment3f segment)
    {
        return fromPoints(segment.getP1(), segment.getP2());
    }
}

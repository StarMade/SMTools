package jo.vecmath.logic;

import jo.vecmath.Point3i;

public class Point3iLogic
{
	public static int distance(Point3i p1, Point3i p2)
	{
		int dx = p1.x - p2.x;
		int dy = p1.y - p2.y;
		int dz = p1.z - p2.z;
		return (int)Math.sqrt(dx*dx + dy*dy + dz*dz);
	}

	public static Point3i min(Point3i p, Point3i lower, Point3i upper)
	{
		p.x = Math.min(lower.x, upper.x);
		p.y = Math.min(lower.y, upper.y);
		p.z = Math.min(lower.z, upper.z);
		return p;
	}

	public static Point3i max(Point3i p, Point3i lower, Point3i upper)
	{
		p.x = Math.max(lower.x, upper.x);
		p.y = Math.max(lower.y, upper.y);
		p.z = Math.max(lower.z, upper.z);
		return p;
	}

	public static Point3i min(Point3i lower, Point3i upper)
	{
		return min(new Point3i(), lower, upper);
	}

	public static Point3i max(Point3i lower, Point3i upper)
	{
		return max(new Point3i(), lower, upper);
	}
}

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
}

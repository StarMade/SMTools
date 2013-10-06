package jo.vecmath.logic;

import java.util.StringTokenizer;

import jo.sm.logic.utils.FloatUtils;
import jo.vecmath.Matrix4f;
import jo.vecmath.Point3f;

public class Point3fLogic extends Tuple3fLogic
{
    public static void rotateBy(Point3f v, float x, float y, float z)
    {
        Matrix4f m = Matrix4fLogic.makeRotateMatrix(x, y, z);
        m.transform(v);
    }

    public static Point3f rotateNew(Point3f vOrig, float x, float y, float z)
    {
        Point3f v = new Point3f(vOrig);
        rotateBy(v, x, y, z);
        return v;
    }

    public static Point3f interpolate(Point3f t1, Point3f t2, float alpha)
    {
        Point3f v = new Point3f();
        v.interpolate(t1, t2, alpha);
        return v;
    }

	public static Point3f add(Point3f a, Point3f b) 
	{
		Point3f ret = new Point3f();
		ret.add(a, b);
		return ret;
	}

	public static Point3f sub(Point3f a, Point3f b) 
	{
		Point3f ret = new Point3f();
		ret.sub(a, b);
		return ret;
	}

	public static Point3f scale(Point3f v, float m) 
	{
		Point3f ret = new Point3f(v);
		ret.scale(m);
		return ret;
	}

	public static Point3f cross(Point3f a, Point3f b) 
	{
		Point3f ret = new Point3f();
        float x = a.y*b.z - a.z*b.y;
        float y = b.x*a.z - b.z*a.x;
        ret.z = a.x*b.y - a.y*b.x;
        ret.x = x;
        ret.y = y;
		return ret;
	}

	public static boolean equals(Point3f a, Point3f b) {
		return isZero(a.distance(b));
	}

	public static Point3f fromString(String txt)
	{
		StringTokenizer st = new StringTokenizer(txt, ", ");
		if (st.countTokens() != 3)
			throw new IllegalArgumentException("Expected three values: '"+txt+"', got "+st.countTokens());
		Point3f p = new Point3f();
		p.x = FloatUtils.parseFloat(st.nextToken());
		p.y = FloatUtils.parseFloat(st.nextToken());
		p.z = FloatUtils.parseFloat(st.nextToken());
		return p;
	}
}

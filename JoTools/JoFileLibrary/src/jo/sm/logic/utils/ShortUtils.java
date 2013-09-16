/*
 * Created on Sep 27, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.sm.logic.utils;


public class ShortUtils
{
    public static short parseShort(Object o)
    {
    	if (o == null)
    		return 0;
    	if (o instanceof Number)
    		return ((Number)o).shortValue();
    	return parseShort(o.toString());
    }

    public static short parseShort(String str)
    {
        try
        {
            if (str == null)
                return 0;
            str = str.trim();
            if (str.startsWith("+"))
                str = str.substring(1);
            int o = str.indexOf('.');
            if (o >= 0)
                str = str.substring(0, o);
            return Short.parseShort(str);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    public static Object[] toArray(short[] shortArray)
    {
        if (shortArray == null)
            return null;
        Short[] objArray = new Short[shortArray.length];
        for (int i = 0; i < shortArray.length; i++)
            objArray[i] = Short.valueOf(shortArray[i]);
        return objArray;
    }

    public static String toString(short[] arr)
    {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < arr.length; i++)
        {
            if (i > 0)
                sb.append(", ");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

	public static short[] toShortArray(Object[] array)
	{
		if (array == null)
			return null;
		short[] ret = new short[array.length];
		for (int i = 0; i < array.length; i++)
			ret[i] = parseShort(array[i]);
		return ret;
	}
}

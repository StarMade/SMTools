package jo.sm.logic.utils;

public class ConvertLogic
{
    public static Object toObject(String sVal, Class<?> propertyType)
    {
        Object oVal = null;
        if (sVal != null)
        {
            if ((propertyType == boolean.class) || (propertyType == Boolean.class))
                try
                {
                    oVal = Boolean.parseBoolean(sVal);
                }
                catch (Exception e)
                {
                    oVal = Boolean.FALSE;
                }
            else if ((propertyType == byte.class) || (propertyType == Byte.class))
                try
                {
                    oVal = Byte.parseByte(sVal);
                }
                catch (Exception e)
                {
                    oVal = (byte)0;
                }
            else if ((propertyType == short.class) || (propertyType == Short.class))
                try
                {
                    oVal = Short.parseShort(sVal);
                }
                catch (Exception e)
                {
                    oVal = (short)0;
                }
            else if ((propertyType == int.class) || (propertyType == Integer.class))
                try
                {
                    oVal = Integer.parseInt(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0;
                }
            else if ((propertyType == long.class) || (propertyType == Long.class))
                try
                {
                    oVal = Long.parseLong(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0L;
                }
            else if ((propertyType == float.class) || (propertyType == Float.class))
                try
                {
                    oVal = Float.parseFloat(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0f;
                }
            else if ((propertyType == double.class) || (propertyType == Double.class))
                try
                {
                    oVal = Double.parseDouble(sVal);
                }
                catch (Exception e)
                {
                    oVal = 0.0;
                }
            else if ((propertyType == char.class) || (propertyType == Character.class))
            {
                if (sVal.trim().length() > 0)
                    oVal = sVal.trim().charAt(0);
            }
            else if (propertyType == String.class)
                oVal = sVal;
            else
                throw new IllegalArgumentException("Cannot handle converting type '"+propertyType.getName()+"'");
        }
        return oVal;        
    }
}

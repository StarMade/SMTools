package jo.sm.plugins.ship.imp;

public class VRMLNode
{
    private String  mName;
    private Object  mValue;
    
    public VRMLNode()
    {
    }
    
    public VRMLNode(String name)
    {
        mName = name;
    }
    
    public VRMLNode(String name, Object value)
    {
        mName = name;
        mValue = value;
    }
    
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }
    public Object getValue()
    {
        return mValue;
    }
    public void setValue(Object value)
    {
        mValue = value;
    }
}

package jo.sm.ent.data;

import java.util.ArrayList;
import java.util.List;

public class ControlElement
{
    private long                    mIndex;
    private List<ControlSubElement> mElements;
    
    public ControlElement()
    {
        mElements = new ArrayList<ControlSubElement>();
    }

    public long getIndex()
    {
        return mIndex;
    }

    public void setIndex(long something)
    {
        mIndex = something;
    }

    public List<ControlSubElement> getElements()
    {
        return mElements;
    }

    public void setElements(List<ControlSubElement> elements)
    {
        mElements = elements;
    }
}

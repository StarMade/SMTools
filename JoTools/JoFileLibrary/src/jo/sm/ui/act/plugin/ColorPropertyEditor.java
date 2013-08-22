package jo.sm.ui.act.plugin;

import java.beans.PropertyEditorSupport;

import jo.sm.data.BlockTypes;

public class ColorPropertyEditor extends PropertyEditorSupport
{
    public ColorPropertyEditor()
    {
        super();
    }

    public ColorPropertyEditor(Object bean)
    {
        super(bean);
    }

    @Override
    public String getAsText()
    {
        Short c = (Short)getValue();
        if (c == null)
            c = BlockTypes.HULL_COLOR_GREY_ID;
        String txt = "Grey";
        switch (c)
        {
            case BlockTypes.HULL_COLOR_GREY_ID:
                txt = "Grey";
                break;
            case BlockTypes.HULL_COLOR_PURPLE_ID:
                txt = "Purple";
                break;
            case BlockTypes.HULL_COLOR_BROWN_ID:
                txt = "Brown";
                break;
            case BlockTypes.HULL_COLOR_BLACK_ID:
                txt = "Black";
                break;
            case BlockTypes.HULL_COLOR_RED_ID:
                txt = "Red";
                break;
            case BlockTypes.HULL_COLOR_BLUE_ID:
                txt = "Blue";
                break;
            case BlockTypes.HULL_COLOR_GREEN_ID:
                txt = "Green";
                break;
            case BlockTypes.HULL_COLOR_YELLOW_ID:
                txt = "Yellow";
                break;
            case BlockTypes.HULL_COLOR_WHITE_ID:
                txt = "White";
                break;
        }
        System.out.println("Getting "+getValue()+" -> "+txt);
        return txt;
    }
    
    
    @Override
    public String[] getTags()
    {
        return new String[] {
         "Grey",
         "Purple",
         "Brown",
         "Black",
         "Red",
         "Blue",
         "Green",
         "Yellow",
         "White",
        };
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException
    {
        if (text.equals("Grey"))
            setValue(BlockTypes.HULL_COLOR_GREY_ID);
        else if (text.equals("Purple"))
            setValue(BlockTypes.HULL_COLOR_PURPLE_ID);
        else if (text.equals("Brown"))
            setValue(BlockTypes.HULL_COLOR_BROWN_ID);
        else if (text.equals("Black"))
            setValue(BlockTypes.HULL_COLOR_BLACK_ID);
        else if (text.equals("Red"))
            setValue(BlockTypes.HULL_COLOR_RED_ID);
        else if (text.equals("Blue"))
            setValue(BlockTypes.HULL_COLOR_BLUE_ID);
        else if (text.equals("Green"))
            setValue(BlockTypes.HULL_COLOR_GREEN_ID);
        else if (text.equals("Yellow"))
            setValue(BlockTypes.HULL_COLOR_YELLOW_ID);
        else if (text.equals("White"))
            setValue(BlockTypes.HULL_COLOR_YELLOW_ID);
        else
            setValue(BlockTypes.HULL_COLOR_GREY_ID);
        System.out.println("Setting "+text+" -> "+getValue());
    }
}

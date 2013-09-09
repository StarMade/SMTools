package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class UndoAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public UndoAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Undo");
        setToolTipText("Undo last change");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        mFrame.getClient().undo();
    }

}

package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class RedoAction extends GenericAction
{
    private RenderFrame mFrame;
    
    public RedoAction(RenderFrame frame)
    {
        mFrame = frame;
        setName("Redo");
        setToolTipText("Redo last undo");
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
        mFrame.getClient().redo();
    }
}

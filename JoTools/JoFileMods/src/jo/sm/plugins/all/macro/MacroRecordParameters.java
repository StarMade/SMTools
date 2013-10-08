package jo.sm.plugins.all.macro;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Record Macro", shortDescription="Save a Javascript macro to your disk")
public class MacroRecordParameters
{
    @Description(displayName="File", shortDescription="Path to Javascript macro file")
    private String  mFile;
    
    public MacroRecordParameters()
    {
    }

    public String getFile()
    {
        return mFile;
    }

    public void setFile(String file)
    {
        mFile = file;
    }
}

package jo.sm.plugins.all.macro;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Run Macro", shortDescription="Run a Javascript macro from your disk")
public class MacroRunParameters
{
    @Description(displayName="File", shortDescription="Path to Javascript macro file")
    private String  mFile;
    
    public MacroRunParameters()
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

package jo.sm.plugins.ship.imp;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Import Binvox Model", shortDescription="Import a model in binvox format from your disk")
public class ImportBinvoxParameters
{
	@Description(displayName="File", shortDescription="Path to binvox model")
    private String  mFile;
    
    public ImportBinvoxParameters()
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

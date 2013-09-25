package jo.sm.plugins.ship.exp;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Export object as Collada", shortDescription="Exports model plus imagemap in Collada file format")
public class ExportDAEParameters
{
    @Description(displayName="", shortDescription="Collada file (including DAE extension) to export to")
    private String  mFile;
    
    public ExportDAEParameters()
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

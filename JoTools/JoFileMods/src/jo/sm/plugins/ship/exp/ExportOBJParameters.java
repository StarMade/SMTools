package jo.sm.plugins.ship.exp;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Export object as OBJ", shortDescription="Exports model plus imagemap in Wavefront OBJ file format")
public class ExportOBJParameters
{
    @Description(displayName="", shortDescription="OBJ file (including extension) to export to")
    private String  mFile;
    
    public ExportOBJParameters()
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

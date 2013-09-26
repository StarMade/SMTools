package jo.sm.plugins.ship.text;

import jo.sm.ui.act.plugin.Description;

@Description(displayName="Draw Image on Model", shortDescription="Draw an approximation of this image on the model using hull blocks")
public class ImageParameters
{
	@Description(displayName="Image File", shortDescription="Image to use")
    private String  mFile;
    
    public ImageParameters()
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

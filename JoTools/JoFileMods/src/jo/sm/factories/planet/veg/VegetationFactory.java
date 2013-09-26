package jo.sm.factories.planet.veg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jo.sm.logic.StarMadeLogic;
import jo.sm.logic.utils.FloatUtils;
import jo.sm.logic.utils.IntegerUtils;
import jo.sm.logic.utils.ResourceUtils;
import jo.sm.logic.utils.ShortUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IStarMadePlugin;
import jo.sm.mods.IStarMadePluginFactory;
import jo.sm.ui.BlockTypeColors;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class VegetationFactory implements IStarMadePluginFactory
{
	private List<VegetationDefinition>	mDefs = new ArrayList<VegetationDefinition>();
	private List<IStarMadePlugin>	mPlugins = new ArrayList<IStarMadePlugin>();
	
	public VegetationFactory()
	{
		loadDefinitions();
		loadPlugins();
	}

	@Override
	public IStarMadePlugin[] getPlugins()
	{
		return mPlugins.toArray(new IStarMadePlugin[0]);
	}
	
	private void loadPlugins()
	{
		for (VegetationDefinition fd : mDefs)
			mPlugins.add(new VegetationPlugin(fd));
	}
	
	private void loadDefinitions()
	{
		File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
		File viewFilters = new File(jo_plugins, "SurfaceVegetation.xml");
		Document xml;
		if (viewFilters.exists())
			xml = XMLUtils.readFile(viewFilters);
		else
			xml = XMLUtils.readStream(ResourceUtils.loadSystemResourceStream("SurfaceVegetation.xml", VegetationFactory.class));
		loadDefinitions(xml);
	}
	
	private void loadDefinitions(Document xml)
	{
		BlockTypeColors.loadBlockIcons();
		Node fs = XMLUtils.findFirstNode(xml, "vegetations");
		String baseAuthor = XMLUtils.getAttribute(fs, "author");
		for (Node f : XMLUtils.findNodes(fs, "vegetation"))
		{
			String title = XMLUtils.getAttribute(f, "title");
            if (StringUtils.isTrivial(title))
                continue;
            String desc = XMLUtils.getAttribute(f, "descriptions");
            String author = XMLUtils.getAttribute(f, "author");
            int priority = IntegerUtils.parseInt(XMLUtils.getAttribute(f, "priority"));
            if (StringUtils.isTrivial(author))
                author = baseAuthor;
            float density = FloatUtils.parseFloat(XMLUtils.getAttribute(f, "density"));
            if (density > 1)
                density = 1/density;
            VegetationDefinition def = new VegetationDefinition();
            def.setTitle(title);
            def.setDescription(desc);
            def.setAuthor(author);
            def.setPriority(priority);
            def.setDensity(density);
			for (Node ob : XMLUtils.findNodes(f, "vegetable"))
			{
			    VegetationEntry entry = parseEntry(ob);
                if (entry == null)
                    continue;
                if ((entry.getPercent() <= 0) || (entry.getPercent() > 100))
                {
                    System.err.println("Vegetables must have percentage between 1 and 100: "+XMLUtils.writeString(ob));
                    continue;
                }
		        def.getVegetation().add(entry);
			}
			mDefs.add(def);
		}
	}
	
	private VegetationEntry parseEntry(Node b)
	{
	    VegetationEntry entry = new VegetationEntry();
        String id = XMLUtils.getAttribute(b, "type");
        if ("*".equals(id))
            entry.setBlockID((short)-1);
        else if (BlockTypeColors.mBlockTypes.containsKey(id))
            entry.setBlockID(ShortUtils.parseShort(BlockTypeColors.mBlockTypes.get(id)));
        else
        {
            short val = ShortUtils.parseShort(id);
            if (val > 0)
                entry.setBlockID(val);
            else
            {
                System.err.println("Unknown block type '"+id+"' : "+XMLUtils.writeString(b));
                return null;
            }
        }
        entry.setPercent(IntegerUtils.parseInt(XMLUtils.getAttribute(b, "percent")));
        int[] low = parseRange(XMLUtils.getAttribute(b, "low"));
        if (low != null)
        {
            entry.setLowStrategy(low[0]);
            entry.setLowValue(low[1]);
        }
        int[] high = parseRange(XMLUtils.getAttribute(b, "high"));
        if (high != null)
        {
            entry.setHighStrategy(high[0]);
            entry.setHighValue(high[1]);
        }
	    return entry;
	}
	
	private int[] parseRange(String range)
	{
	    if (StringUtils.isTrivial(range))
	        return null;
	    int[] ret = new int[2];
	    if (range.endsWith("%"))
	    {
	        ret[0] = VegetationEntry.ABSOLUTE;
	        ret[1] = Integer.parseInt(range.substring(0, range.length() - 1));
	    }
	    else
        {
            ret[0] = VegetationEntry.RELATIVE;
            ret[1] = Integer.parseInt(range);
        }
	    return ret;
	}
}

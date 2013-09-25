package jo.sm.factories.planet.comp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jo.sm.logic.StarMadeLogic;
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

public class MaterialFactory implements IStarMadePluginFactory
{
	private List<MaterialDefinition>	mDefs = new ArrayList<MaterialDefinition>();
	private List<IStarMadePlugin>	mPlugins = new ArrayList<IStarMadePlugin>();
	
	public MaterialFactory()
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
		for (MaterialDefinition fd : mDefs)
			mPlugins.add(new MaterialPlugin(fd));
	}
	
	private void loadDefinitions()
	{
		File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
		File viewFilters = new File(jo_plugins, "MaterialComposition.xml");
		Document xml;
		if (viewFilters.exists())
			xml = XMLUtils.readFile(viewFilters);
		else
			xml = XMLUtils.readStream(ResourceUtils.loadSystemResourceStream("MaterialComposition.xml", MaterialFactory.class));
		loadDefinitions(xml);
	}
	
	private void loadDefinitions(Document xml)
	{
		BlockTypeColors.loadBlockIcons();
		Node fs = XMLUtils.findFirstNode(xml, "compositions");
		String baseAuthor = XMLUtils.getAttribute(fs, "author");
		for (Node f : XMLUtils.findNodes(fs, "composition"))
		{
			String title = XMLUtils.getAttribute(f, "title");
			String desc = XMLUtils.getAttribute(f, "descriptions");
			String author = XMLUtils.getAttribute(f, "author");
			int priority = IntegerUtils.parseInt(XMLUtils.getAttribute(f, "priority"));
			if (StringUtils.isTrivial(author))
				author = baseAuthor;
            if (StringUtils.isTrivial(title))
                continue;
            MaterialDefinition def = new MaterialDefinition();
            def.setTitle(title);
            def.setDescription(desc);
            def.setAuthor(author);
            def.setPriority(priority);
			for (Node ob : XMLUtils.findNodes(f, "oldBlock"))
			{
			    MaterialEntry entry = parseEntry(ob);
                if (entry == null)
                    continue;
                if ((entry.getPercent() <= 0) || (entry.getPercent() > 100))
                {
                    System.err.println("Old block must have percentage between 1 and 100: "+XMLUtils.writeString(ob));
                    continue;
                }
		        def.getOldBlocks().add(entry);
			}
            for (Node nb : XMLUtils.findNodes(f, "newBlock"))
            {
                MaterialEntry entry = parseEntry(nb);
                if (entry == null)
                    continue;
                if (entry.getBlockID() <= 0)
                {
                    System.err.println("New block must have specific type: "+XMLUtils.writeString(nb));
                    continue;
                }
                if (entry.getPercent() <= 0)
                    entry.setPercent(1);
                def.getNewBlocks().add(entry);
            }
			mDefs.add(def);
		}
	}
	
	private MaterialEntry parseEntry(Node b)
	{
	    MaterialEntry entry = new MaterialEntry();
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
        if (low == null)
            return null;
        entry.setLowStrategy(low[0]);
        entry.setLowValue(low[1]);
        int[] high = parseRange(XMLUtils.getAttribute(b, "high"));
        if (high == null)
            return null;
        entry.setHighStrategy(high[0]);
        entry.setHighValue(high[1]);
	    return entry;
	}
	
	private int[] parseRange(String range)
	{
	    int[] ret = new int[2];
	    if (range.endsWith("%"))
	    {
	        ret[0] = MaterialEntry.ABSOLUTE;
	        ret[1] = Integer.parseInt(range.substring(0, range.length() - 1));
	    }
	    else
        {
            ret[0] = MaterialEntry.RELATIVE;
            ret[1] = Integer.parseInt(range);
        }
	    return ret;
	}
}

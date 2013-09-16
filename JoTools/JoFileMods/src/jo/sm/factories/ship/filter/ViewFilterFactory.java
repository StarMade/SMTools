package jo.sm.factories.ship.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

public class ViewFilterFactory implements IStarMadePluginFactory
{
	private List<FilterDefinition>	mDefs = new ArrayList<FilterDefinition>();
	private List<IStarMadePlugin>	mPlugins = new ArrayList<IStarMadePlugin>();
	
	public ViewFilterFactory()
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
		for (FilterDefinition fd : mDefs)
			mPlugins.add(new SelectFilterPlugin(fd));
	}
	
	private void loadDefinitions()
	{
		File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
		File viewFilters = new File(jo_plugins, "ViewFilters.xml");
		Document xml;
		if (viewFilters.exists())
			xml = XMLUtils.readFile(viewFilters);
		else
			xml = XMLUtils.readStream(ResourceUtils.loadSystemResourceStream("ViewFilters.xml", ViewFilterFactory.class));
		loadDefinitions(xml);
	}
	
	private void loadDefinitions(Document xml)
	{
		BlockTypeColors.loadBlockIcons();
		Node fs = XMLUtils.findFirstNode(xml, "filters");
		String baseAuthor = XMLUtils.getAttribute(fs, "author");
		for (Node f : XMLUtils.findNodes(fs, "filter"))
		{
			String title = XMLUtils.getAttribute(f, "title");
			String desc = XMLUtils.getAttribute(f, "descriptions");
			String author = XMLUtils.getAttribute(f, "author");
			int priority = IntegerUtils.parseInt(XMLUtils.getAttribute(f, "priority"));
			if (StringUtils.isTrivial(author))
				author = baseAuthor;
			Set<Short> ids = new HashSet<Short>();
			for (Node b : XMLUtils.findNodes(f, "block"))
			{
				String id = XMLUtils.getAttribute(b, "type");
				if (BlockTypeColors.mBlockTypes.containsKey(id))
					ids.add(ShortUtils.parseShort(BlockTypeColors.mBlockTypes.get(id)));
				else
				{
					short val = ShortUtils.parseShort(id);
					if (val > 0)
						ids.add(val);
				}
			}
			if (!StringUtils.isTrivial(title))
			{
				FilterDefinition def = new FilterDefinition();
				def.setTitle(title);
				def.setDescription(desc);
				def.setAuthor(author);
				def.setPriority(priority);
				def.setBlocks(ids);
				mDefs.add(def);
			}
		}
	}
}

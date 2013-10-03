package jo.sm.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.sm.data.ShapeLibraryEntry;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.utils.FileUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.logic.utils.XMLEditUtils;
import jo.sm.logic.utils.XMLUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ShapeLibraryLogic
{
    private static long                            mLastRead = 0; 
    private static final List<ShapeLibraryEntry>   mEntries = new ArrayList<ShapeLibraryEntry>();
    
    public static void update()
    {
        File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
        File shapeLibDir = new File(jo_plugins, "shapeLibrary");
        if (!shapeLibDir.exists())
        {
            mLastRead = 0;
            mEntries.clear();
            return;
        }
        if (shapeLibDir.lastModified() <= mLastRead)
            return; // up to date
        // check files
        for (File shapeFile : shapeLibDir.listFiles())
        {
            ShapeLibraryEntry entry = findByFile(shapeFile);
            if (entry != null)
            {
                if (entry.getLastRead() >= shapeFile.lastModified())
                    continue;
                else
                    mEntries.remove(entry);
            }
            entry = new ShapeLibraryEntry();
            entry.setShape(shapeFile);
            try
            {
                updateEntry(entry);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            mEntries.add(entry);
        }
    }
    
    private static void updateEntry(ShapeLibraryEntry entry) throws IOException
    {
        String xml = FileUtils.readFileAsString(entry.getShape().toString(), 1024);
        String name = getAttribute(xml, "name");
        if (StringUtils.isTrivial(name))
        {
            name = entry.getShape().getName();
            if (name.endsWith(".xml"))
                name = name.substring(0, name.length() - 4);
        }
        entry.setName(name);
        String author = getAttribute(xml, "author");
        if (StringUtils.isTrivial(author))
            author = "A. N. Onomous";
        entry.setAuthor(author);
        String lower = getAttribute(xml, "lower");
        if (!StringUtils.isTrivial(lower))
            entry.setLower(new Point3i(lower));
        else
            entry.setLower(new Point3i());
        String upper = getAttribute(xml, "upper");
        if (!StringUtils.isTrivial(upper))
            entry.setUpper(new Point3i(upper));
        else
            entry.setUpper(new Point3i());
        String classes = getAttribute(xml, "classifications");
        if (!StringUtils.isTrivial(classes))
        {
            classes = classes.toLowerCase();
            if (classes.indexOf("ship") >= 0)
                entry.getClassifications().add(IBlocksPlugin.TYPE_SHIP);
            if (classes.indexOf("shop") >= 0)
                entry.getClassifications().add(IBlocksPlugin.TYPE_SHOP);
            if (classes.indexOf("station") >= 0)
                entry.getClassifications().add(IBlocksPlugin.TYPE_STATION);
            if (classes.indexOf("planet") >= 0)
                entry.getClassifications().add(IBlocksPlugin.TYPE_PLANET);
            if (classes.indexOf("floatingrock") >= 0)
                entry.getClassifications().add(IBlocksPlugin.TYPE_FLOATINGROCK);
        }
        if (entry.getClassifications().size() == 0)
            entry.getClassifications().add(IBlocksPlugin.TYPE_ALL);
        entry.setLastRead(entry.getShape().lastModified());
    }
    
    private static String getAttribute(String xml, String name)
    {
        int o = xml.indexOf(name);
        if (o < 0)
            return null;
        xml = xml.substring(o + name.length()).trim();
        if (!xml.startsWith("="))
            return null;
        xml = xml.substring(1).trim();
        if (xml.startsWith("\""))
        {
            xml = xml.substring(1);
            o = xml.indexOf("\"");
            if (o < 0)
                return null;
            return xml.substring(0, o);
        }
        if (xml.startsWith("\'"))
        {
            xml = xml.substring(1);
            o = xml.indexOf("\'");
            if (o < 0)
                return null;
            return xml.substring(0, o);
        }
        return null;
    }
    
    public static ShapeLibraryEntry findByFile(File shapeFile)
    {
        for (ShapeLibraryEntry entry : mEntries)
            if (shapeFile.equals(entry))
                return entry;
        return null;
    }
    
    public static List<ShapeLibraryEntry> getEntries(int type)
    {
        update();
        List<ShapeLibraryEntry> entries = new ArrayList<ShapeLibraryEntry>();
        for (ShapeLibraryEntry entry : mEntries)
            if (isType(entry, type))
                entries.add(entry);
        return entries;
    }
    
    public static ShapeLibraryEntry getEntry(int unid)
    {
    	for (ShapeLibraryEntry entry : mEntries)
    		if (entry.getUNID() == unid)
    			return entry;
    	return null;
    }
    
    public static void addEntry(SparseMatrix<Block> grid, String name, String author, int type)
    {
        Document doc = GridLogic.toDocument(grid);
        Node root = doc.getFirstChild();
        if (!StringUtils.isTrivial(name))
            XMLEditUtils.addAttribute(root, "name", name);
        if (!StringUtils.isTrivial(author))
            XMLEditUtils.addAttribute(root, "author", author);
        switch (type)
        {
            case IBlocksPlugin.TYPE_ALL:
                XMLEditUtils.addAttribute(root, "classifications", "all");
                break;
            case IBlocksPlugin.TYPE_FLOATINGROCK:
                XMLEditUtils.addAttribute(root, "classifications", "floatingrock");
                break;
            case IBlocksPlugin.TYPE_SHIP:
                XMLEditUtils.addAttribute(root, "classifications", "ship");
                break;
            case IBlocksPlugin.TYPE_SHOP:
                XMLEditUtils.addAttribute(root, "classifications", "shop");
                break;
            case IBlocksPlugin.TYPE_STATION:
                XMLEditUtils.addAttribute(root, "classifications", "station");
                break;
            case IBlocksPlugin.TYPE_PLANET:
                XMLEditUtils.addAttribute(root, "classifications", "planet");
                break;
            default:
                XMLEditUtils.addAttribute(root, "classifications", "all");
                break;
        }
        File jo_plugins = new File(StarMadeLogic.getInstance().getBaseDir(), "jo_plugins");
        File shapeLibDir = new File(jo_plugins, "shapeLibrary");
        if (!shapeLibDir.exists())
            shapeLibDir.mkdirs();
        File shapeFile = new File(shapeLibDir, name+".xml");
        XMLUtils.writeFile(root, shapeFile);
        mLastRead = 0;
    }
    
    private static boolean isType(ShapeLibraryEntry entry, int type)
    {
        if (type == IBlocksPlugin.TYPE_ALL)
            return true;
        if (entry.getClassifications().size() == 0)
            return true;
        if (entry.getClassifications().contains(IBlocksPlugin.TYPE_ALL))
            return true;
        return entry.getClassifications().contains(type);
    }
    
    public static Map<String,Object> getEntryMap()
    {
    	Map<String,Object> shapeMap = new HashMap<String, Object>();
    	int type = StarMadeLogic.getInstance().getCurrentModel().getType();
    	for (ShapeLibraryEntry entry : ShapeLibraryLogic.getEntries(type))
    		shapeMap.put(entry.getName(), entry.getUNID());
    	if (shapeMap.size() == 0)
    		shapeMap.put("No shapes recorded", -1);
    	return shapeMap;
    }
}

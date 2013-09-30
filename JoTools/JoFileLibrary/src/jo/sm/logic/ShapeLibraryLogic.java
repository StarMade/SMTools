package jo.sm.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jo.sm.data.ShapeLibraryEntry;
import jo.sm.data.SparseMatrix;
import jo.sm.logic.utils.FileUtils;
import jo.sm.logic.utils.StringUtils;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.ship.data.Block;
import jo.vecmath.Point3i;

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
    
    public static void addEntry(SparseMatrix<Block> grid, String name, String author, int type)
    {
        
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
}

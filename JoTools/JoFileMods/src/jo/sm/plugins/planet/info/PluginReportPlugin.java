package jo.sm.plugins.planet.info;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;

public class PluginReportPlugin implements IBlocksPlugin
{
    public static final String NAME = "Plugin Report";
    public static final String DESC = "Report on all plugins";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_EDIT, 95 },
        { TYPE_STATION, SUBTYPE_EDIT, 95 },
        { TYPE_SHOP, SUBTYPE_EDIT, 95 },
        { TYPE_FLOATINGROCK, SUBTYPE_EDIT, 95 },
        { TYPE_PLANET, SUBTYPE_EDIT, 95 },
        };

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public String getDescription()
    {
        return DESC;
    }

    @Override
    public String getAuthor()
    {
        return AUTH;
    }

    @Override
    public Object getParameterBean()
    {
        return null;
    }

    @Override
    public int[][] getClassifications()
    {
        return CLASSIFICATIONS;
    }

    @Override
    public SparseMatrix<Block> modify(SparseMatrix<Block> original,
            Object p, StarMade sm, IPluginCallback cb)
    {
        try
        {
            File repFile = File.createTempFile("smReport", ".txt");
            PrintWriter wtr = new PrintWriter(repFile);
            reportBlockPlugins(wtr, sm);
            wtr.close();
            if (Desktop.isDesktopSupported())
                Desktop.getDesktop().open(repFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private void reportBlockPlugins(PrintWriter wtr, StarMade sm)
    {
        wtr.println("BLOCK PLUGINS");
        wtr.println("~~~~~~~~~~~~~");
        wtr.println();
        for (IBlocksPlugin plugin : sm.getBlocksPlugins())
        {
            wtr.println("Name: "+plugin.getName());
            wtr.println("Author: "+plugin.getAuthor());
            wtr.println(plugin.getDescription());
            wtr.println("Classifications:");
            for (int[] classification : plugin.getClassifications())
            {
                switch (classification[0])
                {
                    case TYPE_FLOATINGROCK:
                        wtr.print("Floating Rock ");
                        break;
                    case TYPE_PLANET:
                        wtr.print("Planet ");
                        break;
                    case TYPE_SHIP:
                        wtr.print("Ship ");
                        break;
                    case TYPE_SHOP:
                        wtr.print("Shop ");
                        break;
                    case TYPE_STATION:
                        wtr.print("Station ");
                        break;
                    default:
                        wtr.print(classification[0]+" ");
                        break;
                }
                switch (classification[1])
                {
                    case SUBTYPE_EDIT:
                        wtr.print("- Edit ");
                        break;
                    case SUBTYPE_FILE:
                        wtr.print("- File ");
                        break;
                    case SUBTYPE_GENERATE:
                        wtr.print("- Generate ");
                        break;
                    case SUBTYPE_MODIFY:
                        wtr.print("- Modify ");
                        break;
                    case SUBTYPE_PAINT:
                        wtr.print("- Paint ");
                        break;
                    case SUBTYPE_VIEW:
                        wtr.print("- Paint ");
                        break;
                    default:
                        wtr.print("- "+classification[1]+" ");
                        break;
                }
                if (classification.length > 2)
                    wtr.print("("+classification[2]+")");
                wtr.println();
            }
            wtr.println();
        }
    }
}

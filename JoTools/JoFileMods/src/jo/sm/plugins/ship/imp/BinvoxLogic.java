package jo.sm.plugins.ship.imp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import jo.vecmath.Point3i;

public class BinvoxLogic
{
    public static BinvoxData read(InputStream is) throws IOException
    {
        BinvoxData data = new BinvoxData();
        DataInputStream dis = new DataInputStream(is);

        //
        // read header
        //
        String line = readLine(dis); // deprecated function though
        if (!line.startsWith("#binvox"))
        {
            System.out.println("Error: first line reads [" + line
                    + "] instead of [#binvox]");
            return null;
        }

        String version_string = line.substring(8);
        int version = Integer.parseInt(version_string);
        System.out.println("reading binvox version " + version);

        data.setDepth(0);
        data.setHeight(0);
        data.setWidth(0);
        boolean done = false;

        while (!done)
        {

            line = readLine(dis);

            if (line.startsWith("data"))
                done = true;
            else
            {
                if (line.startsWith("dim"))
                {
                    String[] dimensions = line.split(" ");
                    data.setDepth(Integer.parseInt(dimensions[1]));
                    data.setHeight(Integer.parseInt(dimensions[2]));
                    data.setWidth(Integer.parseInt(dimensions[3]));
                }
                else
                {
                    if (line.startsWith("translate"))
                    {
                        String[] dimensions = line.split(" ");
                        data.setTX(Double.parseDouble(dimensions[1]));
                        data.setTY(Double.parseDouble(dimensions[2]));
                        data.setTZ(Double.parseDouble(dimensions[3]));
                    }
                    else
                    {
                        if (line.startsWith("scale"))
                        {
                            String[] dimensions = line.split(" ");
                            data.setScale(Double.parseDouble(dimensions[1]));
                        }
                        else
                        {
                            System.out.println("  unrecognized keyword ["
                                    + line + "], skipping");
                        }
                    }
                }
            }
        }

        if (!done)
        {
            System.out.println("  error reading header");
            return null;
        }
        if (data.getDepth() == 0)
        {
            System.out.println("  missing dimensions in header");
            return null;
        }

        data.setSize(data.getWidth() * data.getHeight() * data.getDepth());
        data.setVoxels(new byte[data.getSize()]);

        //
        // read voxel data
        //
        byte value;
        int count;
        int index = 0;
        int end_index = 0;
        int nr_voxels = 0;

        // *input >> value; // read the linefeed char

        while (end_index < data.getSize())
        {

            value = dis.readByte();
            // idiotic Java language doesn't have unsigned types, so we have to
            // use an int for 'count'
            // and make sure that we don't interpret it as a negative number if
            // bit 7 (the sign bit) is on
            count = dis.readByte() & 0xff;

            end_index = index + count;
            if (end_index > data.getSize())
                return null;
            for (int i = index; i < end_index; i++)
                data.getVoxels()[i] = value;

            if (value > 0)
                nr_voxels += count;
            index = end_index;

        } // while

        System.out.println("  read " + nr_voxels + " voxels");
        return data;

    } // read_binvox
    
    private static String readLine(InputStream dis) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        for (;;)
        {
            int ch = dis.read();
            if (ch == -1)
                break;
            if (ch == '\r')
                continue;
            if (ch == '\n')
                break;
            sb.append((char)ch);
        }
        return sb.toString();
    }

    public static void getBounds(BinvoxData hull, Point3i lower, Point3i upper)
    {
        boolean first = true;
        int o = 0;
        for (int x = 0; x < hull.getWidth(); x++)
        {
            for (int z = 0; z < hull.getDepth(); z++)
            {
                for (int y = 0; y < hull.getWidth(); y++)
                {
                    if (hull.getVoxels()[o++] == 0)
                        continue;
                    if (first)
                    {
                        lower.x = x;
                        lower.y = y;
                        lower.z = z;
                        upper.x = x;
                        upper.y = y;
                        upper.z = z;
                        first = false;
                    }
                    else
                    {
                        lower.x = Math.min(lower.x, x);
                        lower.y = Math.min(lower.y, y);
                        lower.z = Math.min(lower.z, z);
                        upper.x = Math.max(upper.x, x);
                        upper.y = Math.max(upper.y, y);
                        upper.z = Math.max(upper.z, z);
                    }
                }
            }
        }
    }

}

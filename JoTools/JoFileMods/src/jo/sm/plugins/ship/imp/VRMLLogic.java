package jo.sm.plugins.ship.imp;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class VRMLLogic
{
    public static List<VRMLNode> read(Reader r) throws IOException
    {
        PushbackReader rdr = new PushbackReader(r);
        List<VRMLNode> nodes = new ArrayList<VRMLNode>();
        for (;;)
        {
            VRMLNode node = readNext(rdr);
            if (node == null)
                break;
            nodes.add(node);
        }
        return nodes;
    }
    
    private static VRMLNode readNext(PushbackReader rdr) throws IOException
    {
        int c = skipWhitespace(rdr);
        if (c == -1)
            return null;
        if (c == '}')
            return new VRMLNode("}");
        if (!Character.isJavaIdentifierStart((char)c))
            throw new IllegalArgumentException("Unexpected character '"+(char)c+"' while searching for tag");
        StringBuffer name = new StringBuffer();
        name.append((char)c);
        for (;;)
        {
            c = rdr.read();
            if (c == -1)
                return null;
            if (Character.isJavaIdentifierPart((char)c))
                name.append((char)c);
            else
                break;
        }
        if (Character.isWhitespace(c))
        {
            c = skipWhitespace(rdr);
            if (c == -1)
                return null;
        }
        //System.out.println("Reading "+name);
        if (c == '{')
        {   // map node
            List<VRMLNode> value = new ArrayList<VRMLNode>();
            for (;;)
            {
                VRMLNode next = readNext(rdr);
                if (next == null)
                    return null;
                if (next.getName().equals("}"))
                    break;
                value.add(next);
            }
            return new VRMLNode(name.toString(), value);
        }
        else if (c == '[')
        {   // array node
            List<String> value = new ArrayList<String>();
            for (;;)
            {
                String next = readValue(rdr, true);
                if (next == null)
                    return null;
                if (next.equals("]"))
                    break;
                value.add(next);
            }
            return new VRMLNode(name.toString(), value);
        }
        else
        {   // singleton
            List<String> value = new ArrayList<String>();
            for (;;)
            {
                String next = readValue(rdr, false);
                if (next == null)
                    return null;
                if (next.equals("\n"))
                    break;
                value.add(next);
            }
            return new VRMLNode(name.toString(), value);
        }
    }

    private static String readValue(PushbackReader rdr, boolean skipEOLN) throws IOException
    {
        StringBuffer value = new StringBuffer();
        int c = skipWhitespace(rdr, skipEOLN);
        if (c == -1)
            return null;
        if (c == ']')
            return "]";
        if (c == '\n')
            return "\n";
        boolean inQuote = false;
        if (c == '"')
            inQuote = true;
        else
            value.append((char)c);
        for (;;)
        {
            c = rdr.read();
            if (c == -1)
                return null;
            if (inQuote)
            {
                if (c == '"')
                    break;
            }
            else
            {
                if (Character.isWhitespace(c))
                    break;
                if (c == ']')
                {
                    rdr.unread(c);
                    break;
                }
            }
            value.append((char)c);
        }
        return value.toString();
    }
    
    private static int skipWhitespace(PushbackReader rdr) throws IOException
    {
        return skipWhitespace(rdr, true);
    }
    
    private static int skipWhitespace(PushbackReader rdr, boolean includingEOLN) throws IOException
    {
        boolean skipComment = false;
        for (;;)
        {
            int c = rdr.read();
            if (c == -1)
                return -1;
            if (skipComment)
            {
                if (c == '\n')
                {
                    if (!includingEOLN)
                        return c;
                    skipComment = false;
                }
            }
            else
            {
                if (c == '#')
                    skipComment = true;
                else
                    if (!Character.isWhitespace(c))
                        return c;
            }
        }
    }
}

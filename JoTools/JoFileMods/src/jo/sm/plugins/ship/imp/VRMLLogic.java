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
        rdr.unread(c);
        String name = readValue(rdr, true);
        if (name.equals("DEF"))
        {
        	name = readValue(rdr, true);
        	readValue(rdr, true);
        }
        c = skipWhitespace(rdr);
        if (c == -1)
            return null;
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
        	rdr.unread(c);
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
            if (c == '\n')
            {
            	rdr.unread(c);
            	break;
            }
            if (inQuote)
            {
                if (c == '"')
                    break;
            }
            else
            {
                if (isWhitespace(c))
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
                else if (c == '\n')
                {
                	if (!includingEOLN)
                		return c;
                }
                else
                    if (!isWhitespace(c))
                        return c;
            }
        }
    }
    
    private static boolean isWhitespace(int ch)
    {
    	return ((ch == ',') || Character.isWhitespace(ch));
    }
}

package jo.sm.plugins.ship.exp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import jo.sm.data.SparseMatrix;
import jo.sm.data.StarMade;
import jo.sm.mods.IBlocksPlugin;
import jo.sm.mods.IPluginCallback;
import jo.sm.ship.data.Block;
import jo.sm.ui.lwjgl.LWJGLRenderLogic;
import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.tri.JGLObj;

public class ExportOBJPlugin implements IBlocksPlugin
{
    public static final String NAME = "Export/OBJ";
    public static final String DESC = "Export OBJ file";
    public static final String AUTH = "Jo Jaquinta";
    public static final int[][] CLASSIFICATIONS = 
        {
        { TYPE_SHIP, SUBTYPE_MODIFY },
        { TYPE_STATION, SUBTYPE_MODIFY },
        { TYPE_SHOP, SUBTYPE_MODIFY },
        { TYPE_FLOATINGROCK, SUBTYPE_MODIFY },
        { TYPE_PLANET, SUBTYPE_MODIFY },
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
        return new ExportOBJParameters();
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
        ExportOBJParameters params = (ExportOBJParameters)p;        
        try
        {
        	JGLGroup quads = new JGLGroup();
        	LWJGLRenderLogic.addBlocks(quads, original);
            writeFile(params.getFile(), quads);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private void writeFile(String objFile, JGLGroup quads) throws IOException
    {
        BufferedWriter wtr = new BufferedWriter(new FileWriter(new File(objFile)));
        int vertPosition = 1;
        for (JGLNode node : quads.getChildren())
        	if (node instanceof JGLObj)
        	{
        		JGLObj obj = (JGLObj)node;
        		int vertCount = obj.getVertices();
        		FloatBuffer verts = obj.getVertexBuffer();
        		verts.rewind();
        		for (int i = 0; i < vertCount; i++)
        		{
        			float x = verts.get();
        			float y = verts.get();
        			float z = verts.get();
        			wtr.write("v "+x+" "+y+" "+z);
        			wtr.newLine();
        		}
        		int faceCount = obj.getIndices();
        		int facePoints = (obj.getMode() == JGLObj.TRIANGLES) ? 3 : 4;
        		ShortBuffer indexes = obj.getIndexBuffer();
        		indexes.rewind();
        		for (int i = 0; i < faceCount; i += facePoints)
        		{
        			wtr.write("f");
        			for (int j = 0; j < facePoints; j++)
        			{
        				short face = indexes.get();
        				wtr.write(" "+(face + vertPosition));
        			}
        			wtr.newLine();
        		}
        		vertPosition += vertCount;
            }
        wtr.close();
    }
}
